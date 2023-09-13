package com.example.backend.web.dto.zzim;

import com.example.backend.domain.zzim.ZzimEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZzimDto {
    private String profileId;
    private Long articleId;

    public ZzimDto(ZzimEntity zzim) {
        this.profileId = zzim.getUserProfileEntity().getId();
        this.articleId = zzim.getArticleEntity().getId();
    }
}
