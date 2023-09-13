package com.example.backend.web.controller;

import com.example.backend.service.qna.QnaService;
import com.example.backend.util.ValidCheck;
import com.example.backend.web.dto.CMRespDto;
import com.example.backend.web.dto.CustomPage;
import com.example.backend.web.dto.qna.QnaDto;
import com.example.backend.web.dto.qna.ReqQnaCommentPutDto;
import com.example.backend.web.dto.qna.ReqQnaPostDto;
import com.example.backend.web.dto.qna.ReqQnaPutDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
@Tag(name = "Qna API", description = "Qna API Document")
public class QnaController {
    private final QnaService qnaService;

    /**
     * 1 대 1 문의 생성
     * */
    @PostMapping("/{profileId}")
    @Operation(summary = "qna 글 생성 API", description = "Qna 글을 생성합니다")
    @ApiResponse(responseCode = "201", description = "Qan 등록이 완료되었습니다")
    public ResponseEntity<?> createQna(@PathVariable String profileId, @Valid @ModelAttribute ReqQnaPostDto dto, BindingResult bindingResult){
        // dto 값 체크
        ValidCheck.validCheck(bindingResult);

        QnaDto result = qnaService.saveQna(profileId, dto);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.CREATED.value(), "Qan 등록이 완료되었습니다", result), HttpStatus.CREATED);
    }

    /**
     * 1 대 1 문의 전체 보기 (본인이 쓴 거)
     * */
    @GetMapping("/{profileId}")
    @Operation(summary = "qna 글 불러오기 API", description = "본인이 쓴 Qna를 모두 불러옵니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다.")
    public ResponseEntity<?> getAllQnaDetailByUser(@PathVariable String profileId) {
        // ValidCheck.pageValidCheck(--page, limit);

        // PageRequest pageRequest = PageRequest.of(page, limit);
        List<QnaDto> result = qnaService.findAllByProfile(profileId);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 불러왔습니다.", result), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의글 보기 (본인이 쓴 거)
     * */
    @GetMapping("/{profileId}/{qnaId}")
    @Operation(summary = "qna 글 불러오기 API", description = "본인이 쓴 Qna를 불러옵니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다.")
    public ResponseEntity<?> getQnaDetail(@PathVariable String profileId, @PathVariable Long qnaId) {

        QnaDto result = qnaService.getQnaDetail(profileId, qnaId);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 불러왔습니다.", result), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의글 보기 (본인이 쓴 거)
     * */
    @GetMapping("/admin/{qnaId}")
    @Operation(summary = "qna 글 불러오기 API", description = "본인이 쓴 Qna를 불러옵니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다.")
    public ResponseEntity<?> getQnaDetail(@PathVariable Long qnaId) {

        QnaDto result = qnaService.getQnaDetail(qnaId);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 불러왔습니다.", result), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의 전체 보기 (관리자)
     * */
    @GetMapping("/admin")
    @Operation(summary = "qna 글 불러오기(관리자) API", description = "Qna를 모두 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다.")
    public ResponseEntity<?> getAllQnaDetailByOwner() {
        // ValidCheck.pageValidCheck(--page, limit);

        // PageRequest pageRequest = PageRequest.of(page, limit);
        List<QnaDto> result = qnaService.findAll();

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 불러왔습니다.", result), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의 수정 (본인만)
     * */
    @PutMapping("/{profileId}/{qnaId}")
    @Operation(summary = "qna 글 수정 API", description = "Qna 글을 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 수정했습니다.")
    public ResponseEntity<?> updateQnaDetail(@PathVariable String profileId, @PathVariable Long qnaId, @Valid @ModelAttribute ReqQnaPutDto dto, BindingResult bindingResult){
        ValidCheck.validCheck(bindingResult);

        QnaDto result = qnaService.updateQna(profileId, qnaId, dto);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 수정했습니다", result), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의 삭제 (본인 or 관리자)
     * */
    @DeleteMapping("/{profileId}/{qnaId}/delete")
    @Operation(summary = "qna 글 삭제 API", description = "Qna 글을 삭제합니다. 본인 혹은 관리자만이 삭제할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 삭제왔습니다.")
    public ResponseEntity<?> deleteQnaDetail(@PathVariable String profileId, @PathVariable Long qnaId) {
        qnaService.deleteQna(profileId, qnaId);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 삭제했습니다", null), HttpStatus.OK);
    }

    /**
     * 1 대 1 문의 댓글(답) 달기 (관리자만 가능)
     * */
    @PutMapping("/{profileId}/{qnaId}/comment")
    @Operation(summary = "qna 댓글 추가 API", description = "Qna 댓글을 생성합니다. 댓글은 관리자만 추가할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 댓글을 추가했습니다")
    public ResponseEntity<?> addComment(@PathVariable String profileId, @PathVariable Long qnaId, @Valid @RequestBody ReqQnaCommentPutDto dto, BindingResult bindingResult) {
        ValidCheck.validCheck(bindingResult);

        QnaDto result = qnaService.saveComment(profileId, qnaId, dto);

        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.OK.value(), "성공적으로 댓글을 추가했습니다", result), HttpStatus.OK);
    }
}
