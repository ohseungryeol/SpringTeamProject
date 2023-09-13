package com.example.backend.service.article;

import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.domain.zzim.ZzimEntity;
import com.example.backend.repository.ZzimRepo;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.article.MemberInfoRepo;
import com.example.backend.repository.article.PartyRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.web.dto.party.PartyDto;
import com.example.backend.web.dto.party.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyService {
    private final PartyRepo partyRepo;
    private final UserProfileRepo userProfileRepo;
    private final MemberInfoRepo memberInfoRepo;
    private final ArticleRepo articleRepo;
    private final ZzimRepo zzimRepo;

    //    내 파티리스트 전체 조회

    public List<PartyResponseDto> getPartyList(String id) {
        //1.받은 userProfile로 memberinfo찾기
        UserProfileEntity userProfile=userProfileRepo.getById(id); //프로필 1개 나옴
        List<MemberInfoEntity>memberInfos=memberInfoRepo.findAllByUserProfileEntityOrderByPartyEntityDesc(userProfile); //배열 3개일듯

        //2.그 memberInfos에 해당하는 파티들을 찾아서 parties에 넣어주기
        List<PartyResponseDto> parties=new ArrayList<>();


        for(MemberInfoEntity memberInfo:memberInfos){
            PartyEntity party=memberInfo.getPartyEntity();
            PartyResponseDto partyResponseDto=new PartyResponseDto(party,memberInfo);
            parties.add(partyResponseDto);
        }
        return parties;
    }

    //파티리스트 디테일 리스트 조회
    public List<PartyDto> getDetailList(Long id) {
        List<PartyDto>partyDtos=new ArrayList<>();
        //1.해당 파티 찾기
        PartyEntity party=partyRepo.getById(id);
        //2.파티에 해당하는 memberinfo 리스트찾기
        List<MemberInfoEntity>memberInfos=memberInfoRepo.findAllByPartyEntity(party);
        //3.memberinfo에 해당하는 user정보를 찾고 dto에 넣어주기 + party +memberinfo
        for(MemberInfoEntity memberInfo:memberInfos){
            UserProfileEntity userProfile=memberInfo.getUserProfileEntity();
            PartyDto partyDto=new PartyDto(userProfile,party,memberInfo);
            partyDtos.add(partyDto);
        }
        return partyDtos;
    }


    public void deleteParty(Long partyId) {
        PartyEntity party=partyRepo.getById(partyId); //파티 찾고

        List<MemberInfoEntity>memberInfos=memberInfoRepo.findAllByPartyEntity(party);
        ArticleEntity article=articleRepo.findByPartyEntity(party);

        for (MemberInfoEntity memberInfo : memberInfos) {

            memberInfoRepo.deleteById(memberInfo.getId());
        }
        List<ZzimEntity> Zzims=zzimRepo.findByArticleEntity(article);
        //파티원들 파티 삭제 시키기
        for (ZzimEntity zzim : Zzims) {
            zzimRepo.deleteById(zzim.getZzimId());
        }
        articleRepo.deleteById(article.getId());

        partyRepo.deleteById(partyId);
    }

    public void finishParty(Long partyId) { //파티 마감
        PartyEntity party=partyRepo.getById(partyId);
        party.setIsClosed(true);
        partyRepo.save(party);
    }

}
