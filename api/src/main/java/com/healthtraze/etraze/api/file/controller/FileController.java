/**
 * 
 */
package com.healthtraze.etraze.api.file.controller;

/**
 * @author mavensi
 *
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseRestController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.file.model.File;
import com.healthtraze.etraze.api.file.model.FileResponse;
import com.healthtraze.etraze.api.file.service.FileStorageService;

@RestController
public class FileController implements BaseRestController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	
	private final FileStorageService fileStorageService;
	
	private static String FILE_DIRECTORY = "C:/uploads/";

	
	
	public FileController(FileStorageService fileStorageService) {
		super();
		this.fileStorageService = fileStorageService;
	}



	@RequestMapping(value = "/file/{fileId}", headers = "content-type=multipart/*", method = RequestMethod.DELETE)
	public Result<File> delete(@PathVariable String fileId) {
		return fileStorageService.removeFile(fileId);
	}

	
	
	@RequestMapping(value = "/upload", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public Result<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		return fileStorageService.storeFileInAws(file);
	}
	
	@RequestMapping(value = "/upload-invoice", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public Result<FileResponse> uploadInvoiceFile(@RequestParam("file") MultipartFile file) {
		return fileStorageService.storeInvoiceFileInAws(file);
	}

	@RequestMapping(value = "/document/upload", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public Result<FileResponse> documentFile(@RequestParam("file") MultipartFile file,
			@RequestParam("mappingId") String mappingId, @RequestParam("description") String description) {
		return fileStorageService.storeFile(file, mappingId, description);
	}


	@GetMapping("/download/{fileId}")
	public ResponseEntity<Resource> download(@PathVariable String fileId, HttpServletRequest request) {
		Resource resource = fileStorageService.loadFileFromLocal(fileId);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileStorageService.loadFileAsResource(fileName);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info(StringIteration.COULDNOTDETERMINEFILETYPE);
		}


		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@GetMapping("/downloadFilefromAWS/{fileId}")
	public ResponseEntity<Resource> downloadFilefromAWS(@PathVariable String fileId, HttpServletRequest request) {
		return  fileStorageService.loadFileFromS3(fileId);

	}
	
	
	@RequestMapping(value = "/upload-invoice-local", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public Result<FileResponse> uploadInvoiceFileLocal(@RequestParam("file") MultipartFile file) {
		return fileStorageService.storeInvoiceFileLocal(file);
	}
	 

	    @PostMapping("/upload-local")
	    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
	        if (file.isEmpty()) {
	            return "Please select a file to upload";
	        }
	        createFolder();
	        try {
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(FILE_DIRECTORY + file.getOriginalFilename());
	            Files.write(path, bytes);
	            return "You successfully uploaded '" + file.getOriginalFilename() + "'";

	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Failed to upload '" + file.getOriginalFilename() + "'";
	        }
	    }
	    
	    public void createFolder() {
	        Path path = Paths.get(FILE_DIRECTORY);
	        try {
	            if (!Files.exists(path)) {
	                Files.createDirectory(path);
	            } else {
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @GetMapping("/get-file/{filename}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
	        try {
	            Path filePath = Paths.get(FILE_DIRECTORY).resolve(filename).normalize();
	            Resource resource = new UrlResource(filePath.toUri());

	            if (resource.exists() || resource.isReadable()) {
	                return ResponseEntity.ok()
	                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                        .body(resource);
	            } else {
	                return ResponseEntity.status(404).body(null);
	            }
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(404).body(null);
	        }
	    }
	    
//	    public () {
//	        Path dir = Paths.get("C:/exampleFolder");
//
//	        try (Stream<Path> stream = Files.list(dir)) {
//	            stream.filter(Files::isRegularFile)
//	                  .forEach(System.out::println);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
	    
	    
	
}