package com.example.backend.repository;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZzimRepo extends JpaRepository<ZzimEntity,ZzimEntity.ZzimId> {
    List<ZzimEntity> findByUserProfileEntityOrderByArticleEntityDesc(UserProfileEntity userProfile);

    List<ZzimEntity> findByArticleEntity(ArticleEntity article);

    void deleteByArticleEntityId(Long ArticleEntityId);
}
