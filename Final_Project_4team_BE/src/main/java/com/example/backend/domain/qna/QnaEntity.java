package com.example.backend.domain.qna;

import com.example.backend.domain.user.UserProfileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Struct;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class QnaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfileEntity userProfileEntity;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false)
    @Setter
    private String content;

    @Setter
    private String comment;

    @Setter
    private String category;

    @ElementCollection
    @Setter
    private List<String> pic = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    protected QnaEntity(Long id, UserProfileEntity userProfileEntity, String title, String content, String comment, String category, List<String> pic, LocalDateTime createdAt) {
        this.id = id;
        this.userProfileEntity = userProfileEntity;
        this.title = title;
        this.content = content;
        this.comment = comment;
        this.category = category;
        this.pic = pic;
        this.createdAt = createdAt;
    }

    //== 생성 메서드 ==//
    public static QnaEntity createQnaEntity(UserProfileEntity user, String title, String content, String category, List<String> pic) {
        if(pic == null) pic = new ArrayList<>();
        return QnaEntity.builder()
                .userProfileEntity(user)
                .title(title)
                .content(content)
                .category(category)
                .pic(pic)
                .build();
    }
}
