package com.example.backend.web.controller;

import com.example.backend.service.paidform.PaidFormService;
import com.example.backend.util.ValidCheck;
import com.example.backend.web.dto.CMRespDto;
import com.example.backend.web.dto.paidform.PaidFormDto;
import com.example.backend.web.dto.paidform.ReqPaidFormPostDto;
import com.example.backend.web.dto.paidform.ReqPaidFormPutDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paidForm")
@RequiredArgsConstructor
@Tag(name = "PaidForm API", description = "PaidForm API Document")
public class PaidFormController {

    private final PaidFormService paidFormService;

    /**
     * 구매 인증 폼 등록
     * */
    @PostMapping("/{partyId}")
    @Operation(summary = "구매 인증 폼 생성 API", description = "구매 인증 폼을 생성합니다")
    public ResponseEntity<?> insert(@PathVariable Long partyId, @Valid @ModelAttribute ReqPaidFormPostDto dto, BindingResult bindingResult) {
        ValidCheck.validCheck(bindingResult);

        PaidFormDto result = paidFormService.insertPaidForm(partyId, dto);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.CREATED.value(), "결제 인증 폼이 등록되었습니다.", result), HttpStatus.CREATED);
    }

    /**
     * 결제 인증 폼 조회
     * */
    @GetMapping("/{partyId}")
    @Operation(summary = "구매 인증 폼 조회 API", description = "구매 인증 폼을 조회합니다")
    public ResponseEntity<?> getPaidForm(@PathVariable Long partyId) {
        PaidFormDto result = paidFormService.getPaidForm(partyId);
        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "결제 인증 폼을 불러왔습니다.", result), HttpStatus.OK);
    }

    /**
     * 결제 인증 폼 수정
     * */
    @PutMapping("/{partyId}")
    @Operation(summary = "구매 인증 폼 수정 API", description = "구매 인증 폼을 수정합니다")
    public ResponseEntity<?> updatePaidForm(@PathVariable Long partyId, @Valid @ModelAttribute ReqPaidFormPutDto dto, BindingResult bindingResult) {
        ValidCheck.validCheck(bindingResult);

        PaidFormDto result = paidFormService.updatePaidForm(partyId, dto);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "결제 인증 폼을 수정했습니다.", result), HttpStatus.OK);
    }

}
