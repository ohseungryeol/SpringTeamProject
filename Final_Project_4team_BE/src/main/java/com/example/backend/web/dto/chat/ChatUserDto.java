package com.example.backend.web.dto.chat;

import com.example.backend.domain.user.UserProfileEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class ChatUserDto {

    private String userId;
    private String nickname;
    private String pic;

    @Builder
    public ChatUserDto(String userId, String nickname, String pic) {
        this.userId = userId;
        this.nickname = nickname;
        this.pic = pic;
    }

    public static ChatUserDto fromEntity(UserProfileEntity userProfileEntity){
        return ChatUserDto.builder()
                .userId(userProfileEntity.getId())
                .nickname(userProfileEntity.getNickname())
                .pic(userProfileEntity.getPic())
                .build();
    }

}
