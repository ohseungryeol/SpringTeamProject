package com.example.backend.service.article;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.chat.ChatRoomEntity;
import com.example.backend.domain.chat.ChatRoomJoinEntity;
import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.chat.ChatRoomJoinRepo;
import com.example.backend.repository.chat.ChatRoomRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.repository.article.MemberInfoRepo;
import com.example.backend.repository.article.PartyRepo;
import com.example.backend.service.chat.ChatService;
import com.example.backend.web.dto.article.MemberInfoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberInfoService {
    private final MemberInfoRepo memberInfoRepo;
    private final PartyRepo partyRepo;
    private final UserProfileRepo userProfileRepo;
    private final ChatRoomRepo chatRoomRepo;
    private final ChatRoomJoinRepo chatRoomJoinRepo;
    private final ChatService chatService;
    private final ArticleRepo articleRepo;

    //파티참여 로직
    //파티참여 로직
    public void insertMemberInfo(MemberInfoRequestDto memberInfoRequestDto) {
        //회원찾기
        UserProfileEntity userProfile = userProfileRepo.getById(memberInfoRequestDto.getProfileId());
        //파티찾기
        PartyEntity party=partyRepo.getById(memberInfoRequestDto.getPartyId());
        //에러체크 -> 파티에 모인 수량 + 참여자가 선택한 수량이
        if(party.getTotalRecruitMember()<party.getCurrentRecruitMember()+memberInfoRequestDto.getAmount()){
            throw new RuntimeException("파티에 참가할 수 없습니다.");
        }
        //해당 파티 인원수 선택 수량만큼 + 해주기
        party.setCurrentRecruitMember(party.getCurrentRecruitMember()+memberInfoRequestDto.getAmount());

        partyRepo.save(party);
        //memberinfo에 저장
        MemberInfoEntity memberInfo=MemberInfoEntity.builder()
                .userProfileEntity(userProfile)
                .partyEntity(party)
                .paidMethod(memberInfoRequestDto.getPaidMethod())
                .price(memberInfoRequestDto.getPrice())
                .amount(memberInfoRequestDto.getAmount())
                .build();
        memberInfoRepo.save(memberInfo);

        // 채팅방 입장
        List<ChatRoomEntity> chatRoomList = chatRoomRepo.findChatRoomEntityByArticleId(party.getArticleEntity().getId());
        ChatRoomEntity chatRoom = chatRoomList.get(0);
        chatRoom.setCount(chatRoom.getCount()+1);
        ChatRoomJoinEntity chatRoomJoin = chatService.createChatRoomJoin(chatRoom, userProfile);
        chatRoomJoinRepo.save(chatRoomJoin);

    }
    //참가 취소

    public void deleteMemberInfo(Long partyId,String profileId) {
        UserProfileEntity userProfile=userProfileRepo.getById(profileId);

        List<MemberInfoEntity> memberInfos=memberInfoRepo.findAllByUserProfileEntity(userProfile);
        PartyEntity party=partyRepo.getById(partyId);

        for (MemberInfoEntity memberInfo : memberInfos) {
            if (memberInfo.getPartyEntity().getId() == partyId) {
                party.setCurrentRecruitMember(party.getCurrentRecruitMember() - memberInfo.getAmount());
                memberInfoRepo.deleteById(memberInfo.getId());
            }
        }


        // 채팅방 퇴장
        ArticleEntity article = articleRepo.findByPartyEntity(party);
        List<ChatRoomEntity> chatRoomList = chatRoomRepo.findChatRoomEntityByArticleId(article.getId());
        chatRoomList.get(0).setCount(chatRoomList.get(0).getCount()-1);
        chatRoomJoinRepo.deleteChatRoomJoinByChatRoomEntityAndUserProfileEntity(chatRoomList.get(0), userProfile);

    }
    //멤버 구매확정여부
    public void confirmMemberInfo(Long partyId,String profileId) {
        UserProfileEntity userProfile=userProfileRepo.getById(profileId);

        List<MemberInfoEntity> memberInfos=memberInfoRepo.findAllByUserProfileEntity(userProfile);

        for (MemberInfoEntity memberInfo : memberInfos) {
            if (memberInfo.getPartyEntity().getId() == partyId) {
                memberInfo.setIsConfirmed(true);
            }
        }
    }
}