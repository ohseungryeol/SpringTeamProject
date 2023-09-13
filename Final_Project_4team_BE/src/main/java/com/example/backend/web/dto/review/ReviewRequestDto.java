package com.example.backend.web.dto.review;


import com.example.backend.domain.review.ReviewEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class ReviewRequestDto {


    private String content;

    private int score;

    private String toUser;

    private String fromUser;

    @Builder
    public ReviewRequestDto(String content, int score, String toUser, String fromUser) {
        this.content = content;
        this.score = score;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public ReviewEntity toEntity() {
        return ReviewEntity.builder()
                .content(content)
                .score(score)
                .build();
    }

}