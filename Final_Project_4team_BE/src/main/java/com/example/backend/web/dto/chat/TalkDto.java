package com.example.backend.web.dto.chat;

import lombok.Data;

@Data
public class TalkDto {
    private String userId;
    private String nickname;
    private String content;
}
