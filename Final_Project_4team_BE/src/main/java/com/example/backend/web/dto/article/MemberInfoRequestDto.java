package com.example.backend.web.dto.article;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequestDto {

    private  Integer amount; //선택 수량

    private  Integer price; //파티원 부담 금액

    private String paidMethod;

    private  Long partyId; //파티 아이디

    private String profileId; //프로필 아이디

    public MemberInfoRequestDto(int amount,int price,String paidMethod,long partyId,String profileId){
        this.amount=amount;
        this.price=price;
        this.paidMethod=paidMethod;
        this.partyId=partyId;
        this.profileId=profileId;
    }

//    public MemberInfo toEntity(){
//        return MemberInfo.builder()
//                .amount(amount)
//                .price(price)
//                .partyId(partyId)
//                .profileId(profileId)
//    }

}