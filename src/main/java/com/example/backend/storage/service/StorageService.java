package com.example.backend.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;

@Service
@Primary
public class StorageService {

    private final Path storageLocation;

    public StorageService(@Value("${spring.file.upload-dir}") String uploadDir) {
        this.storageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize(); // 경로 지정
        try {
            Files.createDirectories(this.storageLocation); // 디렉토리 생성
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 디렉토리 생성 실패");
        }
    }

    // 이미지 저장
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path targetLocation = this.storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/image/")
                    .path(fileName)
                    .toUriString();

        } catch (DirectoryNotEmptyException e) {
            // 아무일도 안일어남
            return null;
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 실패");
        }
    }

    // 이미지 삭제
    public void deleteFile(String fileUrl) {
        try {
            String fileName = Paths.get(new java.net.URI(fileUrl).getPath()).getFileName().toString();

            Path filePath = this.storageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException | java.net.URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 실패");
        }
    }



}
