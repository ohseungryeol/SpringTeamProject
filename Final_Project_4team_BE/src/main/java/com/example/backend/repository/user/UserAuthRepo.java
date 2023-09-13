package com.example.backend.repository.user;

import com.example.backend.domain.user.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepo extends JpaRepository<UserAuthEntity,String> {


}