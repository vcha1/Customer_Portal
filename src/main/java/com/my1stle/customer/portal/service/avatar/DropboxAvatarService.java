package com.my1stle.customer.portal.service.avatar;

import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;

@Service
public class DropboxAvatarService implements AvatarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxAvatarService.class);
    private static final Resource DEFAULT_AVATAR = new ClassPathResource("static/img/blank-profile-picture.png");
    private static final String AVATARS_PATH = "/DEV_DATA/CUSTOMER_PORTAL/AVATARS";

    private DbxClientV2 client;
    private UserRepository userRepository;

    @Autowired
    public DropboxAvatarService(DbxClientV2 client, UserRepository userRepository) {
        this.client = client;
        this.userRepository = userRepository;
    }

    @Override
    public Resource getAvatar(User user) {

        if (StringUtils.isBlank(user.getAvatarUrlPath())) {
            return DEFAULT_AVATAR;
        }

        try {
            DbxDownloader<FileMetadata> download = this.client.files().download(user.getAvatarUrlPath());
            return new InputStreamResource(download.getInputStream());
        } catch (DbxException e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error(user.getAvatarUrlPath());
            return DEFAULT_AVATAR;
        }

    }

    @Override
    @Transactional
    public Resource setAvatar(User user, InputStream inputStream) {

        try {

            BufferedInputStream buffer = new BufferedInputStream(inputStream);
            TikaConfig config = TikaConfig.getDefaultConfig();
            MediaType mediaType = config.getMimeRepository().detect(buffer, new Metadata());
            MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
            String extension = mimeType.getExtension();

            FileMetadata metadata = this.client
                    .files()
                    .uploadBuilder(String.format("%s/%s%s", AVATARS_PATH, user.getEmail(), extension))
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(buffer);

            user.setAvatarUrlPath(metadata.getPathDisplay());
            user.setProvidedIdentification(ZonedDateTime.now());
            userRepository.save(user);

            return new InputStreamResource(buffer);

        } catch (DbxException | IOException | MimeTypeException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }

    }

}