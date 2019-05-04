package br.com.glad.gladtask.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.com.glad.gladtask.services.S3Services;

@Service
public class S3ServicesImpl implements S3Services {

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	@Override
	public ByteArrayOutputStream downloadFile(String keyName) {
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));

		try {
			InputStream is = s3object.getObjectContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void uploadFile(String keyName, MultipartFile file, String folder) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		try {
			s3client.putObject(bucketName, folder + "/" + keyName, file.getInputStream(), metadata);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> listFiles() {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
		List<String> keys = new ArrayList<>();

		ObjectListing objects = s3client.listObjects(listObjectsRequest);

		while (true) {
			List<S3ObjectSummary> summaries = objects.getObjectSummaries();
			if (summaries.size() < 1) {
				break;
			}

			for (S3ObjectSummary item : summaries) {
				if (!item.getKey().endsWith("/"))
					keys.add(item.getKey());
			}

			objects = s3client.listNextBatchOfObjects(objects);
		}

		return keys;
	}
}