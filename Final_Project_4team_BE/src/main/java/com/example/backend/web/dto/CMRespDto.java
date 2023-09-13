package com.example.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공통 응답 DTO
 * 응답의 일관성을 위해 사용하면 좋음
 * (굳이 사용 안해도 상관없음)
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMRespDto<T> {
    private int code;
    private String message;
    private T data;
}
