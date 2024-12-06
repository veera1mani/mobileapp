package com.healthtraze.etraze.api;
 
import java.util.Collections;
import java.util.Formatter;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.service.DatabaseService;
import com.healthtraze.etraze.api.base.util.ConfigUtil;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@SpringBootApplication
@ComponentScan({"com.healthtraze.etraze.api" , "com.healthtraze.etraze.api.security.service"})
@EnableSwagger2
@EnableScheduling
public class ApiApplication implements CommandLineRunner{
	private final Environment env;
	@Autowired
	public ApiApplication(Environment env) {
		this.env=env;
	}
	private static Logger logger = LogManager.getLogger(ApiApplication.class);
 
	

 
	private Properties loadMailConfig() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.port", "993");
		props.setProperty(Constants.SMTP_USERNAME, env.getProperty(Constants.SMTP_USERNAME));
		props.setProperty(Constants.SMTPMAIL,env.getProperty(Constants.SMTPMAIL));
		props.setProperty("mail.smtp.ssl.enable", Constants.TRUE);
		props.setProperty("mail.smtp.auth", Constants.TRUE);
		props.setProperty("mail.smtp.starttls.enable", Constants.TRUE);
		return props;
	}
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
//	@Bean
//    public CommandLineRunner run(DatabaseService databaseService) {
//        return args -> {
//            // This code will run after the application starts
//            System.out.println("Application is running. ");
//            // This keeps the main thread alive indefinitely
//            try {
//                Thread.sleep(Long.MAX_VALUE);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//    }
//	
	
	
	@Override
	public void run(String... args) throws Exception {
		ConfigUtil.setPath(env.getProperty("config.path"));
		ConfigUtil.setAppLink(env.getProperty("app.url.origin"));
		ConfigUtil.setFromEmail(env.getProperty("email.from")); 
		ConfigUtil.setAdminRoleId(env.getProperty("role.admin"));
		ConfigUtil.setAssetManagerRoleId(env.getProperty("role.assetmanager"));
		ConfigUtil.setEmployesRoleId(env.getProperty("role.employe"));
		ConfigUtil.setManagerRoleId(env.getProperty("role.manager"));
		ConfigUtil.setSuperAdminRoleId(env.getProperty("role.superadmin"));
		ConfigUtil.setSessionTimeout(Integer.valueOf(env.getProperty("user.session.timeout")));
		ConfigUtil.setAppPort(env.getProperty("server.port"));
		ConfigUtil.setS3AccessKey(env.getProperty("aws.access_key_id"));
		ConfigUtil.setS3Bucket(env.getProperty("aws.s3.bucket"));
		ConfigUtil.setS3SecretKey(env.getProperty("aws.secret_access_key"));
		ConfigUtil.setS3Region(env.getProperty("aws.s3.region"));
		ConfigUtil.setUri(env.getProperty("spring.data.mongodb.uri"));
		ConfigUtil.setEmailRetryCount(Integer.valueOf(env.getProperty("email.retry")));
		String configPath = env.getProperty("config.path");
		if (configPath != null) {
		    logger.info("Config path: {}", configPath);
		} else {
		    logger.warn("Config path is not defined");
		}
		Session session = Session.getInstance(loadMailConfig(), new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(env.getProperty("mail.smtp.username"), env.getProperty(Constants.SMTPMAIL));
			}
		});
		logger.info(session);
		ConfigUtil.setSession(session);
		Formatter formatter =new Formatter();	
		formatter.format("Admin - %s", ConfigUtil.getAdminRoleId());
		formatter.format("SuperAdmin - %s",ConfigUtil.getSuperAdminRoleId());
 
		formatter.format("AssetManager - %s",ConfigUtil.getAssetManagerRoleId());
 
		formatter.format("Manager - %s",ConfigUtil.getManagerRoleId());
 
		formatter.format("Employe - %s",ConfigUtil.getEmployesRoleId());
 
		

		logger.info("Session Timeout -> {}",ConfigUtil.getSessionTimeout());
		logger.info("Email Retry -> {}",ConfigUtil.getEmailRetryCount());
		logger.info(formatter);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.welldercare.api")).paths(PathSelectors.any()).build()
				.apiInfo(customInfo());
	}
	
	
		
	private ApiInfo customInfo() {
 
		return new ApiInfo("Users API", "CodeFiction Project", "2.0", "Terms of service",
				new Contact("Gabriel Pulga", "www.codefiction.net", "gabrieelplg@gmail.com"), "API license",
				"API license URL", Collections.emptyList());
	}
 
}