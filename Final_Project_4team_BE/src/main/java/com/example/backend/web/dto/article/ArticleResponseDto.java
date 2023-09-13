package com.example.backend.web.dto.article;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.article.Category;
import com.example.backend.domain.article.Tag;
import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data //게시글 상세 페이지
// fromEntity 메서드 오류 제거를 위한 임시방편. 해당 메서드 삭제완료되면 지워야함!
@NoArgsConstructor
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String product;
    private LocalDateTime createdAt;
    private String link;
    @ElementCollection
    private List<String> pic;
    private Integer totalPrice;
    private Integer totalRecruitMember;
    private Integer myPrice;
    private Integer currentRecruitMember;
    private Tag tag;
    private Category category;
    private Integer penalty;
    private LocalDateTime deadline;
    private Integer totalProductCount;
    private Long partyId;
    // 찜 유무..

    public ArticleResponseDto(ArticleEntity article) {
        id = article.getId();
        title = article.getTitle();
        author = article.getUserProfileEntity() == null ? "탈퇴한 사용자입니다" :article.getUserProfileEntity().getNickname();
        product = article.getPartyEntity().getProduct();
        content = article.getContent();
        createdAt = article.getCreatedAt();
        link = article.getLink();
        pic = article.getPic();
        totalPrice = article.getPartyEntity().getTotalPrice();
        totalRecruitMember = article.getPartyEntity().getTotalRecruitMember();
        currentRecruitMember = article.getPartyEntity().getCurrentRecruitMember();
        myPrice = article.getPartyEntity().getTotalPrice() / totalRecruitMember;
        tag = article.getTag();
        category = article.getCategory();
        penalty = article.getPartyEntity().getPenalty();
        deadline = article.getPartyEntity().getDeadline();
        totalProductCount = article.getPartyEntity().getTotalProductCount();
        partyId=article.getPartyEntity().getId();
    }
}
