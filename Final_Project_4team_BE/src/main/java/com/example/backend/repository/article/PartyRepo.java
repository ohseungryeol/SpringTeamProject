package com.example.backend.repository.article;


import com.example.backend.domain.memberInfo.MemberInfoEntity;
import com.example.backend.domain.party.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepo extends JpaRepository<PartyEntity,Long> {
    PartyEntity findAllByMemberInfos(MemberInfoEntity memberInfo); //memberInfo에 해당하는 파티 찾기
}
