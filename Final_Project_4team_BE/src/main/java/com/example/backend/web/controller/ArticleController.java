package com.example.backend.web.controller;

import com.example.backend.service.article.ArticleService;
import com.example.backend.web.dto.article.ArticleAndPartyRequestDto;
import com.example.backend.web.dto.article.ArticleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Tag(name = "게시글 컨트롤러")
public class ArticleController {

    // 의존성
    private final ArticleService articleService;


    // Create - 게시글 생성
    @Operation(summary = "게시글 작성", description = "게시글을 작성하고 파티를 자동으로 생성하여 맵핑한다. 성공 여부가 success 에 저장된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글이 작성되었을 때 반환됩니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 입력 데이터가 누락된 경우 반환됩니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생한 경우 반환됩니다.")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> insertArticle(
            @Valid @RequestBody @Parameter(description = "게시글과 파티에 대한 정보", required = true) ArticleAndPartyRequestDto articleAndPartyRequestDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
        try {
            ArticleResponseDto articleResponseDto = articleService.createArticle(articleAndPartyRequestDto);

            httpStatus = HttpStatus.OK;
            result.put("article", articleResponseDto);
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<>(result, httpStatus);
    }

    // read all - 게시글 리스트 조회
    @Operation(summary = "게시글 리스트 조회", description = "게시글 리스트를 쿼리스트링으로 받은 옵션에 따라 불러온다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글 리스트가 조회되었을 때 반환됩니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 입력 데이터가 누락된 경우 반환됩니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생한 경우 반환됩니다.")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getArticleList(
            @RequestParam(required = false) @Parameter(description = "카테고리 정보") String category,
            @RequestParam(required = false) @Parameter(description = "범위 정보") String range,
            @RequestParam(required = false) @Parameter(description = "검색어") String keyword,
            @RequestParam @Parameter(description = "프로필 아이디") String profileId) {
        Map<String, Object> result = new HashMap<>();
        List<ArticleResponseDto> articleList = null;
        HttpStatus httpStatus;

        try {
            articleList = articleService.getArticleList(profileId, category, range, keyword);
            httpStatus = HttpStatus.OK;
            result.put("success", true);

        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);

        }

        result.put("articleList", articleList);

        return new ResponseEntity<>(result, httpStatus);
    }

    // read one - 게시글 단일 조회
    @Operation(summary = "게시글 단일 조회", description = "게시글 번호에 해당하는 게시글을 불러온다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글이 조회되었을 때 반환됩니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 입력 데이터가 누락된 경우 반환됩니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생한 경우 반환됩니다.")
    })
    @GetMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> getArticle(
            @PathVariable @Parameter(description = "게시글 번호", required = true) String articleId) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
        ArticleResponseDto article = null;
        try {
            article = articleService.getArticle(Long.parseLong(articleId));
            httpStatus = HttpStatus.OK;
            result.put("success", true);

        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);

        }
        result.put("article", article);
        return new ResponseEntity<>(result, httpStatus);
    }

    // update - 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글과 파티 정보를 수정한다. 성공 여부가 success 에 저장된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글이 수정되었을 때 반환됩니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 입력 데이터가 누락된 경우 반환됩니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생한 경우 반환됩니다.")
    })
    @PutMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @Valid @RequestBody @Parameter(description = "게시글과 파티에 대한 정보", required = true) ArticleAndPartyRequestDto articleAndPartyRequestDto,
            @PathVariable @Parameter(description = "수정할 게시글 아이디", required = true) String articleId) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
        try {
            ArticleResponseDto articleResponseDto = articleService.updateArticle(articleAndPartyRequestDto, Long.parseLong(articleId));

            httpStatus = HttpStatus.OK;
            result.put("article", articleResponseDto);
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<>(result, httpStatus);
    }

    // delete - 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글 번호에 해당하는 게시글을 삭제한다. 성공 여부가 success 에 저장된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 게시글이 삭제되었을 때 반환됩니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 입력 데이터가 누락된 경우 반환됩니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생한 경우 반환됩니다.")
    })
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> deleteArticle(
            @PathVariable @Parameter(description = "삭제할 게시글 번호", required = true) String articleId) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
        try {
            articleService.deleteArticle(Long.parseLong(articleId));

            httpStatus = HttpStatus.OK;
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<>(result, httpStatus);
    }

}
