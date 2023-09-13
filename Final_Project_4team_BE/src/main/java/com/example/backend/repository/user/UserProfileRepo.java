package com.example.backend.repository.user;

import com.example.backend.domain.user.UserAuthEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, String> {

    UserProfileEntity findByUserAuthEntity(UserAuthEntity userAuthEntity);

    UserProfileEntity findByNickname(String Nickname);

}
