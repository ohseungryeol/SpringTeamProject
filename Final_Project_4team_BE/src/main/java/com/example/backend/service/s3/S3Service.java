package com.example.backend.service.s3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * S3의 업로드, 파일 URL 불러오기, 파일 삭제 기능을 쉽게 사용하기 위한 interface
 * 이 인터페이스는 왠만하면 수정 X
 * */
public interface S3Service {
    String saveFile(MultipartFile file) throws IOException;
    ResponseEntity downloadImage(String originalFilename);
    void deleteImage(String originalFilename);
}
