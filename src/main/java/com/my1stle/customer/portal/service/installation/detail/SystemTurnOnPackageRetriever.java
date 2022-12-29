package com.my1stle.customer.portal.service.installation.detail;

import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.files.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SystemTurnOnPackageRetriever {
	private static final String ABSOLUTE_PATH_TO_IN_PROGRESS_INSTALLATION_FOLDER = "/DEV_DATA/DESIGN";
	private static final String ABSOLUTE_PATH_TO_ARCHIVED_INSTALLATION_FOLDER = "/DEV_DATA/DESIGN";
	//private static final String ABSOLUTE_PATH_TO_IN_PROGRESS_INSTALLATION_FOLDER = "/Salesforce Documents/Installations";
	//private static final String ABSOLUTE_PATH_TO_ARCHIVED_INSTALLATION_FOLDER = "/Archived/Installations";
	private static final String SYSTEM_TURN_ON_PACKAGE_FILE_NAME = "STOP Package.pdf";

	private final DbxClientV2 dropboxClient;

	@Autowired
	public SystemTurnOnPackageRetriever(DbxClientV2 dropboxClient) {
		this.dropboxClient = dropboxClient;
	}

	/**
	 * Determines if the STOP Package is present for the specified installation.
	 * @param installId the installation's Id (note that only the 18-length string works properly
	 *                  due to the name of the SFDB file and search strategy).
	 * @return true when the STOP Package file is present in the installation's folder, false
	 *         otherwise.
	 * @throws RuntimeException when a connection error or unknown error occurs that prevents proper
	 *                          interpretation of the results.
	 */
	public boolean getExistenceByInstallationId(String installId, String installName) {
		String systemTurnOnPackageFileLocation;
		try {
			systemTurnOnPackageFileLocation = getPackagePathForInstall(installId, installName);
		}
		catch (ResourceNotFoundException e) {
			return false;
		}
		catch(SearchErrorException e) {
			throw new RuntimeException("Search failed for installation information location (id: " + installId + ")", e);
		}
		catch (DbxException e) {
			throw new RuntimeException("An unknown failure prevented finding the installation information location (id: " + installId + ")", e);
		}


		try {
			Metadata metadata = dropboxClient.files().getMetadata(systemTurnOnPackageFileLocation);
			return metadata instanceof FileMetadata;
		}
		catch (GetMetadataErrorException e) {
			GetMetadataError error = e.errorValue;
			if(error.isPath() && error.getPathValue().isNotFound())
				return false;

			throw new RuntimeException("Failed to determine existence of STOP Package (id: " + installId + ")");
		}
		catch (DbxException e) {
			throw new RuntimeException("Failed to get STOP Package information location (id: " + installId + ")");
		}
	}

	/**
	 * Retrieves the binary data for the System Turn On Package at the expected location for the
	 * installation.
	 * @param id the id of the installation
	 * @return the byte array (simple binary blob) of the installation's System Turn On Package
	 * @throws RuntimeException when an unknown issue arose attempting to get the file
	 * @throws ResourceNotFoundException when the System Turn On Package file does not exist
	 */
	public byte[] getDataByInstallationId(String id, String installName) throws RuntimeException, ResourceNotFoundException {
		String filePath;
		try {
			filePath = getPackagePathForInstall(id, installName);
		}
		catch (SearchErrorException e) {
			throw new RuntimeException("A search error occurred while looking for the file");
		}
		catch (DbxException e) {
			throw new RuntimeException("An API error occurred while looking for the file");
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			DbxDownloader<FileMetadata> fileDownloader = this.dropboxClient.files().downloadBuilder(filePath).start();
			fileDownloader.download(output);
		}
		catch (DownloadErrorException e) {
			DownloadError error = e.errorValue;

			if(error != null && error.isPath()){
				LookupError lookupError = error.getPathValue();
				if(lookupError != null && lookupError.isNotFound()) {
					throw new ResourceNotFoundException("Failed to find STOP Package");
				}
				throw new RuntimeException("Unknown error accessing data source", e);
			}

			throw new RuntimeException("Unknown error downloading file", e);
		}
		catch (DbxException e) {
			throw new RuntimeException("Unknown error transferring file", e);
		}
		catch (IOException e) {
			throw new ResourceNotFoundException("Filed to transform data");
		}

		return output.toByteArray();
	}

	private String getPackagePathForInstall(String installId, String installName) throws SearchErrorException, DbxException, ResourceNotFoundException {
		String inProgressSfdbFileFullName = findSfdbFileMatchInDropboxFolder(installId, ABSOLUTE_PATH_TO_IN_PROGRESS_INSTALLATION_FOLDER);
		if(inProgressSfdbFileFullName != null) {
			String folderName = DbxPathV2.getParent(inProgressSfdbFileFullName);
			return folderName + "/" + SYSTEM_TURN_ON_PACKAGE_FILE_NAME;
		}

		String naiveExpectedArchivedLocation = ABSOLUTE_PATH_TO_ARCHIVED_INSTALLATION_FOLDER + "/" + installName + "/" + SYSTEM_TURN_ON_PACKAGE_FILE_NAME;
		return naiveExpectedArchivedLocation;
	}

	private String findSfdbFileMatchInDropboxFolder(String installId, String baseFolder) throws SearchErrorException, DbxException {
		String expectedFileName = "." + installId + ".sfdb";
		Pattern pattern = Pattern.compile(Pattern.quote(expectedFileName));

		SearchResult searchResult = dropboxClient.files()
				.searchBuilder(baseFolder, expectedFileName)
				.withMaxResults(100L)
				.withMode(SearchMode.FILENAME)
				.start();
		List<SearchMatch> potentialMatches = searchResult.getMatches();

		for(SearchMatch potentialMatch : potentialMatches) {
			Metadata meta = potentialMatch.getMetadata();
			if(pattern.matcher(meta.getName()).matches()) {
				return meta.getPathDisplay();
			}
		}

		return null;
	}
}
