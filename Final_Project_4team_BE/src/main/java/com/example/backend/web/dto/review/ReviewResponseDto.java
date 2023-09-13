package com.example.backend.web.dto.review;

import com.example.backend.domain.review.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {


    private Long id;

    private String content;

    private LocalDateTime date;

    private int score;

    private String fromUser;

    private String ToUser;

    public ReviewResponseDto(ReviewEntity review){
        id = review.getId();
        content = review.getContent();
        date = review.getDate();
        score = review.getScore();
        fromUser = review.getFromUser().getNickname();
        ToUser = review.getToUser().getNickname();
    }
}