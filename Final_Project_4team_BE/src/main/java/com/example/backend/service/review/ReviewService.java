package com.example.backend.service.review;

import com.example.backend.domain.review.ReviewEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.repository.review.ReviewRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.web.dto.review.ReviewRequestDto;
import com.example.backend.web.dto.review.ReviewResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final UserProfileRepo userProfileRepo;

    public List<ReviewResponseDto> getReviewList(String profileId) {

        UserProfileEntity user = userProfileRepo.getById(profileId);
        List<ReviewEntity> reviews = reviewRepo.findByToUserOrderByDateDesc(user);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }


    public List<ReviewResponseDto> getMyReviewList(String profileId) {

        UserProfileEntity user = userProfileRepo.getById(profileId);
        List<ReviewEntity> reviews = reviewRepo.findByFromUserOrderByDateDesc(user);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }


    public void insertReview(ReviewRequestDto reviewRequestDto) {
        ReviewEntity review = reviewRequestDto.toEntity();
        review.setDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        review.setFromUser(userProfileRepo.getById(reviewRequestDto.getFromUser()));

        UserProfileEntity toUser = userProfileRepo.getById(reviewRequestDto.getToUser());
        review.setToUser(toUser);
        toUser.setScore(toUser.getScore() + review.getScore());
        toUser.setCnt(toUser.getCnt()+1);
        reviewRepo.save(review);
    }

    public void updateReview(Long reviewId, ReviewRequestDto reviewRequestDto) {
        ReviewEntity review = reviewRepo.getById(reviewId);
        review.setContent(reviewRequestDto.getContent());
        review.setScore(reviewRequestDto.getScore());
    }


    public void deleteReview(Long reviewId) {
        ReviewEntity review=reviewRepo.getById(reviewId);
        UserProfileEntity toUser=review.getToUser();
        toUser.setScore(toUser.getScore() - review.getScore());
        toUser.setCnt(toUser.getCnt()-1);
        reviewRepo.deleteById(reviewId);
    }
}
