package com.example.backend.domain.article;

import com.example.backend.domain.chat.ChatRoomEntity;
import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    //@NotNull
    private LocalDateTime createdAt;

    @NotNull
    @Column(length = 1000)
    private String link;

    @ElementCollection
    private List<String> pic;


    // 참조 영역

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id") //프로필=유저
    private UserProfileEntity userProfileEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", unique = true)
    private PartyEntity partyEntity;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ZzimEntity> zzims = new ArrayList<>();

    //== 연관 관계 메서드 ==//

//    public void setChatRoomEntity(ChatRoomEntity chatRoomEntity) {
//        this.chatRoomEntity = chatRoomEntity;
//        chatRoomEntity.setArticleEntity(this);
//    }

}
