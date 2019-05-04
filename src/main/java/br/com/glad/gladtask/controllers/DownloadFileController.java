package br.com.glad.gladtask.controllers;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.glad.gladtask.services.S3Services;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Download files from Amazon S3", description = "Do all kind of stuff related to download files from S3")
public class DownloadFileController {

	@Autowired S3Services s3Services;

	@ApiOperation(value = "Download a file from Amazon S3 bucket")
	@GetMapping("/api/file/{keyname}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String keyname) {
		ByteArrayOutputStream downloadInputStream = s3Services.downloadFile(keyname);

		return ResponseEntity.ok()
				.contentType(contentType(keyname))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
				.body(downloadInputStream.toByteArray());
	}

	@ApiOperation(value = "Retrieve all the filenames in the Amazon S3 bucket")
	@GetMapping("/api/file/all")
	public List<String> listAllFiles(){
		return s3Services.listFiles();
	}

	private MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length-1];
		switch(type) {
			case "txt": return MediaType.TEXT_PLAIN;
			case "png": return MediaType.IMAGE_PNG;
			case "jpg": return MediaType.IMAGE_JPEG;
			default: return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
