package com.example.backend.web.dto.user;


import com.example.backend.domain.user.UserProfileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema( defaultValue = "유저 프로필 정보", description = "로그인, 회원가입 등에 사용할 사용자 프로필 정보")
public class UserProfileDto {

    // 사용자 ID
    @Schema( defaultValue = "회원 가입 ID", example = "123456")
    private String id;

    // 주소
    @Schema( defaultValue = "사용자 주소", example = "인천광역시 연수구 송도동")
    private String address;

    // 닉네임
    @Schema( defaultValue = "사용자 닉네임", example = "닉네임")
    private String nickname;

    // 회원 가입 시간
    @Schema( defaultValue = "화원 가입 시간", example = " ")
    private LocalDateTime joinDate;

    // 사진
    @Schema( defaultValue = "사용자 사진", example = "Url")
    private String pic;

    // 평점
    @Schema( defaultValue = "사용자 평점", example = "10")
    private Integer score;

    // 관리자 여부
    @Schema( defaultValue = "관리자", example = "false")
    private Boolean isAdmin;

    public UserProfileDto(UserProfileEntity userProfile){
        this.id = userProfile.getId();
        this.address = userProfile.getAddress();
        this.nickname = userProfile.getNickname();
        this.joinDate = userProfile.getJoinDate();
        this.pic = userProfile.getPic();
        this.score = userProfile.getScore();
        this.isAdmin = userProfile.getUserAuthEntity().getIsAdmin();

    }
}
