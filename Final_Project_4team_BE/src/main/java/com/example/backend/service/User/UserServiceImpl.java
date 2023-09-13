package com.example.backend.service.User;

import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.user.UserAuthEntity;
import com.example.backend.repository.comment.CommentRepo;
import com.example.backend.service.s3.S3Service;
import com.example.backend.util.HashEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.article.PartyRepo;
import com.example.backend.repository.chat.ChatRoomJoinRepo;
import com.example.backend.repository.user.UserAuthRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.util.GeoLocationUtil;
import com.example.backend.web.dto.user.UserProfileDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

//TODO import 필요
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final GeoLocationUtil geoLocationUtil;

    private final UserAuthRepo userAuthRepo;

    private final UserProfileRepo userProfileRepo;

    private final HashEncoder hashEncoder;

    private final ArticleRepo articleRepo;

    private final CommentRepo commentRepo;

    private final ChatRoomJoinRepo chatRoomJoinRepo;

    private final PartyRepo partyRepo;

    private final RestTemplate restTemplate;
    private final S3Service s3Service;


    @Override
    public UserProfileDto login(String userAuthId) {
        UserAuthEntity userAuth = userAuthRepo.getById(hashEncoder.encode(userAuthId));
        UserProfileEntity userProfileEntity = userProfileRepo.findByUserAuthEntity(userAuth);
        return userProfileEntity == null ? null : new UserProfileDto(userProfileEntity);
    }


    @Override
    public UserProfileDto register(UserProfileDto userProfileDto) {

        UserAuthEntity userAuth = new UserAuthEntity(hashEncoder.encode(userProfileDto.getId()), Boolean.FALSE, null);
        userAuth = userAuthRepo.save(userAuth);

        UserProfileEntity userProfile = new UserProfileEntity(userProfileDto);
        userProfile.setId(hashEncoder.encode(userAuth.getId())); // double hashed id
        userProfile.setUserAuthEntity(userAuth);
        userProfile.setJoinDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        Double[] lnglat = geoLocationUtil.parseLocationToLngLat(userProfile.getAddress());
        userProfile.setLng(lnglat[0]);
        userProfile.setLat(lnglat[1]);
        userProfile.setScore(0);
        userProfile.setCnt(0);
        userProfile.setPic(userProfileDto.getPic());

        userProfile = userProfileRepo.save(userProfile);

        return new UserProfileDto(userProfile);

    }

    @Override
    public String upload(MultipartFile uploadFile) throws Exception {

        return s3Service.saveFile(uploadFile);
        // 파일 정보
       /* String originFilename = uploadFile.getOriginalFilename(); //파일이름
        String extension = originFilename.substring(originFilename.length()-3); //확장자

        // 사진인지 체크
        if(!(extension.equals("jpg") || extension.equals("png")|| extension.equals("PNG")|| extension.equals("JPG"))){
            throw new FileUploadException("파일 확장자가 jpg나 png가 아닙니다.");
        }
        //파일이름 랜덤으로 만들기
        String url="/profile/";
        String saveFileName =UUID.randomUUID().toString() + originFilename.substring(originFilename.lastIndexOf(".")); //랜덤이름+확장자
        String saveFileName2=url+saveFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //파일 바이트
        ByteArrayResource fileAsResource = new ByteArrayResource(uploadFile.getBytes()){
            @Override
            public String getFilename() {
                return saveFileName2;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("uploadFile", fileAsResource); //파일 바이트 저장
        body.add("parentPath","profile");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = "http://i6c102.p.ssafy.io:3000/upload";

        ResponseEntity<String> response = restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);

        return "http://i6c102.p.ssafy.io/images"+saveFileName2;*/
    }

    @Override
    public UserProfileDto update(UserProfileDto userProfileDto) {

        UserProfileEntity user = userProfileRepo.getById(userProfileDto.getId()); // hashwoori
        user.setNickname(userProfileDto.getNickname());
        user.setAddress(userProfileDto.getAddress());
        Double[] lnglat = geoLocationUtil.parseLocationToLngLat(user.getAddress());
        user.setLng(lnglat[0]);
        user.setLat(lnglat[1]);
        user.setPic(userProfileDto.getPic());
        user = userProfileRepo.save(user);

        return new UserProfileDto(user);
    }

    @Override
    public void delete(UserProfileDto userProfileDto) {
        UserProfileEntity user = userProfileRepo.getById(userProfileDto.getId());

        /* 회원과 관련된 글 목록과 댓글의 연관관계 끊기, 나머지는 삭제 */
        List<ArticleEntity> articleList = articleRepo.findByUserProfileEntityIsNotNullAndUserProfileEntityOrderByCreatedAtDesc(user);
        for (ArticleEntity article : articleList) {

            article.setUserProfileEntity(null);
            articleRepo.save(article);
        }

        List<CommentEntity> commentList = commentRepo.findByUserProfileEntity(user);
        for (CommentEntity comment : commentList) {
            comment.setUserProfileEntity(null);
            commentRepo.save(comment);
        }

        userProfileRepo.deleteById(userProfileDto.getId());
    }

    @Override
    public UserProfileDto getUserProfileInfo(String profileId) {
        return new UserProfileDto(userProfileRepo.getById(profileId));
    }
}