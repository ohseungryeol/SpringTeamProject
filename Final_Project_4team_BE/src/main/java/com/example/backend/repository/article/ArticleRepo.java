package com.example.backend.repository.article;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.article.Category;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<ArticleEntity,Long>, ArticleRepoSupport {

    // 사용자 프로필이 있는 모든 게시글을 생성 날짜 기준으로 내림차순으로 가져옵니다.
    List<ArticleEntity> findAllByUserProfileEntityIsNotNullOrderByCreatedAtDesc();

    // 특정 사용자 프로필이 있는 게시글을 해당 사용자의 프로필과 생성 날짜 기준으로 내림차순으로 가져옵니다.
    List<ArticleEntity> findByUserProfileEntityIsNotNullAndUserProfileEntityOrderByCreatedAtDesc(UserProfileEntity userProfileEntity);

    // 특정 카테고리에 속한 게시글 중 사용자 프로필이 있는 것들을 생성 날짜 기준으로 내림차순으로 가져옵니다.
    List<ArticleEntity> findByUserProfileEntityIsNotNullAndCategoryOrderByCreatedAtDesc(Category category);

    // 제목이나 내용에 특정 키워드가 포함된 게시글 중에서 사용자 프로필이 있는 것들을 생성 날짜 기준으로 내림차순으로 가져옵니다.
    List<ArticleEntity> findByUserProfileEntityIsNotNullAndTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content);

    // 특정 파티(Party) 엔티티와 연결된 게시글을 가져옵니다.
    ArticleEntity findByPartyEntity(PartyEntity partyEntity);
}