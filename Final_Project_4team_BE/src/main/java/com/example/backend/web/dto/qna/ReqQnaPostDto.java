package com.example.backend.web.dto.qna;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Qna 생성 요청 Dto")
public class ReqQnaPostDto {
    @Schema(description = "Qna 제목", example = "제목")
    @NotEmpty
    private String title;
    @Schema(description = "Qna 내용", example = "내용입니다.")
    @NotEmpty
    private String content;
    @Schema(description = "Qna 분류", example = "오류")
    @NotEmpty
    private String category;
    @Schema(description = "Qna 내용에 들어갈 사진", example = "file url")
    private List<MultipartFile> pic = new ArrayList<>();
}
