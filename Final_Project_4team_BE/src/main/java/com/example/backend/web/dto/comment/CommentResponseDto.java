package com.example.backend.web.dto.comment;

import com.example.backend.domain.comment.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 리턴 관련 모델")
public class CommentResponseDto {

    @Schema(description = "댓글 번호", example = "null", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "댓글 내용", example = "testContent", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "댓글 작성자 아이디", example = "Example User", requiredMode = Schema.RequiredMode.REQUIRED)
    private String profileId;

    @Schema(description = "댓글을 작성한 시간", example = "2024-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "댓글을 작성한 게시글 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long articleId;

    public CommentResponseDto(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.profileId = commentEntity.getUserProfileEntity().getId();
        this.createdAt = commentEntity.getCreatedAt();
        this.articleId = commentEntity.getArticleEntity().getId();
    }
}
