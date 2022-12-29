package com.my1stle.customer.portal.service.avatar;

import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Unit Test for {@link DropboxAvatarService}
 */
@RunWith(MockitoJUnitRunner.class)
public class DropboxAvatarServiceTest {

    private AvatarService avatarService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DbxClientV2 mockDbxClientV2;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private DbxDownloader<FileMetadata> mockDownload;

    @Mock
    private FileMetadata mockFileMetaData;

    @Before
    public void setUp() throws Exception {

        this.avatarService = new DropboxAvatarService(mockDbxClientV2, mockUserRepository);

    }

    @Test
    public void shouldGetDefaultAvatar() throws DbxException {

        // Given
        User user = new User();
        user.setAvatarUrlPath(null);

        Resource expectedResource = new ClassPathResource("static/img/blank-profile-picture.png");

        // When
        Resource avatar = avatarService.getAvatar(user);

        // Then
        verify(mockDbxClientV2.files(), times(0)).download(anyString());
        verify(mockDownload, times(0)).getInputStream();
        assertEquals(expectedResource, avatar);


    }

    @Test
    public void shouldGetAvatarFromDropbox() throws DbxException, IOException {

        // Given
        User user = new User();
        user.setAvatarUrlPath("/dropbox/path");

        Resource stubResource = new ClassPathResource("static/img/blank-profile-picture.png");

        when(mockDbxClientV2.files().download(eq(user.getAvatarUrlPath()))).thenReturn(mockDownload);
        when(mockDownload.getInputStream()).thenReturn(stubResource.getInputStream());

        // When
        Resource avatar = avatarService.getAvatar(user);

        // Then
        verify(mockDbxClientV2.files(), times(1)).download(eq(user.getAvatarUrlPath()));
        verify(mockDownload, times(1)).getInputStream();
        assertEquals(stubResource.contentLength(), avatar.contentLength()); // TODO find a better way that they are the same resource


    }

    @Test
    public void shouldReturnDefaultAvatarWhenDropboxExceptionIsThrown() throws DbxException, IOException {

        // Given
        User user = new User();
        user.setAvatarUrlPath("/dropbox/path");

        Resource stubResource = new ClassPathResource("static/img/blank-profile-picture.png");

        when(mockDbxClientV2.files().download(eq(user.getAvatarUrlPath()))).thenThrow(new DbxException("testing only"));

        // When
        Resource avatar = avatarService.getAvatar(user);

        // Then
        verify(mockDbxClientV2.files(), times(1)).download(eq(user.getAvatarUrlPath()));
        verify(mockDownload, times(0)).getInputStream();
        assertEquals(stubResource, avatar);

    }

    @Test
    public void shouldSetAvatar() throws DbxException, IOException {

        // Given
        User user = new User();
        user.setId(1869L);
        user.setEmail("development@1stle.com");
        String dropboxPath = "/dropbox/path";
        Resource stubResource = new ClassPathResource("static/img/blank-profile-picture.png");

        when(mockDbxClientV2
                .files()
                .uploadBuilder(anyString())
                .withMode(eq(WriteMode.OVERWRITE)).uploadAndFinish(any(BufferedInputStream.class)))
                .thenReturn(mockFileMetaData);
        when(mockFileMetaData.getPathDisplay()).thenReturn(dropboxPath);

        // When
        Resource avatar = avatarService.setAvatar(user, stubResource.getInputStream());

        // Then
        verify(mockUserRepository, times(1)).save(eq(user));
        assertEquals(stubResource.contentLength(), avatar.contentLength()); // TODO find a better way that they are the same resource
        assertEquals(user.getAvatarUrlPath(), dropboxPath);
        assertNotNull(user.getOnboarding().getProvidedIdentificationDateTime());

    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrownAnInternalServerException() throws DbxException, IOException {

        // Given
        User user = new User();
        user.setId(1869L);
        user.setEmail("development@1stle.com");
        String dropboxPath = "/dropbox/path";
        Resource stubResource = new ClassPathResource("static/img/blank-profile-picture.png");

        when(mockDbxClientV2
                .files()
                .uploadBuilder(anyString())
                .withMode(eq(WriteMode.OVERWRITE)).uploadAndFinish(any(BufferedInputStream.class)))
                .thenThrow(new DbxException("Unable to Upload File!"));

        // When
        Resource avatar = avatarService.setAvatar(user, stubResource.getInputStream());

        fail("Expected an Exception to be thrown");

    }

}