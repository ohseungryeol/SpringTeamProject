package com.example.backend.web.dto.party;


import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyResponseDto {

    private String title; //제목

    private Tag tag; //태그

    private Boolean isBoss; //파티장/원 유무

    private Long id; //파티 아이디

    private LocalDateTime deadline; //마감일

    private Integer totalPrice; //총금액

    private Integer totalRecruitMember; //총 모집인원수

    private Integer currentRecruitMember; //현재 인원수

    private Integer myPrice; // 총 금액/현재 인원수

    public PartyResponseDto(PartyEntity party, MemberInfoEntity memberInfo){
        title= party.getArticleEntity() == null ? "원글이 삭제된 파티입니다." : party.getArticleEntity().getTitle();
        //tag=party.getArticle().getTag();
        isBoss=memberInfo.getIsBoss();
        id=party.getId();
        deadline=party.getDeadline();
        totalPrice=party.getTotalPrice();
        totalRecruitMember=party.getTotalRecruitMember();
        currentRecruitMember=party.getCurrentRecruitMember();
        myPrice=party.getTotalPrice()/totalRecruitMember;
    }
}
