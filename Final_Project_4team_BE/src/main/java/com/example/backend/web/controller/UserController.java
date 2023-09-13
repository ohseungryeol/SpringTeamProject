package com.example.backend.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.backend.web.dto.user.UserProfileDto;
import com.example.backend.service.User.UserService;

import java.util.HashMap;
import java.util.Map;

//TODO 오류 수정 필요
@RestController
@RequestMapping("/user")
@Tag(name = "사용자 컨트롤러") // Swagger 태그를 Tag 어노테이션으로 지정
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "로그인", description = "로그인을 시도한다, 성공시 프로필 정보를 반환하고 실패시 null을 반환한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        try {
            UserProfileDto userProfileDto = userService.login(body.get("authid")); // thirdPartyId
            result.put("profile", userProfileDto);
            result.put("success", true);

            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);

        }

        return ResponseEntity.status(status).body(result);
    }

    @Operation(summary = "회원가입",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserProfileDto userProfileDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status = null;
        try {
            userProfileDto = userService.register(userProfileDto);
            result.put("profile", userProfileDto);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(status).body(result);
    }

    @Operation(summary = "회원 사진 업로드",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadProfilePicture(@RequestPart(required = false) MultipartFile uploadFile) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        try {
            String url = userService.upload(uploadFile);
            status = HttpStatus.OK;
            result.put("url", url);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return ResponseEntity.status(status).body(result);
    }

    @Operation(summary = "회원정보 수정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody UserProfileDto userProfileDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        try {
            userProfileDto = userService.update(userProfileDto);
            result.put("profile", userProfileDto);
            result.put("success", true);

            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return ResponseEntity.status(status).body(result);
    }

    @Operation(summary = "회원 탈퇴",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteProfile(@RequestBody UserProfileDto userProfileDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        try {
            userService.delete(userProfileDto);
            status = HttpStatus.OK;
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return ResponseEntity.status(status).body(result);
    }

    @Operation(summary = "프로필 정보 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
            })
    @GetMapping("/{profileId}")
    public ResponseEntity<Map<String, Object>> getProfileInfo(@PathVariable String profileId) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        try {
            UserProfileDto userProfileDto = userService.getUserProfileInfo(profileId);
            result.put("profile", userProfileDto);
            result.put("success", true);

            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }

        return ResponseEntity.status(status).body(result);
    }
}


