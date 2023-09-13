package com.example.backend.web.dto.qna;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Qna 댓글 달기 dto")
public class ReqQnaCommentPutDto {
    @NotEmpty
    @Schema(description = "Qna 댓글", example = "댓글입니다.")
    private String comment;
}
