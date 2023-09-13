package com.example.backend.service.article;


import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.article.Category;
import com.example.backend.domain.chat.ChatRoomEntity;
import com.example.backend.domain.chat.ChatRoomJoinEntity;
import com.example.backend.domain.comment.CommentEntity;
import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import com.example.backend.repository.ZzimRepo;
import com.example.backend.repository.comment.CommentRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.article.MemberInfoRepo;
import com.example.backend.repository.article.PartyRepo;
import com.example.backend.repository.chat.ChatRoomJoinRepo;
import com.example.backend.repository.chat.ChatRoomRepo;
import com.example.backend.service.chat.ChatService;
import com.example.backend.util.GeoLocationUtil;
import com.example.backend.web.dto.article.ArticleAndPartyRequestDto;
import com.example.backend.web.dto.article.ArticleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    // 의존성
    /*
        주석처리 되어있는 의존성은 나중에 해당 클래스 만들어지면
        주석 해제하여 의존성 부여할 예정 !!
     */
    private final ArticleRepo articleRepo;
    private final PartyRepo partyRepo;
    private final MemberInfoRepo memberInfoRepo;
    private final UserProfileRepo userProfileRepo;
    private final GeoLocationUtil geoLocationUtil;
    private final ChatRoomRepo chatRoomRepo;
    private final ChatRoomJoinRepo chatRoomJoinRepo;
    private final ChatService chatService;
    private final CommentRepo commentRepo;

    @Autowired
    private ZzimRepo zzimRepo;

    // Create - 게시글 생성 매서드
    @Transactional
    public ArticleResponseDto createArticle(ArticleAndPartyRequestDto articleAndPartyRequestDto){

        if (articleAndPartyRequestDto.getTotalRecruitMember() <= articleAndPartyRequestDto.getAmount()) {
            throw new RuntimeException("전체 인원수보다 선택 인원수가 더 많습니다.");
        } else {

            PartyEntity partyEntity = new PartyEntity();
            partyEntity.setDeadline(LocalDateTime.parse(articleAndPartyRequestDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")));
            partyEntity.setTotalRecruitMember(articleAndPartyRequestDto.getTotalRecruitMember());
            partyEntity.setProduct(articleAndPartyRequestDto.getProduct());
            partyEntity.setTotalPrice(articleAndPartyRequestDto.getTotalPrice());
            partyEntity.setTotalProductCount(articleAndPartyRequestDto.getTotalProductCount());
            partyEntity.setTotalRecruitMember(articleAndPartyRequestDto.getTotalRecruitMember());
            partyEntity.setPenalty(articleAndPartyRequestDto.getPenalty());
            partyEntity.setCurrentRecruitMember(articleAndPartyRequestDto.getAmount());
            partyEntity.setFormChecked(false);
            partyEntity.setIsClosed(false);
            partyEntity = partyRepo.save(partyEntity);


            ArticleEntity articleEntity = new ArticleEntity();

            UserProfileEntity userProfileEntity = userProfileRepo.getById(articleAndPartyRequestDto.getProfileId());
            articleEntity.setUserProfileEntity(userProfileEntity);
            articleEntity.setTitle(articleAndPartyRequestDto.getTitle());
            articleEntity.setContent(articleAndPartyRequestDto.getContent());
            articleEntity.setLink(articleAndPartyRequestDto.getLink());
            articleEntity.setPic(articleAndPartyRequestDto.getPic());
            articleEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            articleEntity.setCategory(articleAndPartyRequestDto.getCategory());
            articleEntity.setTag(null);
            articleEntity.setPartyEntity(partyEntity);
            articleEntity = articleRepo.save(articleEntity);


            MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
            memberInfoEntity.setIsBoss(true);
            memberInfoEntity.setAmount(articleAndPartyRequestDto.getAmount());
            int calprice = (int) (articleAndPartyRequestDto.getTotalPrice() / articleAndPartyRequestDto.getTotalRecruitMember()) * articleAndPartyRequestDto.getAmount();
            memberInfoEntity.setPrice(calprice);
            memberInfoEntity.setPartyEntity(partyEntity);
            memberInfoEntity.setUserProfileEntity(userProfileEntity);
            memberInfoRepo.save(memberInfoEntity);

            // ■ ChatRoom 관련
            // 이 함수 하나면 내부적으로 chatroom 을 생성하고 chatroomJoin 과 articleEntity 에 대한 연관관계를 맺어줍니다!
            ChatRoomEntity chatRoom = chatService.createChatRoom(articleEntity.getId(), articleEntity.getTitle());
            ChatRoomJoinEntity chatRoomJoin = chatService.createChatRoomJoin(chatRoom, userProfileEntity);
            chatRoomRepo.save(chatRoom);
            chatRoomJoinRepo.save(chatRoomJoin);

            return new ArticleResponseDto(articleEntity);
        }
    }

    // read all - 게시글 전체 조회
    public List<ArticleResponseDto> getArticleList(String profileId, String category, String range, String keyword) {
        List<ArticleEntity> articles = null;

        // 비로그인 상태일때는 전체범위 조회
        if (profileId.equals("null")) {
            if (category == null && keyword == null) {
                articles = articleRepo.findAllByUserProfileEntityIsNotNullOrderByCreatedAtDesc();
            } else if (keyword == null) {
                articles = articleRepo.findByUserProfileEntityIsNotNullAndCategoryOrderByCreatedAtDesc(Category.valueOf(category));
            } else if (category == null) {
                articles = articleRepo.findByUserProfileEntityIsNotNullAndTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword);
            } else {
                articles = articleRepo.findByKeywordAndCategory(keyword, category);
            }
            return articles.stream().map(ArticleResponseDto::new).collect(Collectors.toList());
        }

        Double[] lnglat = geoLocationUtil.parseLocationToLngLat(userProfileRepo.getById(profileId).getAddress());
        // 1. 검색 기준이 모두 없을때
        if (category == null && range == null && keyword == null) {
            articles = articleRepo.findAllByUserProfileEntityIsNotNullOrderByCreatedAtDesc();
        }
        // 3. 범위만 있을때
        else if (category == null && keyword == null) {
            articles = articleRepo.findByRange(lnglat, range);
        }
        // 4. 검색어만 있을때
        else if (category == null && range == null) {
            articles = articleRepo.findByUserProfileEntityIsNotNullAndTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword);
        }
        // 5. 카테고리, 범위
        else if (keyword == null) {
            articles = articleRepo.findByCategoryAndRange(category, lnglat, range);
        }
        // 6. 카테고리, 검색어
        else if (range == null) {
            articles = articleRepo.findByKeywordAndCategory(keyword, category);
        }
        // 7. 범위, 검색어
        else if (category == null) {
            articles = articleRepo.findByKeywordAndRange(keyword, lnglat, range);
        }
        //8. 전부있을때
        else {
            articles = articleRepo.findByAllCondition(keyword, category, lnglat, range);
        }
        return articles.stream().map(ArticleResponseDto::new).collect(Collectors.toList());
    }

    // read one - 게시글 단일 조회
    public ArticleResponseDto getArticle(Long articleId){
        /*
            GetReferenceById가 아닌 getById 사용한 이유
            JpaRepository 에서 GetReferenceById 메서드는
            EntityNotFoundException 을 던지지 않지만 객체가 존재하지 않을 때
            예외를 던지는 getById를 사용함.
         */
        ArticleEntity articleEntity = articleRepo.getById(articleId);
        ArticleResponseDto articleResponseDto = new ArticleResponseDto(articleEntity);

        return articleResponseDto;
    }

    // update - 게시글 수정
    @Transactional
    public ArticleResponseDto updateArticle(ArticleAndPartyRequestDto articleAndPartyRequestDto, Long articleId) {
        if (articleAndPartyRequestDto.getTotalRecruitMember() <= articleAndPartyRequestDto.getAmount()) {
            throw new RuntimeException("전체 인원수보다 선택 인원수가 더 많습니다.");
        } else {
            ArticleEntity article = articleRepo.getById(articleId);

            PartyEntity party = article.getPartyEntity();
            party.setDeadline(LocalDateTime.parse(articleAndPartyRequestDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")));
            party.setTotalRecruitMember(articleAndPartyRequestDto.getTotalRecruitMember());
            party.setProduct(articleAndPartyRequestDto.getProduct());
            party.setTotalPrice(articleAndPartyRequestDto.getTotalPrice());
            party.setTotalProductCount(articleAndPartyRequestDto.getTotalProductCount());
            party.setTotalRecruitMember(articleAndPartyRequestDto.getTotalRecruitMember());
            party.setPenalty(articleAndPartyRequestDto.getPenalty());
            party.setCurrentRecruitMember(articleAndPartyRequestDto.getAmount());
            party = partyRepo.save(party);

            article.setTitle(articleAndPartyRequestDto.getTitle());
            article.setContent(articleAndPartyRequestDto.getContent());
            article.setLink(articleAndPartyRequestDto.getLink());
            article.setPic(articleAndPartyRequestDto.getPic());
            article.setCategory(articleAndPartyRequestDto.getCategory());
            article.setTag(null);
            article.setPartyEntity(party);
            article = articleRepo.save(article);


            List<MemberInfoEntity> memberInfos = memberInfoRepo.findAllByPartyEntity(party);
            for (MemberInfoEntity memberInfo : memberInfos) {
                if (memberInfo.getIsBoss() == true) {
                    memberInfo.setAmount(articleAndPartyRequestDto.getAmount());
                    int calprice = (int) (articleAndPartyRequestDto.getTotalPrice() / articleAndPartyRequestDto.getTotalRecruitMember()) * articleAndPartyRequestDto.getAmount();
                    memberInfo.setPrice(calprice);
                    memberInfoRepo.save(memberInfo);
                }
            }
            return new ArticleResponseDto(article);
        }
    }

    // delete - 게시글 삭제
    @Transactional
    public void deleteArticle(Long articleId) {
        ArticleEntity articleEntity = articleRepo.getReferenceById(articleId);
        List<CommentEntity> comments = commentRepo.findByArticleEntity(articleEntity);
        List<ZzimEntity> Zzims = zzimRepo.findByArticleEntity(articleEntity);

        for (CommentEntity comment : comments) {
            commentRepo.deleteById(comment.getId());
        }
        for (ZzimEntity zzim : Zzims) {
            zzimRepo.deleteById(zzim.getZzimId());
        }

        articleRepo.deleteById(articleId);
    }
}
