package com.healthtraze.etraze.api.base.util;

import javax.mail.Session;

public class ConfigUtil {
	ConfigUtil utils=new ConfigUtil();
	
	private static Integer sessionTimeout;
	private static String fromEmail;
	private static String path;
	private static String sleepTime;
	private static String authKey;
	private static String clientUrl;
	private static String sqlQuery;
	private static String s3Path;
	private static String s3AccessKey;
	private static String s3SecretKey;
	private static String s3Bucket;
	private static String s3Region;
	private static Session session;
	
	private static String foldername;
	
	
	

	private static String adminRoleId;
	private static String superAdminRoleId;
	private static String assetManagerRoleId;
	private static String managerRoleId;
	private static String employeRoleId;
	

	
	private static int emailRetryCount;
	private static String AppPort; 
	private static String uri;
	
	
	
	
	
	
	
	public static void setFromEmail(String fromEmail) {
		ConfigUtil.fromEmail = fromEmail;
	}
	public static String getUri() {
		return uri;
	}
	public static void setUri(String uri) {
		ConfigUtil.uri = uri;
	}
	public static String getSuperAdminRoleId() {
		return superAdminRoleId;
	}
	public static void setSuperAdminRoleId(String superAdminRoleId) {
		ConfigUtil.superAdminRoleId = superAdminRoleId;
	}
	public static String getAssetManagerRoleId() {
		return assetManagerRoleId;
	}
	public static void setAssetManagerRoleId(String assetManagerRoleId) {
		ConfigUtil.assetManagerRoleId = assetManagerRoleId;
	}
	public static String getManagerRoleId() {
		return managerRoleId;
	}
	public static void setManagerRoleId(String managerRoleId) {
		ConfigUtil.managerRoleId = managerRoleId;
	}
	public static String getEmployesRoleId() {
		return employeRoleId;
	}
	public static void setEmployesRoleId(String employesRoleId) {
		ConfigUtil.employeRoleId = employesRoleId;
	}
	public static String getAppPort() {
		return AppPort;
	}
	public static void setAppPort(String appPort) {
		AppPort = appPort;
	}
	public ConfigUtil getUtils() {
		return utils;
	}
	public void setUtils(ConfigUtil utils) {
		this.utils = utils;
	}
	public static int getEmailRetryCount() {
		return emailRetryCount;
	}
	public static void setEmailRetryCount(int emailRetryCount) {
		ConfigUtil.emailRetryCount = emailRetryCount;
	}
	public static Session getSession() {
		return session;
	}
	public static void setSession(Session session) {
		ConfigUtil.session = session;
	}
	private static String appLink;
	
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		ConfigUtil.path = path;
	}
	
	public static String getSleepTime() {
		return sleepTime;
	}
	public static void setSleepTime(String sleepTime) {
		ConfigUtil.sleepTime = sleepTime;
	}
	public static String getAuthKey() {
		return authKey;
	}
	public static void setAuthKey(String authKey) {
		ConfigUtil.authKey = authKey;
	}
	public static String getClientUrl() {
		return clientUrl;
	}
	public static void setClientUrl(String clientUrl) {
		ConfigUtil.clientUrl = clientUrl;
	}
	public static String getSqlQuery() {
		return sqlQuery;
	}
	public static void setSqlQuery(String sqlQuery) {
		ConfigUtil.sqlQuery = sqlQuery;
	}
	public static String getS3Path() {
		return s3Path;
	}
	public static void setS3Path(String s3Path) {
		ConfigUtil.s3Path = s3Path;
	}
	public static String getS3AccessKey() {
		return s3AccessKey;
	}
	public static void setS3AccessKey(String s3AccessKey) {
		ConfigUtil.s3AccessKey = s3AccessKey;
	}
	public static String getS3SecretKey() {
		return s3SecretKey;
	}
	public static void setS3SecretKey(String s3SecretKey) {
		ConfigUtil.s3SecretKey = s3SecretKey;
	}
	public static String getS3Bucket() {
		return s3Bucket;
	}
	public static void setS3Bucket(String s3Bucket) {
		ConfigUtil.s3Bucket = s3Bucket;
	}
	public static String getFromEmail() {
		return fromEmail;
	}
	public static void f(String fromEmail) {
		ConfigUtil.fromEmail = fromEmail;
	}
	public static String getAppLink() {
		return appLink;
	}
	public static void setAppLink(String appLink) {
		ConfigUtil.appLink = appLink;
	}

	public static String getAdminRoleId() {
		return adminRoleId;
	}
	public static void setAdminRoleId(String adminRoleId) {
		ConfigUtil.adminRoleId = adminRoleId;
	}
	public static String getFoldername() {
		return foldername;
	}
	public static void setFoldername(String foldername) {
		ConfigUtil.foldername = foldername;
	}
	public static String getS3Region() {
		return s3Region;
	}
	public static void setS3Region(String s3Region) {
		ConfigUtil.s3Region = s3Region;
	}
	public static Integer getSessionTimeout() {
		return sessionTimeout;
	}
	public static void setSessionTimeout(Integer sessionTimeout) {
		ConfigUtil.sessionTimeout = sessionTimeout;
	}
	
	
	
	


	
	
	
	
	
	
}