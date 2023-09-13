package com.example.backend.repository.article;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.article.Category;
import com.example.backend.domain.article.QArticleEntity;
import com.example.backend.domain.user.QUserProfileEntity;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepoSupportImpl extends QuerydslRepositorySupport implements ArticleRepoSupport {

    public ArticleRepoSupportImpl() {
        super(ArticleEntity.class);
    }

    @Override
    public List<ArticleEntity> findByRange(Double[] lnglat, String range) {
        QArticleEntity article = QArticleEntity.articleEntity;
        JPQLQuery<ArticleEntity> jpqlQuery = from(article);
        jpqlQuery.where(article.userProfileEntity.isNotNull());
        jpqlQuery.leftJoin(article.userProfileEntity, QUserProfileEntity.userProfileEntity);
        jpqlQuery.fetchJoin();
        jpqlQuery.where(Expressions.stringTemplate("function('ST_Distance_Sphere', Point({0},{1}), Point({2},{3}))",
                        QUserProfileEntity.userProfileEntity.lng, QUserProfileEntity.userProfileEntity.lat, lnglat[0], lnglat[1])
                .loe(range)).orderBy(article.createdAt.desc());
        return jpqlQuery.fetch();
    }

    @Override
    public List<ArticleEntity> findByKeywordAndRange(String keyword, Double[] lnglat, String range) {
        QArticleEntity article = QArticleEntity.articleEntity;
        JPQLQuery<ArticleEntity> jpqlQuery = from(article);
        jpqlQuery.where(article.userProfileEntity.isNotNull());
        jpqlQuery.leftJoin(article.userProfileEntity, QUserProfileEntity.userProfileEntity);
        jpqlQuery.fetchJoin();
        jpqlQuery.where(Expressions.stringTemplate("function('ST_Distance_Sphere', Point({0},{1}), Point({2},{3}))",
                        QUserProfileEntity.userProfileEntity.lng, QUserProfileEntity.userProfileEntity.lat, lnglat[0],lnglat[1])
                .loe(range)
                .and(article.title.contains(keyword)
                        .or(article.content.contains(keyword)))).orderBy(article.createdAt.desc());
        return jpqlQuery.fetch();
    }

    @Override
    public List<ArticleEntity> findByKeywordAndCategory(String keyword, String category) {
        QArticleEntity article = QArticleEntity.articleEntity;
        JPQLQuery<ArticleEntity> jpqlQuery = from(article);
        jpqlQuery.where(article.userProfileEntity.isNotNull());
        jpqlQuery.where(article.category.eq(Category.valueOf(category))
                        .and(article.title.contains(keyword).or(article.content.contains(keyword))))
                .orderBy(article.createdAt.desc());
        return jpqlQuery.fetch();
    }

    @Override
    public List<ArticleEntity> findByCategoryAndRange(String category, Double[] lnglat, String range) {
        QArticleEntity article = QArticleEntity.articleEntity;
        JPQLQuery<ArticleEntity> jpqlQuery = from(article);
        jpqlQuery.where(article.userProfileEntity.isNotNull());
        jpqlQuery.leftJoin(article.userProfileEntity, QUserProfileEntity.userProfileEntity);
        jpqlQuery.fetchJoin();
        jpqlQuery.where(Expressions.stringTemplate("function('ST_Distance_Sphere', Point({0},{1}), Point({2},{3}))",
                        QUserProfileEntity.userProfileEntity.lng, QUserProfileEntity.userProfileEntity.lat, lnglat[0],lnglat[1])
                .loe(range)
                .and(article.category.eq(Category.valueOf(category)))).orderBy(article.createdAt.desc());
        return jpqlQuery.fetch();
    }

    @Override
    public List<ArticleEntity> findByAllCondition(String keyword, String category, Double[] lnglat, String range) {
        QArticleEntity article = QArticleEntity.articleEntity;
        JPQLQuery<ArticleEntity> jpqlQuery = from(article);
        jpqlQuery.where(article.userProfileEntity.isNotNull());
        jpqlQuery.leftJoin(article.userProfileEntity, QUserProfileEntity.userProfileEntity);
        jpqlQuery.fetchJoin();
        jpqlQuery.where(Expressions.stringTemplate("function('ST_Distance_Sphere', Point({0},{1}), Point({2},{3}))",
                        QUserProfileEntity.userProfileEntity.lng, QUserProfileEntity.userProfileEntity.lat, lnglat[0],lnglat[1])
                .loe(range)
                .and(article.category.eq(Category.valueOf(category)))
                .and(article.title.contains(keyword).or(article.content.contains(keyword)))).orderBy(article.createdAt.desc());
        return jpqlQuery.fetch();
    }
}
