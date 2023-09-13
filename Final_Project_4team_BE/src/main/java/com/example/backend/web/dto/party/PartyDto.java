package com.example.backend.web.dto.party;


import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//파티 상세정보에 보여줄 dto
@Data
@NoArgsConstructor
@AllArgsConstructor
 public class PartyDto {

    //article

    private Long articleId; //게시판아이디

    //userProfile

    private String profileId; //프로필아이디

    private String address; //주소

    private String nickname; //닉네임

    private LocalDateTime joinDate; //가입일

    private String pic; //사진

    private Integer score; //점수

    //party

    private Long id; //파티 번호

    private String title; //제목

    private String product; //상품

    private LocalDateTime deadline; //진행기간

    private Boolean isClosed; //파티 마감 여부

    //memberinfo

    private Boolean isBoss; //파티장 여부
 //   private Integer totalcount; //내가 선택한 인분 => front가 알아서 계산할듯

    private Integer totalamount; // 내가 선택 총 수량

    private Integer price; // 결제금액

    private Boolean isConfirmed; //파티원 구매확정여부

    public PartyDto(UserProfileEntity userProfile, PartyEntity party, MemberInfoEntity memberInfo){
        //==userprofile==//
        articleId=party.getArticleEntity().getId();
        //==userprofile==//
        address=userProfile.getAddress();
        nickname=userProfile.getNickname();
        joinDate=userProfile.getJoinDate();
        pic=userProfile.getPic();
        score=userProfile.getScore();
        //==party==//
        id=party.getId();
        title=party.getArticleEntity().getTitle();
        product=party.getProduct();
        deadline=party.getDeadline();
        isClosed=party.getIsClosed();
        //==memberinfo==//
        profileId=memberInfo.getUserProfileEntity().getId();
        isBoss=memberInfo.getIsBoss();
        totalamount=memberInfo.getAmount();
        isConfirmed=memberInfo.getIsConfirmed();
        price=memberInfo.getPrice();

    }

}