package com.example.backend.domain.chat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_message")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="msg_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @NotNull
    private String content;

    @NotNull
    private String sender;

    @NotNull
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoomEntity chatRoomEntity;

    public static ChatMessageEntity createChatMessage(ChatRoomEntity chatRoomEntity, MessageType type, String content, String sender, String time){
        ChatMessageEntity chatMessage = ChatMessageEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .type(type)
                .content(content)
                .sender(sender)
                .time(time)
                .build();
        return chatMessage;
    }

    // 입장, 퇴장 메시지 변경을 위해
    public void setContent(String content){
        this.content=content;
    }

}
