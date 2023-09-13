package com.example.backend.domain.user;


import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.chat.ChatRoomJoinEntity;
import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.qna.QnaEntity;
import com.example.backend.domain.review.ReviewEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import com.example.backend.web.dto.user.UserProfileDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEntity implements Serializable {

    @Id
    @Column(name = "profile_id")
    private String id;

    @NotNull
    private String address;

    @NotNull
    private Double lng;

    @NotNull
    private Double lat;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private LocalDateTime joinDate;

    private String pic;

    private Integer score;

    private Integer cnt;

    public UserProfileEntity(UserProfileDto userProfileDto) {
        this.id = userProfileDto.getId();
        this.address = userProfileDto.getAddress();
        this.nickname = userProfileDto.getNickname();
        this.joinDate = userProfileDto.getJoinDate();
        this.pic = userProfileDto.getPic();
        this.score = userProfileDto.getScore();
    }

    @OneToMany(mappedBy = "userProfileEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberInfoEntity> memberInfos = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", unique = true)
    private UserAuthEntity userAuthEntity;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewEntity> reviewsFrom = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewEntity> reviewsTo = new ArrayList<>();

    @OneToMany(mappedBy = "userProfileEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<QnaEntity> qnas = new ArrayList<>();

    @OneToMany(mappedBy = "userProfileEntity")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "userProfileEntity")
    private List<ArticleEntity> articles = new ArrayList<>();

    @OneToMany(mappedBy = "userProfileEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatRoomJoinEntity> chatRoomJoinList = new ArrayList<>();

    @OneToMany(mappedBy = "userProfileEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ZzimEntity> zzims = new ArrayList<>();

}