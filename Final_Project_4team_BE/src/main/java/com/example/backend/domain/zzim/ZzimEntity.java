package com.example.backend.domain.zzim;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.user.UserProfileEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZzimEntity {
    @EmbeddedId
    private ZzimId zzimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    private UserProfileEntity userProfileEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    private ArticleEntity articleEntity;

    public ZzimEntity(UserProfileEntity userProfile, ArticleEntity article) {
        this.userProfileEntity = userProfile;
        this.articleEntity = article;
        this.zzimId = new ZzimId(userProfile.getId(),article.getId());
    }

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZzimId implements Serializable {
        private String profileId;
        private Long articleId;
    }

}
