package com.example.backend.domain.review;

import com.example.backend.domain.user.UserProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @NotNull
    private String content;

    private LocalDateTime date;

    @NotNull
    private int score;

    // review_from_id
    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfileEntity fromUser;

    // review_to_id
    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfileEntity toUser;

    @Builder
    public ReviewEntity(String content, int score) {
        this.content = content;
        this.score = score;
    }

}
