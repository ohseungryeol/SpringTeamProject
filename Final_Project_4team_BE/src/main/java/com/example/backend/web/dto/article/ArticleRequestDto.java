package com.example.backend.web.dto.article;

import com.example.backend.domain.article.Category;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Data
public class ArticleRequestDto {
    // 작성
    private String title;
    private String content;
    private String link;
    private Category category;
    @ElementCollection
    private List<String> pic;
}
