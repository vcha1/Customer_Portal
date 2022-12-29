package com.my1stle.customer.portal.service.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.my1stle.customer.portal.service.odoo.*;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my1stle.customer.portal.persistence.model.InstallationSalesforceObject;
import com.my1stle.customer.portal.persistence.repository.InstallationSalesforceRepository;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Service
public class DropboxImageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxImageService.class);
    //private static final Resource DEFAULT_AVATAR = new ClassPathResource("static/img/blank-profile-picture.png");

    private DbxClientV2 client;

    //@Autowired
    public DropboxImageService(DbxClientV2 client) {
        this.client = client;
    }

    //private static final String ACCESS_TOKEN = "FWR__K39HH0AAAAAAAEy0RFS-d5hSB5VZFaw3mrHsDgdGPpfCJTOsxvRRCivOJ-L";
    //DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/1le_customer_portal").build();
    //DbxClientV2 clientTest = new DbxClientV2(config, ACCESS_TOKEN);


    //public Resource getDropboxImage() {
    public String getDropboxImage(InstallationSalesforceObject install){
        //System.out.println("Here4");
        //return "https://dl.dropbox.com/s/7r8hk9hax7wrnii/web_first_images_release.png";

        //try {
            //System.out.println("Here1");
            //DbxDownloader<FileMetadata> download = this.client.files().download("https://www.dropbox.com/s/7r8hk9hax7wrnii/web_first_images_release.png?dl=0");
            //DbxDownloader<FileMetadata> download = this.client.files().download("/Test/InProgress/Karyn Warren - 1001956/Opportunity/web_first_images_release.png");
            //return new InputStreamResource(download.getInputStream());


            //***********************************************//
            //Working Code below if the file path is provided//
            //***********************************************//
            /*
            try {
                this.client.sharing().createSharedLinkWithSettings("/Test/InProgress/Karyn Warren - 1001956/Opportunity/MotleyFool-TMOT-6b0d4105-space.webp");

            }catch(DbxException e) {
                ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                        .withPath("/Test/InProgress/Karyn Warren - 1001956/Opportunity/MotleyFool-TMOT-6b0d4105-space.webp")
                        .withDirectOnly(true)
                        .start();
                System.out.println(sh);
                for (SharedLinkMetadata slm : sh.getLinks()) {
                    String sharedLink = slm.getUrl();
                    //String[] result = sharedLink.split("g");
                    String result = sharedLink.split("\\?")[0];

                    return result.replace("www","dl");
                }
            }

            System.out.println("Here2");


            ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                    .withPath("/Test/InProgress/Karyn Warren - 1001956/Opportunity/MotleyFool-TMOT-6b0d4105-space.webp")
                    .withDirectOnly(true)
                    .start();
            System.out.println(sh);
            for (SharedLinkMetadata slm : sh.getLinks()) {
                String sharedLink = slm.getUrl();
                //String[] result = sharedLink.split("g");
                String result = sharedLink.split("\\?")[0];

                return result.replace("www","dl");
            }
            return "No Image";
            */


            //******************************************************//
            //Working Code below if the file shared path is provided//
            //******************************************************//
            //return fileSharedPath;


            //**********************************************************************//
            //Working Code below if the folder shared path is provided and file name//
            //**********************************************************************//
            /*
            String fileName = "pulsar-831502910.webp";
            SharedLinkMetadata linkMetaData =  this.client.sharing().getSharedLinkMetadata("https://www.dropbox.com/sh/o1sc6uqbddudout/AADJCoas-pgwUlx7XdgEIJe0a?dl=0");
            String fullFolderPath = linkMetaData.getPathLower()+ "/" + fileName;

            try {
                this.client.sharing().createSharedLinkWithSettings(fullFolderPath);

            }catch(DbxException e) {
                ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                        .withPath(fullFolderPath)
                        .withDirectOnly(true)
                        .start();
                System.out.println(sh);
                for (SharedLinkMetadata slm : sh.getLinks()) {
                    String sharedLink = slm.getUrl();
                    //String[] result = sharedLink.split("g");
                    String result = sharedLink.split("\\?")[0];

                    return result.replace("www","dl");
                }
            }

            ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                    .withPath(fullFolderPath)
                    .withDirectOnly(true)
                    .start();
            System.out.println(sh);
            for (SharedLinkMetadata slm : sh.getLinks()) {
                String sharedLink = slm.getUrl();
                //String[] result = sharedLink.split("g");
                String result = sharedLink.split("\\?")[0];

                return result.replace("www","dl");
            }

            return "No Image";

             */

            //********************************************************************************//
            //Working Code below if the folder shared path is provided and file name for a pdf//
            //********************************************************************************//
            /*
            String fileName = "2017 Summer PD Proposal.pdf";
            SharedLinkMetadata linkMetaData =  this.client.sharing().getSharedLinkMetadata("https://www.dropbox.com/sh/o1sc6uqbddudout/AADJCoas-pgwUlx7XdgEIJe0a?dl=0");
            String fullFolderPath = linkMetaData.getPathLower()+ "/" + fileName;
            System.out.println(fullFolderPath);

            ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                    .withPath(fullFolderPath)
                    .withDirectOnly(true)
                    .start();
            System.out.println(sh);
            for (SharedLinkMetadata slm : sh.getLinks()) {
                String sharedLink = slm.getUrl();
                //String[] result = sharedLink.split("g");
                String result = sharedLink.split("\\?")[0];
                //System.out.println(result.replace("www", "dl"));
                return result + "?raw=1";
            }
            return "No Image";

             */

            //System.out.println(install.getContactId());
            //System.out.println(install.getDesignLink());

            //https://www.dropbox.com/s/jelmprtitl4o7mr/2017%20Summer%20PD%20Proposal.pdf?raw=1#page=3

            //*************************************************************************************************//
            //Working Code below if the folder shared path is provided and file name for a pdf using salesforce//
            //*************************************************************************************************//

            //String fileName = "22c58a06-c72f-4c82-90cf-66a9633c03f6.pdf";
            //SharedLinkMetadata linkMetaData =  this.client.sharing().getSharedLinkMetadata(install.getDesignLink());
            //String fullFolderPath = linkMetaData.getPathLower()+ "/" + fileName;


            //System.out.println(fullFolderPath);
            //System.out.println(install);
            //System.out.println(install.getAccountId());

            /*
            InputStream readInputStream = this.client.files().downloadBuilder(fullFolderPath).start().getInputStream();
            String resultss = new BufferedReader(new InputStreamReader( readInputStream))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println(resultss);

            byte[] array = new byte[100];
            readInputStream.read(array);
            String data = new String(array);
            System.out.println(data);
            readInputStream.close();
            */

            /*
            ListSharedLinksResult sh = client.sharing().listSharedLinksBuilder()
                    .withPath(fullFolderPath)
                    .withDirectOnly(true)
                    .start();
            for (SharedLinkMetadata slm : sh.getLinks()) {
                String sharedLink = slm.getUrl();
                String result = sharedLink.split("\\?")[0];
                result = result + "?raw=1&autoplay=1";
                return result;
            }



            return "No Image";




          } catch (DbxException e) {
            System.out.println("Here3");
            LOGGER.error(e.getMessage(), e);
            //LOGGER.error("https://www.dropbox.com/s/7r8hk9hax7wrnii/web_first_images_release.png?dl=0");
            return "No Image";
        }

        */

        /*
        //This code works in retrieving data from odoo using existing code
        System.out.println("Hello");

        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        System.out.println(odooConnection);
        System.out.println("Second Location");

        String objectType = "project.task";
        List<String> fields = Arrays.asList("name",  "x_studio_contract_type_3");
        List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_email_3", "=","hilltrucktrailer@gmail.com"));

        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

        System.out.println(results);
        System.out.println(results.get(0).get("id"));
        */

        //OdooInstallationData data = new OdooInstallationData("tdunham@1stlightenergy.com");
        //System.out.println(data.getName());

        /*
        //This code works in retrieving data from odoo
        final String url = "https://1stle-testingsandbox-6407655.dev.odoo.com",
                db = "1stle-testingsandbox-6407655",
                username = "admin",
                password = "1$tL1ght";

        final XmlRpcClient client = new XmlRpcClient();

        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));
        client.execute(common_config, "version", emptyList());

        int uid = (int)client.execute(common_config, "authenticate", asList(db, username, password, emptyMap()));

        final XmlRpcClient models = new XmlRpcClient() {{
            setConfig(new XmlRpcClientConfigImpl() {{
                setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
            }});
        }};

        System.out.println(asList((Object[])models.execute("execute_kw", asList(
                db, uid, password,
                "crm.lead", "search_read",
                asList(asList(
                        asList("name", "=", "Vong C Test 753"))),
                new HashMap() {{
                    put("fields", asList("name", "x_studio_contract_type_1"));
                    put("limit", 5);
                }}
        ))) );
        */




        return "No Image";
    }

}
