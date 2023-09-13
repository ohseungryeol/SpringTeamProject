package com.example.backend.web.controller;

import com.example.backend.service.comment.CommentService;
import com.example.backend.web.dto.comment.CommentRequestDto;
import com.example.backend.web.dto.comment.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "댓글 컨트롤러")
public class CommentController {

    // 의존성
    private final CommentService commentService;


    // create - 댓글 작성
    @Operation(summary = "댓글 작성", description = "게시글 내에 댓글을 작성한다.")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "500", description = "댓글 작성 실패")
    })
    public ResponseEntity<String> createComment(
            @Parameter(description = "댓글 작성 모델", required = true)
            @RequestBody CommentRequestDto commentRequestDto
            ) {
        HttpStatus status;
        try {
            commentService.createComment(commentRequestDto);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

    // read comments of article - 게시글 내 댓글 조회
    @Operation(summary = "게시글 내 댓글 조회", description = "게시글 내의 모든 댓글을 조회한다.")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 내 댓글 조회 성공"),
            @ApiResponse(responseCode = "500", description = "게시글 내 댓글 조회 실패")
    })
    public ResponseEntity<Map<String, Object>> readCommentList(
            @Parameter(description = "게시글 번호", required = true)
            @RequestParam("articleId") Long articleId
    ) {
        Map<String, Object> result = new HashMap<>();
        List<CommentResponseDto> commentList = null;
        HttpStatus status;
        try {
            commentList = commentService.readArticleCommentList(articleId);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("commentList", commentList);

        return new ResponseEntity<>(result, status);
    }

    // read my comments - 내가 작성한 댓글 조회
    @Operation(summary = "내가 작성한 댓글 조회", description = "내가 작성한 모든 댓글을 조회한다.")
    @GetMapping("/{profileId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 작성한 댓글 조회 성공"),
            @ApiResponse(responseCode = "500", description = "내가 작성한 댓글 조회 실패")
    })
    public ResponseEntity<Map<String, Object>> readMyCommentList(
            @Parameter(description = "내 프로필아이디", example = "Example User", required = true)
            @PathVariable("profileId") String profileId
    ) {
        Map<String, Object> result = new HashMap<>();
        List<CommentResponseDto> myCommentList = null;
        HttpStatus status;
        try {
            myCommentList = commentService.readMyCommentList(profileId);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("myCommentList", myCommentList);

        return new ResponseEntity<>(result, status);
    }

    // update - 댓글 수정
    @Operation(summary = "댓글 수정", description = "게시글 내의 내가 쓴 댓글을 수정한다.")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "500", description = "댓글 수정 실패")
    })
    public ResponseEntity<String> updateComment(
            @Parameter(description = "댓글 수정 모델")
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        HttpStatus status;
        try {
            commentService.updateComment(commentRequestDto);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

    // delete - 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "게시글 내의 내가 쓴 댓글을 삭제한다.")
    @DeleteMapping("/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "500", description = "댓글 삭제 실패")
    })
    public ResponseEntity<String> deleteComment(
            @Parameter(description = "댓글 번호", required = true)
            @PathVariable("commentId") Long commentId
    ) {
        HttpStatus status;
        try {
            commentService.deleteComment(commentId);
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

}
