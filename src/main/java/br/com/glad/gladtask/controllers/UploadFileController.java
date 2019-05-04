package br.com.glad.gladtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.glad.gladtask.services.S3Services;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "File Upload Controller", description = "Perform operations regard file upload to Amazon S3")
public class UploadFileController {

	@Autowired S3Services s3Services;

	@ApiOperation(value = "Upload a given multipart file to the Amazon S3 bucket configured")
	@PostMapping("/api/file/upload")
	public String uploadMultipartFile(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
		String keyName = file.getOriginalFilename();
		s3Services.uploadFile(keyName, file, folder);
		return "Upload Successfully -> KeyName = " + keyName;
	}
}
