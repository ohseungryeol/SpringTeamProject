package com.example.backend.domain.party;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.paidform.PaidFormEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class PartyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    // 구매폼 작성여부
    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean formChecked;

    @NotNull
    @FutureOrPresent
    private LocalDateTime deadline;

    @NotNull
    private String product;

    @NotNull
    private Integer totalPrice; //총금액

    @NotNull
    private Integer totalProductCount; //총 물건수량

    @NotNull
    private Integer totalRecruitMember; //총 모집인원수

    @Positive
    @NotNull
    private Integer currentRecruitMember; //현재 인원수

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean isClosed; //마감

    @NotNull
    private Integer penalty; //위약금

    @OneToOne(mappedBy = "partyEntity",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private PaidFormEntity paidFormEntity;

    @OneToOne(mappedBy = "partyEntity",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private ArticleEntity articleEntity;

    @OneToMany(mappedBy = "partyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberInfoEntity> memberInfos = new ArrayList<>();

}
