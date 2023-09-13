package com.example.backend.web.dto.chat;


import com.example.backend.domain.chat.ChatMessageEntity;
import com.example.backend.domain.chat.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private MessageType type;
    private String content;
    private String sender;
    private String time;
    private String roomId;

    public ChatMessageDto(ChatMessageEntity chatMessage) {
        this.type = chatMessage.getType();
        this.content = chatMessage.getContent();
        this.sender = chatMessage.getSender();
        this.time = chatMessage.getTime();
        this.roomId = chatMessage.getChatRoomEntity().getId();
    }
}
