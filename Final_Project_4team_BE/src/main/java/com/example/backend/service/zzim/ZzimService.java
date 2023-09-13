package com.example.backend.service.zzim;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import com.example.backend.repository.ZzimRepo;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.web.dto.zzim.ZzimDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ZzimService {

    ZzimRepo zzimRepo;
    UserProfileRepo userProfileRepo;
    ArticleRepo articleRepo;


    public ZzimDto getZzim(String profileId, Long articleId) {
        ZzimEntity zzim = zzimRepo.getById(new ZzimEntity.ZzimId(profileId, articleId));
        System.out.println(zzim.getZzimId().getProfileId());
        return new ZzimDto(zzim);
    }


    public List<ZzimDto> getZzimList(String profileId) {
        UserProfileEntity userProfile = userProfileRepo.getById(profileId);
        return zzimRepo.findByUserProfileEntityOrderByArticleEntityDesc(userProfile).stream().map(ZzimDto::new).collect(Collectors.toList());
    }


    public List<ZzimDto> getZzimList(Long articleId) {
        ArticleEntity article = articleRepo.getById(articleId);

        return zzimRepo.findByArticleEntity(article).stream().map(ZzimDto::new).collect(Collectors.toList());
    }

    public ZzimDto insertZzim(String profileId, Long articleId) {
        ZzimEntity zzim = new ZzimEntity(userProfileRepo.getById(profileId), articleRepo.getById(articleId));
        zzim = zzimRepo.save(zzim);
        return new ZzimDto(zzim);
    }

    public void deleteZzim(String profileId, Long articleId) {
        zzimRepo.deleteById(new ZzimEntity.ZzimId(profileId, articleId));
    }
}
