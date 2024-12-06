package com.healthtraze.etraze.api.base.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AWSUtil {

	public static AmazonS3 getAWSS3Clint() {
		AWSCredentials credentials = new BasicAWSCredentials(ConfigUtil.getS3AccessKey(), ConfigUtil.getS3SecretKey());
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(ConfigUtil.getS3Region())
				.build();
	}
}
