package com.example.backend.domain.chat;


import com.example.backend.domain.user.UserProfileEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomJoinEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id")
    private UserProfileEntity userProfileEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoomEntity chatRoomEntity;

    public static ChatRoomJoinEntity createRoomJoin(ChatRoomEntity chatRoomEntity, UserProfileEntity userProfileEntity) {
        ChatRoomJoinEntity chatRoomJoinEntity = new ChatRoomJoinEntity();
        chatRoomJoinEntity.setChatRoomEntity(chatRoomEntity); // 채팅방 설정
        chatRoomJoinEntity.setUserProfileEntity(userProfileEntity); // 파티장
        return chatRoomJoinEntity;
    }

    public static ChatRoomJoinEntity create(ChatRoomEntity chatRoom, UserProfileEntity userProfile) {
        ChatRoomJoinEntity chatRoomJoin = new ChatRoomJoinEntity();
        chatRoomJoin.setChatRoomEntity(chatRoom); // 채팅방 설정
        chatRoomJoin.setUserProfileEntity(userProfile); // 파티장
        return chatRoomJoin;
    }
}