/**
 * 
 */
package com.healthtraze.etraze.api.file.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author mavensi
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.util.AWSUtil;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.file.model.File;
import com.healthtraze.etraze.api.file.model.FileResponse;
import com.healthtraze.etraze.api.file.repository.FileRepository;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;


/**
 * 
 * 
 * 
 * @author mavensi
 *
 */

@Service
public class FileStorageService {

	private Path fileStorageLocation;
	private Logger logger = LogManager.getLogger(FileStorageService.class);

	@Value("${aws.s3.region}")
	private String region;

	@Value("${aws.s3.bucket}")
	private String bucketName;
	
	private final AmazonS3 amazonS3;

	private final FileRepository fileRepository;

	private final UserRepository userRepository;

	private final TenantRepository tenantRepository;
	
	@Autowired
	public FileStorageService(AmazonS3 amazonS3, FileRepository fileRepository, UserRepository userRepository,
			TenantRepository tenantRepository) {
		this.amazonS3 = amazonS3;
		this.fileRepository = fileRepository;
		this.userRepository = userRepository;
		this.tenantRepository = tenantRepository;
	}

	/**
	 * 
	 * 
	 * 
	 * @param file
	 * @return
	 */
	public Result<FileResponse> storeFile(MultipartFile file, String mappingId, String description) {

		Result<FileResponse> result = new Result<>();

		FileResponse fileResponse = new FileResponse();
		try {
			if (file == null) {
				result.setCode("1111");
				result.setMessage("File should not be empty");
				return result;
			}

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			if (this.fileStorageLocation == null) {
				this.fileStorageLocation = Paths.get(ConfigUtil.getPath()).toAbsolutePath().normalize();
				Files.createDirectories(this.fileStorageLocation);
			}

			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String fileId = UUID.randomUUID().toString();

			File f = new File();
			f.setFileId(fileId);
			f.setFileName(StringUtils.cleanPath(fileName));
			f.setUrl(ConfigUtil.getAppLink() + "/rest/api/v1/download/" + fileId);

			String[] s = fileName.split("[.]");
			if (s != null && s.length > 0) {
				String type = s[s.length - 1];
				f.setFileType(type);
			}
			
			f.setDescription(description);
			f.setMappingId(mappingId);
			CommonUtil.setCreatedOn(f);
			fileRepository.save(f);

			result.setCode("0000");
			result.setMessage("Successfully  Uploaded");
			result.setData(fileResponse);

		} catch (IOException ex) {
			result.setCode("1111");
			result.setMessage("Error while uploading  in S3");
			logger.error("", ex);
		}
		return result;
	}

	public Result<FileResponse> storeFileInAws1(MultipartFile multipartFile) {

		Result<FileResponse> result = new Result<>();
		Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
		FileResponse fileResponse = new FileResponse();
		try {

			String fileId = UUID.randomUUID().toString();
			ObjectMetadata data = new ObjectMetadata();
			data.setContentType(multipartFile.getContentType());
			data.setContentLength(multipartFile.getSize());	
			String key = fileId;
			if (user != null) {
				key = user.get().getUserId() + "/" + key;
			}
			File file=uploadFileToAWS(fileId,key,multipartFile.getInputStream(),multipartFile.getOriginalFilename(),  multipartFile.getContentType(),multipartFile.getSize());
			fileResponse.setUrl(file.getUrl());
			fileResponse.setFileId(fileId);
			result.setCode("0000");
			result.setMessage("Successfully Uploaded");
			result.setData(fileResponse);
		} catch (Exception ex) {
			result.setCode("1111");
			result.setMessage(StringIteration.ERRORWHILEUPLOADINGIMAGEINS3);
			logger.error("", ex);
		}
		return result;

	}

