package com.my1stle.customer.portal.service.util;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public class MediaTypeUtil {

    private static final Tika tika = new Tika();

    /**
     * @param inputStream
     * @return media type based on provided input stream
     * @implNote uses apache's tika to guess the media type by analyzing the
     * content of the input stream
     */
    public static MediaType getMediaType(InputStream inputStream) {

        String contentType = null;
        try {
            contentType = tika.detect(inputStream);
            MimeTypes defaultMimeTypes = MimeTypes.getDefaultMimeTypes();
            return MediaType.valueOf(defaultMimeTypes.forName(contentType).getName());
        } catch (IOException | MimeTypeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * @param mediaType
     * @return file extension
     */
    public static String getExtension(MediaType mediaType) {

        MimeTypes defaultMimeTypes = MimeTypes.getDefaultMimeTypes();
        try {
            return defaultMimeTypes.forName(mediaType.toString()).getExtension();
        } catch (MimeTypeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public static MimeType getMimeType(InputStream inputStream) {

        String contentType = null;
        try {
            contentType = tika.detect(inputStream);
            MimeTypes defaultMimeTypes = MimeTypes.getDefaultMimeTypes();
            return defaultMimeTypes.forName(contentType);
        } catch (IOException | MimeTypeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

}
