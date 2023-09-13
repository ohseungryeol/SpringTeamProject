package com.example.backend.service.paidform;

import com.example.backend.domain.paidform.PaidFormEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.handler.ex.CustomApiFileUploadException;
import com.example.backend.handler.ex.CustomApiNotFoundException;
import com.example.backend.repository.article.PartyRepo;
import com.example.backend.repository.paidform.PaidFormRepo;
import com.example.backend.service.s3.S3Service;
import com.example.backend.web.dto.paidform.PaidFormDto;
import com.example.backend.web.dto.paidform.ReqPaidFormPostDto;
import com.example.backend.web.dto.paidform.ReqPaidFormPutDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.SpannerSqlAstTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaidFormService {

    private final PaidFormRepo paidFormRepo;
    private final PartyRepo partyRepo;
    private final S3Service s3Service;

    @Transactional
    public PaidFormDto insertPaidForm(Long partyId, ReqPaidFormPostDto dto) {
        // 1. 파티 찾기
        PartyEntity partyEntity = partyRepo.findById(partyId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 파티를 찾지 못했습니다");
        });

        // 2, dto의 이미지 파일 업로드
        String pic = null;
        try {
            pic = s3Service.saveFile(dto.getPic());
        } catch (IOException e) {
            throw new CustomApiFileUploadException("파일 업로드에 실패하였습니다");
        }

        // 3. paidForm엔티티 생성
        PaidFormEntity paidForm = PaidFormEntity.createPaidFormEntity(pic, dto.getBillingNo(), dto.getDeliveryDate(), dto.getReceiptDate());
        PaidFormEntity paidFormEntity = paidFormRepo.save(paidForm);

        // 3. 파티와 연결
        paidFormEntity.setParty(partyEntity);

        // dto로 변환 후 return
        return PaidFormDto.fromEntity(paidFormEntity);
    }

    @Transactional(readOnly = true)
    public PaidFormDto getPaidForm(Long partyId) {
        // 1. 파티 가져오기
        PartyEntity partyEntity = partyRepo.findById(partyId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 파티를 찾지 못했습니다");
        });

        // 2. 파티에 속한 결제 인증 폼 가져오기
        PaidFormEntity paidFormEntity = null;
        try {
            paidFormEntity = partyEntity.getPaidFormEntity();
        } catch (Exception e) {
            throw new CustomApiNotFoundException("파티에 대한 결제 인증 폼을 찾을 수 없습니다.");
        }

        // 3. dto로 변환 후 return
        return PaidFormDto.fromEntity(paidFormEntity);
    }

    @Transactional
    public PaidFormDto updatePaidForm(Long partyId, ReqPaidFormPutDto dto) {
        // 1. 파티 가져오기
        PartyEntity partyEntity = partyRepo.findById(partyId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 파티를 찾지 못했습니다");
        });

        // 2. 파티에 속한 결제 인증 폼 가져오기
        PaidFormEntity paidFormEntity = null;
        try {
            paidFormEntity = partyEntity.getPaidFormEntity();
        } catch (Exception e) {
            throw new CustomApiNotFoundException("파티에 대한 결제 인증 폼을 찾을 수 없습니다.");
        }

        // 3. 달라진 값과 비교하여 수정
        String originalFilename = dto.getPic().getOriginalFilename();
        String[] entityFileName = paidFormEntity.getPic().split("_");
        String pic = paidFormEntity.getPic();

        if(!originalFilename.equals(entityFileName[0])) {
            try {
                s3Service.deleteImage(paidFormEntity.getPic());
                pic = s3Service.saveFile(dto.getPic());
            } catch (Exception e) {
                throw new CustomApiFileUploadException("파일 수정 과정중 문제가 생겼습니다.");
            }
        }

        paidFormEntity.updatePaidForm(pic, dto.getBillingNo(), dto.getDeliveryDate(), dto.getReceiptDate());

        // 4. dto로 변환 후 return
        return PaidFormDto.fromEntity(paidFormEntity);
    }
}
