package com.example.backend.service.qna;

import com.example.backend.domain.qna.QnaEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.handler.ex.CustomApiFileUploadException;
import com.example.backend.handler.ex.CustomApiNotFoundException;
import com.example.backend.handler.ex.CustomNoPermissionException;
import com.example.backend.repository.qna.QnaRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.service.s3.S3Service;
import com.example.backend.web.dto.CustomPage;
import com.example.backend.web.dto.qna.QnaDto;
import com.example.backend.web.dto.qna.ReqQnaCommentPutDto;
import com.example.backend.web.dto.qna.ReqQnaPostDto;
import com.example.backend.web.dto.qna.ReqQnaPutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final UserProfileRepo userProfileRepo;
    private final QnaRepo qnaRepo;
    private final S3Service s3Service;

    @Transactional
    public QnaDto saveQna(String userId, ReqQnaPostDto dto) {
        // 1. 해당 유저가 있는지 확인
        UserProfileEntity userEntity = userProfileRepo.findById(userId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. 이미지가 있으면 S3에 저장 후 이미지 url 받기
        List<String> picList = new ArrayList<>();
        if(dto.getPic() != null) {
            for(MultipartFile file : dto.getPic()) {
                try {
                    picList.add(s3Service.saveFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new CustomApiFileUploadException("파일 업로드에 실패하였습니다.");
                }
            }
        }

        // 3. 유저, 이미지 url, dto 값을 이용해서 Entity 생성
        QnaEntity qna = QnaEntity.createQnaEntity(userEntity, dto.getTitle(), dto.getContent(), dto.getCategory(), picList);

        // 4. 저장
        QnaEntity qnaEntity = qnaRepo.save(qna);

        // 5. QnaDto 로 변환 후 return
        return QnaDto.fromEntity(qnaEntity);
    }

    @Transactional(readOnly = true)
    public List<QnaDto> findAllByProfile(String profileId) {
        // 1. 유저 찾기
        UserProfileEntity userEntity = userProfileRepo.findById(profileId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. 유저가 쓴 글 불러오기
        List<QnaEntity> pageEntity;
        if(userEntity.getUserAuthEntity().getIsAdmin()) {
            pageEntity = qnaRepo.findAllByOrderByIdDesc();

        } else {
            pageEntity = qnaRepo.findAllByUserProfileEntityOrderByIdDesc(userEntity);
        }

        // 3. dto 로 변환 후 return
        List<QnaDto> page = new ArrayList<>();
        for(QnaEntity entity: pageEntity) {
            page.add(QnaDto.fromEntity(entity));
        }

        return page;
    }

    // 관리자 : 전체 보기
    @Transactional(readOnly = true)
    public List<QnaDto> findAll() {
        List<QnaEntity> pageEntity = qnaRepo.findAllByOrderByIdDesc();

        List<QnaDto> page = new ArrayList<>();
        for(QnaEntity entity: pageEntity) {
            page.add(QnaDto.fromEntity(entity));
        }

        return page;
    }

    // TODO: 파일 업데이트 부분 무조건 성능 개선 필요!
    @Transactional
    public QnaDto updateQna(String profileId, Long qnaId, ReqQnaPutDto dto) {
        // 1. 유저 찾기
        UserProfileEntity userEntity = userProfileRepo.findById(profileId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. qna 글 불러오기
        QnaEntity qnaEntity = qnaRepo.findById(qnaId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 Qna 를 찾지 못했습니다.");
        });

        // 3. 본인 확인
        if(!userEntity.getId().equals(qnaEntity.getUserProfileEntity().getId())) {
            throw new CustomNoPermissionException("Qna 를 수정할 권한이 없습니다.");
        }

        // 4. 수정
        if(!qnaEntity.getTitle().equals(dto.getTitle())) qnaEntity.setTitle(dto.getTitle());
        if(!qnaEntity.getContent().equals(dto.getContent())) qnaEntity.setContent(dto.getContent());
        if(!qnaEntity.getCategory().equals(dto.getCategory())) qnaEntity.setCategory(dto.getCategory());

        for(String url : qnaEntity.getPic()) {
            s3Service.deleteImage(url);
        }

        List<String> urlList = new ArrayList<>();
        for(MultipartFile file : dto.getPic()) {
            try {
                urlList.add(s3Service.saveFile(file));
            } catch (IOException e) {
                throw new CustomApiFileUploadException("파일 업로드에 실패하였습니다");
            }
        }
        qnaEntity.setPic(urlList);

        // 5. dto 변환 후 return
        return QnaDto.fromEntity(qnaEntity);
    }

    @Transactional()
    public void deleteQna(String profileId, Long qnaId) {
        // 1. 유저 불러오기
        UserProfileEntity userEntity = userProfileRepo.findById(profileId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. qna 게시판 불러오기
        QnaEntity qnaEntity = qnaRepo.findById(qnaId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 Qna 를 찾지 못했습니다.");
        });

        // 3, 유저가 쓴 글인지 or 관리자인지 확인하고 삭제
        if(!userEntity.getId().equals(qnaEntity.getUserProfileEntity().getId()) && !userEntity.getUserAuthEntity().getIsAdmin()) {
            throw new CustomNoPermissionException("Qna 를 삭제할 권한이 없습니다.");
        }
        for(String url : qnaEntity.getPic()) {
            s3Service.deleteImage(url);
        }
        qnaRepo.delete(qnaEntity);
    }

    @Transactional
    public QnaDto saveComment(String profileId, Long qnaId, ReqQnaCommentPutDto dto) {
        // 1. 유저 불러오기
        UserProfileEntity userEntity = userProfileRepo.findById(profileId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. qna 게시판 불러오기
        QnaEntity qnaEntity = qnaRepo.findById(qnaId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 Qna 를 찾지 못했습니다.");
        });

        // 3. admin 인지 확인
        if(!userEntity.getUserAuthEntity().getIsAdmin()) throw new CustomNoPermissionException("Qna 에 댓글을 추가할 권한이 없습니다.");

        // 4. 댓글 추가
        qnaEntity.setComment(dto.getComment());

        // 5. dto 로 변환 후 return
        return QnaDto.fromEntity(qnaEntity);
    }

    public QnaDto getQnaDetail(String profileId, Long qnaId) {
        // 1. 유저 불러오기
        UserProfileEntity userEntity = userProfileRepo.findById(profileId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 유저를 찾지 못했습니다");
        });

        // 2. qna 게시판 불러오기
        QnaEntity qnaEntity = qnaRepo.findById(qnaId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 Qna 를 찾지 못했습니다.");
        });

        if(!userEntity.getId().equals(qnaEntity.getUserProfileEntity().getId()) && !userEntity.getUserAuthEntity().getIsAdmin()) throw new CustomNoPermissionException("Qna글을 볼 자격이 없습니다");

        return QnaDto.fromEntity(qnaEntity);
    }

    public QnaDto getQnaDetail(Long qnaId) {
        QnaEntity qnaEntity = qnaRepo.findById(qnaId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("해당 Qna 를 찾지 못했습니다.");
        });

        return QnaDto.fromEntity(qnaEntity);
    }
}
