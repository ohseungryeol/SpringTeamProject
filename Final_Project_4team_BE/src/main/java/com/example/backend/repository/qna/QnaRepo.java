package com.example.backend.repository.qna;

import com.example.backend.domain.qna.QnaEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaRepo extends JpaRepository<QnaEntity, Long> {
    List<QnaEntity> findAllByUserProfileEntityOrderByIdDesc(UserProfileEntity userProfileEntity);
    List<QnaEntity> findAllByOrderByIdDesc();
}
