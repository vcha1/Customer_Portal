package com.my1stle.customer.portal.service.installation.configuration.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfiguration {

    @Bean
    public BasicAWSCredentials awsS3Credentials(
            @Value("${aws.s3.access.key}") String accessKey,
            @Value("${aws.s3.secret.key}")
                    String secretKey) {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    @Primary
    @Autowired
    public AmazonS3 amazonS3USWest1(AWSCredentials awsS3Credentials) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsS3Credentials))
                .withRegion(Regions.US_WEST_1)
                .build();

    }

}