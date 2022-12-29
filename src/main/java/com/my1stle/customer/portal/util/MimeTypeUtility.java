package com.my1stle.customer.portal.util;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.IOException;
import java.io.InputStream;

public class MimeTypeUtility {

    private static final Tika tika = new Tika();

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
