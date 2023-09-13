package com.example.backend.web.dto.chat;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatMessageDetailDto {

    private ChatUserDto user;
    private ChatMessageDto message;

    @Builder
    public ChatMessageDetailDto(ChatUserDto user, ChatMessageDto message) {
        this.user = user;
        this.message = message;
    }
}
