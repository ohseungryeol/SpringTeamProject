package com.example.backend.repository.article;


import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import com.example.backend.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberInfoRepo extends JpaRepository<MemberInfoEntity,Long> {
    List<MemberInfoEntity> findAllByUserProfileEntityOrderByPartyEntityDesc(UserProfileEntity userProfile); //userprofile아이디로 찾기
    List<MemberInfoEntity> findAllByUserProfileEntity(UserProfileEntity userProfile);
    List<MemberInfoEntity> findAllByPartyEntity(PartyEntity party);
}
