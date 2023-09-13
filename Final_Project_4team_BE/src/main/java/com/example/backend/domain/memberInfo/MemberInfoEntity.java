package com.example.backend.domain.memberInfo;

import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 파티 참여자들 정보 entity
public class MemberInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private Boolean isBoss; //파티장 여부

    //    @Positive
    @NotNull
    private Integer amount; // 파티원 선택수량

    //    @Positive
    @NotNull
    private Integer price; // 파티원 부담금액

    private String paidMethod; //결제 방법

    @Column(columnDefinition = "boolean default false")
    private Boolean isConfirmed; //파티원 구매확정여부

    @Column(columnDefinition = "boolean default false")
    private Boolean isLated; // 위약금 부담여부


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id")
    private UserProfileEntity userProfileEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="party_id")
    private PartyEntity partyEntity;


}