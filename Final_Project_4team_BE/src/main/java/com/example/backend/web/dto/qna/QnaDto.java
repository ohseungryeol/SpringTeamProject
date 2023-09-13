package com.example.backend.web.dto.qna;

import com.example.backend.domain.qna.QnaEntity;
import com.example.backend.domain.user.UserProfileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Qna 응답 Dto")
public class QnaDto {
    @Schema(description = "Qna Id", example = "1")
    private Long id;

//    @Schema(description = "Qna를 등록한 유저 닉네임", example = "alexgim")
//    private String nickname;

    @Schema(description = "Qna 제목", example = "제목")
    private String title;

    @Schema(description = "Qna 내용", example = "내용입니다")
    private String content;

    @Schema(description = "Qna 댓글", example = "댓글입니다.")
    private String comment;

    @Schema(description = "Qna 분류", example = "오류")
    private String category;

    @Schema(description = "Qna 내용에 있는 사진", example = "file url")
    private List<String> pic;

    @Schema(description = "Qna 생성일자", example = "시간")
    private String date;

    @Builder
    protected QnaDto(Long id, String title, String content, String comment, String category, List<String> pic, String date) {
        this.id = id;
        // this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.comment = comment;
        this.category = category;
        this.pic = pic;
        this.date = date;
    }

    public static QnaDto fromEntity(QnaEntity qna) {
        return QnaDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .category(qna.getCategory())
                .comment(qna.getComment())
                .pic(qna.getPic())
                .date(qna.getCreatedAt().toString())
                .build();
    }
}
