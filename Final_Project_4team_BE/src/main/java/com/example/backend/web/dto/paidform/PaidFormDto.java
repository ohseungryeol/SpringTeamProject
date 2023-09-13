package com.example.backend.web.dto.paidform;

import com.example.backend.domain.paidform.PaidFormEntity;
import com.example.backend.domain.party.PartyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(description = "PaidForm 응답 Dto")
public class PaidFormDto {
    @Schema(description = "PaidFrom id", example = "1")
    private Long id;

    @Schema(description = "사진")
    private String pic;

    @Schema(description = "결제 번호", example = "123asd")
    private String billingNo;

    @Schema(description = "배송일자", example = "2021-10-10")
    private String deliveryDate;

    @Schema(description = "구매일자", example = "2021-10-10")
    private String receiptDate;

    @Schema(description = "party id", example = "1")
    private Long partyId;

    @Builder
    protected PaidFormDto(Long id, String pic, String billingNo, String deliveryDate, String receiptDate, Long partyId) {
        this.id = id;
        this.pic = pic;
        this.billingNo = billingNo;
        this.deliveryDate = deliveryDate;
        this.receiptDate = receiptDate;
        this.partyId = partyId;
    }

    //== 생성 메서드 ==//
    public static PaidFormDto fromEntity(PaidFormEntity entity) {
        return PaidFormDto.builder()
                .id(entity.getId())
                .partyId(entity.getPartyEntity().getId())
                .pic(entity.getPic())
                .billingNo(entity.getBillingNo())
                .deliveryDate(entity.getDeliveryDate().toString())
                .receiptDate(entity.getReceiptDate().toString())
                .build();
    }
}
