package com.example.backend.web.controller;

import com.example.backend.service.article.MemberInfoService;
import com.example.backend.web.dto.MessageDto;
import com.example.backend.web.dto.article.MemberInfoRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memberInfo")
@RequiredArgsConstructor
public class MemberInfoController {
    private final MemberInfoService memberInfoService;

    @Tag(name="파티 참여 API", description = "파티참여 폼")
    @PostMapping
    public ResponseEntity<Map<String,Object>> insertMemberInfo(@RequestBody MemberInfoRequestDto memberInfoRequestDto){
        Map<String,Object>result=new HashMap<>();
        HttpStatus status=null;
        try{
            memberInfoService.insertMemberInfo(memberInfoRequestDto);
            status=HttpStatus.OK;
            result.put("success", true);
        }catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, status);
    }
    @Tag(name="사용자 구매확정 API", description = "사용자가 물건을 받고 구매확정을 한다.")

    @GetMapping
    public ResponseEntity<Map<String,Object>> confirmMemberInfo(@RequestParam Long partyId,@RequestParam String profileId){
        Map<String,Object>result=new HashMap<>();
        HttpStatus status=null;
        try{
            memberInfoService.confirmMemberInfo(partyId,profileId);
            status=HttpStatus.OK;
            result.put("success", true);
        }catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @Tag(name="파티 나가기 API", description = "참가 취소")
    @DeleteMapping()
    public ResponseEntity<Map<String, Object>> deleteMemberInfo(@RequestParam Long partyId,@RequestParam String profileId){
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus = null;
        try{
            memberInfoService.deleteMemberInfo(partyId,profileId);
            httpStatus = HttpStatus.OK;
            result.put("success", true);
        }catch (RuntimeException e){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String,Object>>(result, httpStatus);
    }

}
