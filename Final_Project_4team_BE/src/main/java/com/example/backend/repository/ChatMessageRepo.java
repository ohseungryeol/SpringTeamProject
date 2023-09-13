package com.example.backend.repository.chat;

import com.example.backend.domain.chat.ChatMessageEntity;
import com.example.backend.domain.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findAllByChatRoomEntityOrderByTime(ChatRoomEntity chatRoomEntity);
}
