package com.example.backend.repository.review;


import com.example.backend.domain.review.ReviewEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByToUserOrderByDateDesc(UserProfileEntity userProfile);
    List<ReviewEntity> findByFromUserOrderByDateDesc(UserProfileEntity userProfile);
}
