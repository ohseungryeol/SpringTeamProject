package com.example.backend.repository.chat;

import com.example.backend.domain.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepo extends JpaRepository<ChatRoomEntity, String> {

    List<ChatRoomEntity> findChatRoomEntityByArticleId(Long articleId);
}
