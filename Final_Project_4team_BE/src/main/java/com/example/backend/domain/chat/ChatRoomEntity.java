package com.example.backend.domain.chat;

import com.example.backend.domain.article.ArticleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor()
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomEntity implements Serializable{

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @Column(name="room_id")
    private String id;

    private String name;

    private Long articleId;

    private Integer count;

    @OneToMany(mappedBy = "chatRoomEntity")
    private List<ChatMessageEntity> chatMessageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoomEntity")
    private List<ChatRoomJoinEntity> chatRoomJoinList = new ArrayList<>();

    public static ChatRoomEntity create(Long articleId, String articleName) {
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = articleName;
        chatRoom.articleId = articleId;
        chatRoom.count = 1;
        return chatRoom;
    }

}
