package com.example.backend.web.controller;

import com.example.backend.service.zzim.ZzimService;
import com.example.backend.web.dto.zzim.ZzimDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("zzim")
@AllArgsConstructor
public class ZzimController {

        private final ZzimService zzimService;


        @GetMapping
        public ResponseEntity<Map<String, Object>> getZzim(@RequestParam(required = false) String profileId,
                                                           @RequestParam(required = false) String articleId) {
            Map<String, Object> result = new HashMap<>();
            HttpStatus status = null;
            try {
                if (profileId != null && articleId != null) {
                    result.put("zzim", zzimService.getZzim(profileId, Long.parseLong(articleId)));
                } else if (profileId != null) {
                    result.put("zzimList", zzimService.getZzimList(profileId));
                } else {
                    result.put("zzimList", zzimService.getZzimList(Long.parseLong(articleId)));
                }
                status = HttpStatus.OK;
                result.put("success", true);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                status = HttpStatus.OK;
                result.put("success", true);
            } catch (RuntimeException e) {
                e.printStackTrace();
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                result.put("success", false);

            }
            return new ResponseEntity<Map<String, Object>>(result, status);
        }


        @PostMapping
        public ResponseEntity<Map<String, Object>> insertZzim(@RequestBody Map<String, String> body) {
            Map<String, Object> result = new HashMap<>();
            HttpStatus status = null;
            try {
                ZzimDto zzim = zzimService.insertZzim(body.get("profileid"), Long.parseLong(body.get("articleid")));
                status = HttpStatus.OK;
                result.put("zzim", zzim);
                result.put("success", true);
            } catch (RuntimeException e) {
                e.printStackTrace();
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                result.put("success", false);
            }

            return new ResponseEntity<Map<String, Object>>(result, status);
        }


        @DeleteMapping
        public ResponseEntity<Map<String, Object>> deleteZzim(@RequestBody Map<String, String> body) {
            Map<String, Object> result = new HashMap<>();
            HttpStatus status = null;
            try {
                zzimService.deleteZzim(body.get("profileid"), Long.parseLong(body.get("articleid")));
                status = HttpStatus.OK;
                result.put("success", true);
            } catch (RuntimeException e) {
                e.printStackTrace();
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                result.put("success", false);
            }

            return new ResponseEntity<Map<String, Object>>(result, status);
        }
}
