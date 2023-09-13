package com.example.backend.repository.article;

import com.example.backend.domain.article.ArticleEntity;

import java.util.List;

public interface ArticleRepoSupport {
    List<ArticleEntity> findByRange(Double[] lnglat, String range);

    List<ArticleEntity> findByKeywordAndRange(String keyword, Double[] lnglat, String range);

    List<ArticleEntity> findByKeywordAndCategory(String keyword, String category);

    List<ArticleEntity> findByCategoryAndRange(String category, Double[] lnglat, String range);

    List<ArticleEntity> findByAllCondition(String keyword, String category, Double[] lnglat, String range);
}
