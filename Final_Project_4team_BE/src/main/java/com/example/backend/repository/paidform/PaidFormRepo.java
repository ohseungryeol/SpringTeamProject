package com.example.backend.repository.paidform;

import com.example.backend.domain.paidform.PaidFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidFormRepo extends JpaRepository<PaidFormEntity, Long> {
}