	public File uploadFileToAWS(String fileId, String key, InputStream inputStream,String  originalFilename ,String contentType, long size) {

		ObjectMetadata data = new ObjectMetadata();
		data.setContentType(contentType);
		if(size>0) {
			data.setContentLength(size);

		}
		
		PutObjectRequest p = new PutObjectRequest(ConfigUtil.getS3Bucket(), key, inputStream,
				data).withCannedAcl(CannedAccessControlList.PublicRead);
		AWSUtil.getAWSS3Clint().putObject(p);

		File file = new File();
		file.setFileId(fileId);
		file.setKey(key);
		file.setFileName(StringUtils.cleanPath(originalFilename));
		file.setUrl("https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key);
		fileRepository.save(file);
		return file;
	}
	
	public Result<FileResponse> storeInvoiceFileInAws1(MultipartFile multipartFile) {
		Result<FileResponse> result = new Result<>();
	    Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
	    FileResponse fileResponse = new FileResponse();
	    try {
	        String fileId = UUID.randomUUID().toString();
	        ObjectMetadata data = new ObjectMetadata();
	        data.setContentType(multipartFile.getContentType());
	        data.setContentLength(multipartFile.getSize());
	        String key = fileId;

	        if (user.isPresent()) {
	            User currentUser = user.get();
	            Optional<Tenant> tenant= tenantRepository.findById(currentUser.getTenantId());
	            if(tenant.isPresent()) {
	            	Tenant t = tenant.get();
		            String tenantFolderKey = "TemporaryInvoiceForTenants/" + t.getTenantCode() + "_" + t.getTenantName();
		            if (checkFolderExists(tenantFolderKey)) {
		            	String tenantFolderKey1 = "TemporaryInvoiceForTenants/" + t.getTenantCode() + "_" + t.getTenantName() + "/";
		                key = tenantFolderKey1 + multipartFile.getOriginalFilename();
		            } else {
		                logger.error("Tenant folder '{}' does not exist or does not match user's tenant code", tenantFolderKey);
		                result.setCode("1111");
		                result.setMessage("Error while uploading image in S3");
		                return result;
		            }
	            }else {
	            	result.setCode(StringIteration.ERROR_CODE10);
	            	result.setMessage("invalid tenant");
	            	return result;
	            }
	        }

	        File file = uploadInvoiceFileToAWS(fileId, key, multipartFile.getInputStream(),
	                multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize());

	        fileResponse.setUrl(file.getUrl());
	        fileResponse.setFileId(fileId);
	        fileResponse.setName(key);
	        result.setCode("0000");
	        result.setMessage("Successfully Uploaded");
	        result.setData(fileResponse);
	    } catch (Exception ex) {
	        result.setCode("1111");
	        result.setMessage("Error while uploading image in S3");
	        logger.error("", ex);
	    }
	    return result;
	}
	
	/*
	 * public Result<FileResponse> storeInvoiceFileInAws1Source(MultipartFile
	 * multipartFile) { Result<FileResponse> result = new Result<>(); Optional<User>
	 * user = userRepository.findById(SecurityUtil.getUserName()); FileResponse
	 * fileResponse = new FileResponse(); try { String fileId =
	 * UUID.randomUUID().toString(); ObjectMetadata data = new ObjectMetadata();
	 * data.setContentType(multipartFile.getContentType());
	 * data.setContentLength(multipartFile.getSize()); String key = fileId;
	 * 
	 * if (user.isPresent()) { User currentUser = user.get(); Optional<Tenant>
	 * tenant= tenantRepository.findById(currentUser.getTenantId());
	 * if(tenant.isPresent()) { Tenant t = tenant.get(); String tenantFolderKey =
	 * "SourceFolderForTenants/" + t.getTenantCode() + "_" + t.getTenantName(); if
	 * (checkFolderExists(tenantFolderKey)) { String tenantFolderKey1 =
	 * "SourceFolderForTenants/" + t.getTenantCode() + "_" + t.getTenantName() +
	 * "/"; key = tenantFolderKey1 + multipartFile.getOriginalFilename(); } else {
	 * logger.
	 * error("Tenant folder '{}' does not exist or does not match user's tenant code"
	 * , tenantFolderKey); result.setCode("1111");
	 * result.setMessage("Error while uploading image in S3"); return result; }
	 * }else { result.setCode(StringIteration.ERROR_CODE2);
	 * result.setMessage("invalid tenant"); return result; } }
	 * 
	 * File file = uploadInvoiceFileToAWS(fileId, key,
	 * multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
	 * multipartFile.getContentType(), multipartFile.getSize());
	 * 
	 * fileResponse.setUrl(file.getUrl()); fileResponse.setFileId(fileId);
	 * fileResponse.setName(key); result.setCode("0000");
	 * result.setMessage("Successfully Uploaded"); result.setData(fileResponse); }
	 * catch (Exception ex) { result.setCode("1111");
	 * result.setMessage("Error while uploading image in S3"); logger.error("", ex);
	 * } return result; }
	 */
	
	private static String FILE_DIRECTORY = "C:/uploads/";
	
	
	public Result<FileResponse> storeManufactureReportAws(MultipartFile multipartFile,Integer month) {
		Result<FileResponse> result = new Result<>();
	    Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
	    FileResponse fileResponse = new FileResponse();
	    try {
	        String fileId = UUID.randomUUID().toString();
	        ObjectMetadata data = new ObjectMetadata();
	        data.setContentType(multipartFile.getContentType());
	        data.setContentLength(multipartFile.getSize());
	        String key = fileId;
	      
	        if (user.isPresent()) {
	            User currentUser = user.get();
	            Optional<Tenant> tenant= tenantRepository.findById(currentUser.getTenantId());
	            if(tenant.isPresent()) {
	            	Tenant t = tenant.get();
	            	int year=LocalDateTime.now().getYear();
	            	String months = Month.of(month).toString();
                      String tenantFolderKey1 = "manufactureReport/" + t.getTenantCode() + "_" + t.getTenantName();
		                key = tenantFolderKey1 +"/"+year+"/"+months+"/"+multipartFile.getOriginalFilename();
	            }else {
	            	result.setCode(StringIteration.ERROR_CODE10);
	            	result.setMessage("invalid tenant");
	            	return result;
	            }
	        }

	        File file = uploadInvoiceFileToAWS(fileId, key, multipartFile.getInputStream(),
	                multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize());

	        fileResponse.setUrl(file.getUrl());
	        fileResponse.setFileId(fileId);
	        fileResponse.setName(key);
	        result.setCode("0000");
	        result.setMessage("Successfully Uploaded");
	        result.setData(fileResponse);
	    } catch (Exception ex) {
	        result.setCode("1111");
	        result.setMessage("Error while uploading image in S3");
	        logger.error("", ex);
	    }
	    return result;
	}
	

	
	
	public Result<FileResponse> storeInvoiceFileLocal(MultipartFile file) {
		Result<FileResponse> result = new Result<>();
	    Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
	    try {
	        if (user.isPresent()) {
	            User currentUser = user.get();
	            Optional<Tenant> tenant= tenantRepository.findById(currentUser.getTenantId());
	            	String tenantFolderKey1 = FILE_DIRECTORY+"SourceFolderForTenants/" + tenant.get().getTenantCode() + "_" + tenant.get().getTenantName() + "/";
	               String key = tenantFolderKey1 + file.getOriginalFilename();
	                
	                
	                byte[] bytes = file.getBytes();
		            Path path = Paths.get(key);
		            Files.write(path, bytes);
		            result.setCode("0000");
			        result.setMessage("Successfully Uploaded");
	        }
	        
	    } catch (Exception ex) {
	        result.setCode("1111");
	        result.setMessage("Error while uploading image in S3");
	        logger.error("", ex);
	    }
	    return result;
	}
	
	private boolean checkFolderExists(String folderKey) {
	    ListObjectsV2Request listRequest = new ListObjectsV2Request()
	            .withBucketName(ConfigUtil.getS3Bucket())
	            .withPrefix(folderKey)
	            .withDelimiter("/");
	    ListObjectsV2Result listResponse = AWSUtil.getAWSS3Clint().listObjectsV2(listRequest);
	    return !listResponse.getCommonPrefixes().isEmpty();
	}

	public File uploadInvoiceFileToAWS(String fileId, String key, InputStream inputStream,String  originalFilename ,String contentType, long size) {

		ObjectMetadata data = new ObjectMetadata();
		data.setContentType(contentType);
		if(size>0) {
			data.setContentLength(size);

		}
		
		PutObjectRequest p = new PutObjectRequest(ConfigUtil.getS3Bucket(), key, inputStream,
				data).withCannedAcl(CannedAccessControlList.PublicRead);
		AWSUtil.getAWSS3Clint().putObject(p);

		File file = new File();
		file.setFileId(fileId);
		file.setKey(key);
		file.setFileName(StringUtils.cleanPath(originalFilename));
		file.setUrl("https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key);
		fileRepository.save(file);
		return file;
	}

	/**
	 * 
	 * 
	 * 
	 * @param file
	 * @return
	 */
	public Result<FileResponse> storeFileInAws(MultipartFile multipartFile) {
		return storeFileInAws1(multipartFile);
	}
	
	public Result<FileResponse> storeInvoiceFileInAws(MultipartFile multipartFile) {
		return storeInvoiceFileInAws1(multipartFile);
	}
	
	/*
	 * public Result<FileResponse> storeInvoiceFileInAwsToSource(MultipartFile
	 * multipartFile) { return storeInvoiceFileInAws1Source(multipartFile); }
	 */
	
	public Result<File> removeFile(String fileId){
		Result<File> result = new Result<>();
		try {
			fileRepository.deleteById(fileId);
			result.setCode("0000");
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}
	

	/**
	 * 
	 * 
	 * @param fileName
	 * @return
	 */
	public String deleteFileFromBucket(String fileName) {

		amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));

		return "Deletion Successful";
	}

	/**
	 * 
	 * 
	 * 
	 * @param fileName
	 * @return
	 */
	public Resource loadFileAsResource(String fileName) {
		try {
			if (this.fileStorageLocation == null) {
				this.fileStorageLocation = Paths.get(ConfigUtil.getPath()).toAbsolutePath().normalize();
				Files.createDirectories(this.fileStorageLocation);
			}
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found" + fileName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<Resource> loadFileFromS3(String fileId) {
		try {
			Optional<File> option = fileRepository.findById(fileId);
			if (option.isPresent()) {
				File f = option.get();
				S3Object s3object = AWSUtil.getAWSS3Clint().getObject(ConfigUtil.getS3Bucket(), f.getKey());
				S3ObjectInputStream inputStream = s3object.getObjectContent();
				
				

				return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.getFileName() + "\"")
						.body( new InputStreamResource(inputStream));
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public Resource loadFileFromLocal(String fileId) {
		try {
			Optional<File> option = fileRepository.findById(fileId);
			if (option.isPresent()) {
				File f = option.get();
				
				FileInputStream inputStream = new FileInputStream(new java.io.File(ConfigUtil.getPath()+java.io.File.pathSeparator+f.getFileName()));
				
				return new InputStreamResource(inputStream);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param fileName
	 * @param fileStorageLocation
	 * @return
	 */
	public Resource loadFileAsResource(String fileName, Path fileStorageLocation) {
		try {
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}