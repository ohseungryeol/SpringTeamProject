package com.example.backend.service.User;

import com.example.backend.web.dto.user.UserProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserProfileDto login(String userAuthId);
    UserProfileDto register(UserProfileDto userProfileDto);
    String upload(MultipartFile uploadFile) throws Exception;
    UserProfileDto update(UserProfileDto userProfileDto);
    void delete(UserProfileDto userProfileDto);

    UserProfileDto getUserProfileInfo(String profileId);
}