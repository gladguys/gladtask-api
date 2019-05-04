package br.com.glad.gladtask.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface S3Services {
	ByteArrayOutputStream downloadFile(String keyName);
	void uploadFile(String keyName, MultipartFile file, String folder);
	List<String> listFiles();
}
