package com.example.backend.service.comment;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.comment.CommentRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.web.dto.comment.CommentRequestDto;
import com.example.backend.web.dto.comment.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    // 의존성
    private final CommentRepo commentRepo;
    private final ArticleRepo articleRepo;
    private final UserProfileRepo userProfileRepo;

    // create - 댓글 작성
    @Transactional
    public void createComment(CommentRequestDto commentRequestDto) {
        ArticleEntity articleEntity = articleRepo.getReferenceById(commentRequestDto.getArticleId());
        UserProfileEntity userProfileEntity = userProfileRepo.getReferenceById(commentRequestDto.getProfileId());
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentRequestDto.getContent());
        commentEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        commentEntity.setArticleEntity(articleEntity);
        commentEntity.setUserProfileEntity(userProfileEntity);
        commentRepo.save(commentEntity);
    }

    // read comments of article - 게시글 내 댓글 조회
    public List<CommentResponseDto> readArticleCommentList(Long articleId) {
        ArticleEntity articleEntity = articleRepo.getReferenceById(articleId);
        List<CommentEntity> comments = commentRepo.findByArticleEntity(articleEntity);
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // read my comments - 댓글 목록 조회(내가 작성한)
    public List<CommentResponseDto> readMyCommentList(String profileId) {
        UserProfileEntity userProfileEntity = userProfileRepo.getReferenceById(profileId);
        List<CommentEntity> comments = commentRepo.findByUserProfileEntityOrderByCreatedAtDesc(userProfileEntity);
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // update - 댓글 수정
    @Transactional
    public void updateComment(CommentRequestDto commentRequestDto) {
        CommentEntity commentEntity = commentRepo.getReferenceById(commentRequestDto.getId());
        commentEntity.setContent(commentRequestDto.getContent());
        commentEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        commentRepo.save(commentEntity);
    }

    // delete - 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

}
