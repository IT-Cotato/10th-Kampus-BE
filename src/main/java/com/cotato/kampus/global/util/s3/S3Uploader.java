package com.cotato.kampus.global.util.s3;

import static com.cotato.kampus.global.util.FileUtil.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.global.error.exception.ImageException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;
	private static final String REGION = "ap-northeast-2";

	// 단일 파일 업로드
	public String uploadFile(MultipartFile multipartFile, String folderName) throws ImageException {
		log.info("{} 사진 업로드", multipartFile.getOriginalFilename());
		File localUploadFile = convert(multipartFile);

		return uploadFile(localUploadFile, folderName);
	}

	public String uploadFile(File file, String folderName) {
		String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getName();
		String uploadUrl = putS3(file, fileName);
		file.delete();

		return uploadUrl;
	}

	// 여러 파일 업로드
	public List<String> uploadFiles(List<MultipartFile> multipartFiles, String folderName) throws ImageException {
		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			String fileUrl = uploadFile(multipartFile, folderName);
			uploadedUrls.add(fileUrl);
		}
		return uploadedUrls;
	}

	public void deleteFile(String folderName, String fileName) {
		String key = folderName + "/" + fileName;

		log.info("{} 사진 삭제", key);
		s3Client.deleteObject(DeleteObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build());
	}

	private String putS3(File uploadFile, String fileName) {
		try {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(fileName)
				.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromFile(uploadFile));

			return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, REGION, fileName);
		} catch (Exception e) {
			log.error("S3 업로드 실패: {}", e.getMessage(), e);
			throw new RuntimeException("S3 업로드 실패", e);
		}
	}
}
