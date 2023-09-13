package com.example.backend.repository.chat;

import com.example.backend.domain.chat.ChatRoomEntity;
import com.example.backend.domain.chat.ChatRoomJoinEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomJoinRepo extends JpaRepository<ChatRoomJoinEntity, Long> {

    void deleteChatRoomJoinByChatRoomEntityAndUserProfileEntity(ChatRoomEntity chatRoom, UserProfileEntity userProfile);
    List<ChatRoomJoinEntity> findAllByChatRoomEntity(ChatRoomEntity chatRoomEntity);
    List<ChatRoomJoinEntity> findAllByUserProfileEntity(UserProfileEntity userProfileEntity);
}
