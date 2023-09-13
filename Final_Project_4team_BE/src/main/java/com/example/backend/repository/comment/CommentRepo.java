package com.example.backend.repository.comment;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {

    // 특정 게시글(ArticleEntity)과 관련된 모든 댓글(CommentEntity)을 검색합니다.
    List<CommentEntity> findByArticleEntity(ArticleEntity articleEntity);

    // 특정 사용자 프로필(UserProfileEntity)과 관련된 모든 댓글을 시간 내림차순으로 검색합니다.
    List<CommentEntity> findByUserProfileEntityOrderByCreatedAtDesc(UserProfileEntity userProfileEntity);

    // 특정 사용자 프로필(UserProfileEntity)과 관련된 모든 댓글을 검색합니다.
    List<CommentEntity> findByUserProfileEntity(UserProfileEntity userProfileEntity);

}
