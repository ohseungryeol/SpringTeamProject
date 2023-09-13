package com.example.backend.web.dto.chat;

import com.example.backend.domain.chat.ChatMessageEntity;
import com.example.backend.domain.chat.ChatRoomEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomDto extends ChatRoomEntity {

    private String id;
    private String name;
    private Integer count;
    private List<ChatMessageDto> msgList = new ArrayList<>();// msg내용만

    public ChatRoomDto(ChatRoomEntity chatRoomEntity) {
        this.id = chatRoomEntity.getId();
        this.name = chatRoomEntity.getName();
        this.count = chatRoomEntity.getCount();
        for(ChatMessageEntity s: chatRoomEntity.getChatMessageList()){
            this.msgList.add(new ChatMessageDto(s));
        }
    }
}
