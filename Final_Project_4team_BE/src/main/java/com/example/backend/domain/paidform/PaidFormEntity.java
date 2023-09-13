package com.example.backend.domain.paidform;

import com.example.backend.domain.party.PartyEntity;
import com.example.backend.handler.ex.CustomApiValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaidFormEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pic;

    private String billingNo;

    private LocalDate deliveryDate;

    private LocalDate receiptDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", unique = true)
    private PartyEntity partyEntity;

    @Builder
    protected PaidFormEntity(Long id, String pic, String billingNo, LocalDate deliveryDate, LocalDate receiptDate, PartyEntity partyEntity) {
        this.id = id;
        this.pic = pic;
        this.billingNo = billingNo;
        this.deliveryDate = deliveryDate;
        this.receiptDate = receiptDate;
        this.partyEntity = partyEntity;
    }

    //== 생성 메서드 ==//
    public static PaidFormEntity createPaidFormEntity(String pic, String billingNo, String deliveryDate, String receiptDate) {
        return PaidFormEntity.builder()
                .pic(pic)
                .billingNo(billingNo)
                .deliveryDate(LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_DATE))   // 2021-10-01 -> 이런 형식을 LocalDate로 바꿔줌
                .receiptDate(LocalDate.parse(receiptDate, DateTimeFormatter.ISO_DATE))
                .build();
    }

    //== 수정 메서드 ==//
    // 사진은 제외 -> 따로 업로드가 필요하기 때문
    // 여기 부분 코드를 리팩토링할 필요가 있어 보임
    public void updatePaidForm(String pic, String billingNo, String deliveryDate, String receiptDate) {
        if(!this.pic.equals(pic)) this.pic = pic;
        if(!this.billingNo.equals(billingNo)) this.billingNo = billingNo;
        if(!this.deliveryDate.toString().equals(LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_DATE).toString())) this.deliveryDate = LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_DATE);
        if(!this.receiptDate.toString().equals(LocalDate.parse(receiptDate, DateTimeFormatter.ISO_DATE).toString())) this.receiptDate = LocalDate.parse(receiptDate, DateTimeFormatter.ISO_DATE);
    }

    //== 연관 관계 메서드 ==//
    public void setParty(PartyEntity party) {
        this.partyEntity = party;
        party.setPaidFormEntity(this);
        if(partyEntity.getFormChecked()) throw new CustomApiValidationException("이미 결제 인증 폼이 등록되어 있습니다.", null);
        party.setFormChecked(true);
    }
}
