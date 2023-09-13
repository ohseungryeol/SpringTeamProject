package com.example.backend.web.controller;

import com.example.backend.service.article.PartyService;
import com.example.backend.web.dto.MessageDto;
import com.example.backend.web.dto.party.PartyDto;
import com.example.backend.web.dto.party.PartyResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;
    //@GetMapping("/{partyId}")


    @Tag(name="내 파티리스트 조회 API", description = "내 파티 전체조회")
    @GetMapping("/{profileId}")
    public ResponseEntity<List<PartyResponseDto>> GetPartyList(@PathVariable("profileId") String profileId){
        List<PartyResponseDto> list= partyService.getPartyList(profileId);
        return new ResponseEntity<List<PartyResponseDto>>(list, HttpStatus.OK);
    }

    @Tag(name="파티 디테일 리스트 조회 API", description = "해당 파티 참여한 사용자 정보들을 조회")
    @GetMapping
    public ResponseEntity<List<PartyDto>> getDetailList(@RequestParam("partyId") Long partyId){
        List<PartyDto> party=partyService.getDetailList(partyId);
        return new ResponseEntity<List<PartyDto>>(party,HttpStatus.OK);
    }

    //파티리스트 삭제

    @Tag(name="파티 삭제 API", description = "파티를 삭제한다.")
    @DeleteMapping("/{partyId}")
    public ResponseEntity<Map<String, Object>> deleteParty(@PathVariable Long partyId){
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus = null;
        try{
            partyService.deleteParty(partyId);
            httpStatus = HttpStatus.OK;
            result.put("success", true);
        }catch (RuntimeException e){
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String,Object>>(result, httpStatus);
    }



    @Tag(name="파티마감 구매확정 API", description = "파티를 마감하고 구매를 확정한다.")
    @GetMapping("/finish/{partyId}")
    public ResponseEntity<Map<String,Object>> finishParty(@PathVariable Long partyId){
        Map<String,Object>result=new HashMap<>();
        HttpStatus status=null;
        try{
            partyService.finishParty(partyId);
            status=HttpStatus.OK;
            result.put("success", true);
        }catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, status);
    }
}
