package com.example.backend.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * S3Service 구현체
 * S3Interface를 이용하여 따로 임의의 구현체를 만들어도 상관 X
 * */
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service{
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일을 저장하는 함수
     * 매개변수로 입력받은 MultipartFile을 받음
     * return : 파일이 저장된 URL
     * URL을 DB에 저장한 후 이미지가 필요할 때 URL로 사용 가능
     * */
    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_"+ file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(bucket, filename, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, filename).toString();
    }

    /**
     * S3에서 파일을 다운받는 함수 (거의 쓸 일 없을 예정)
     * 매개변수 : S3(or DB)에 저장된 파일 이름 (UUID_originalFilename)
     * return : ResponseEntity<UrlResource> => 클릭 시 다운 받으려면 header값도 지정해줘야 해서 한번에 해결하기위해
     * service 단에서 처리
     * */
    @Override
    public ResponseEntity<UrlResource> downloadImage(String filename) {
        UrlResource urlResource = new UrlResource(amazonS3Client.getUrl(bucket, filename));
        String contentDisposition = "attachment; filename=\"" +  filename + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    /**
     * S3에서 파일을 삭제하는 함수
     * 매개변수 : S3(or DB)에 저장된 파일 이름 (UUID_originalFilename)
     * return : void
     * db에서는 삭제되는 것이 아니기 때문에
     * S3에서 삭제할 경우 DB에서도 이미지 URL을 지워 줄 필요가 있어 보임
     * */
    @Override
    public void deleteImage(String filename) {
        amazonS3Client.deleteObject(bucket, filename);
    }
}
