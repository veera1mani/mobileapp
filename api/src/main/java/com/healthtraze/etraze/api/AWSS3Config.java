package com.healthtraze.etraze.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.healthtraze.etraze.api.base.constant.StringIteration;

@Configuration
public class AWSS3Config {

	
	@Value("${aws.access_key_id}")
	private String accessKeyId;

	
	@Value("${aws.secret_access_key}")
	private String secretAccessKey;

	
	@Value("${aws.s3.region}")
	private String region;


	@Bean
	public AmazonS3 getAmazonS3Cient() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(StringIteration.ACCESSKEYID, StringIteration.SECRETACCESSKEY);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).build();
	}
}
