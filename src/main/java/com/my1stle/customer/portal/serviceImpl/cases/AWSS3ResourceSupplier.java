package com.my1stle.customer.portal.serviceImpl.cases;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URI;
import java.util.function.Supplier;

class AWSS3ResourceSupplier implements Supplier<Resource> {

    private final AmazonS3 amazonS3;
    private final URI uri;

    /**
     * @param amazonS3 client with credentials
     * @param uri      must be an amazon s3 object
     */
    AWSS3ResourceSupplier(AmazonS3 amazonS3, URI uri) {
        this.amazonS3 = amazonS3;
        this.uri = uri;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public Resource get() {
        AmazonS3URI s3URI = new AmazonS3URI(this.uri);
        S3Object object = amazonS3.getObject(s3URI.getBucket(), s3URI.getKey());
        S3ObjectInputStream objectContent = object.getObjectContent();
        return convert(objectContent);
    }

    private Resource convert(S3ObjectInputStream objectContent) {
        try {
            return new ByteArrayResource(IOUtils.toByteArray(objectContent));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
