package com.example.backend.web.controller;

import com.example.backend.service.review.ReviewService;
import com.example.backend.web.dto.review.ReviewRequestDto;
import com.example.backend.web.dto.review.ReviewResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{profileId}")
    public ResponseEntity<Map<String, Object>> getReviewList(@PathVariable("profileId") String profileId) {
        Map<String, Object> result = new HashMap<>();
        List<ReviewResponseDto> reviewList = null;
        HttpStatus httpStatus = null;
        try {
            reviewList = reviewService.getReviewList(profileId);
            httpStatus = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("reviewList", reviewList);
        return new ResponseEntity<Map<String, Object>>(result, httpStatus);
    }

    @GetMapping("/to/{profileId}")
    public ResponseEntity<Map<String, Object>> getMyReviewList(@PathVariable("profileId") String profileId) {
        Map<String, Object> result = new HashMap<>();
        List<ReviewResponseDto> reviewList = null;
        HttpStatus httpStatus = null;
        try {
            reviewList = reviewService.getMyReviewList(profileId);
            httpStatus = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("reviewList", reviewList);
        return new ResponseEntity<Map<String, Object>>(result, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> insertReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus = null;
        try {
            reviewService.insertReview(reviewRequestDto);
            httpStatus = HttpStatus.OK;
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, httpStatus);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Map<String, Object>> updateReview(@PathVariable("reviewId") Long reviewId,
                                                            @RequestBody ReviewRequestDto reviewRequestDto) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus = null;
        try {
            reviewService.updateReview(reviewId, reviewRequestDto);
            httpStatus = HttpStatus.OK;
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, httpStatus);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Map<String, Object>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus = null;
        try {
            reviewService.deleteReview(reviewId);
            httpStatus = HttpStatus.OK;
            result.put("success", true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("success", false);
        }
        return new ResponseEntity<Map<String, Object>>(result, httpStatus);
    }
}
