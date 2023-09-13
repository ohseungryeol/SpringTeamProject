
package com.example.backend.service.chat;


import com.example.backend.domain.article.ArticleEntity;
import com.example.backend.domain.chat.*;
import com.example.backend.domain.user.UserProfileEntity;
import com.example.backend.handler.ex.CustomApiNotFoundException;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.repository.article.ArticleRepo;
import com.example.backend.repository.chat.ChatMessageRepo;
import com.example.backend.repository.chat.ChatRoomJoinRepo;
import com.example.backend.repository.chat.ChatRoomRepo;
import com.example.backend.web.dto.chat.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepo chatRoomRepo;
    private final ChatRoomJoinRepo chatRoomJoinRepo;
    private final UserProfileRepo userProfileRepo;
    private final ChatMessageRepo chatMessageRepo;
    private final ArticleRepo articleRepo;

    // 내가 가진 채팅방 가져오기
    public List<String> findAllRoom(String profileId) {
        UserProfileEntity user = userProfileRepo.getById(profileId);
        List<ChatRoomJoinEntity> joinList = user.getChatRoomJoinList();
        List<String> chatRoomList = new ArrayList<>();
        for (ChatRoomJoinEntity chatRoomJoin : joinList) {
            // 객체로 넣으며 안됨 -> 무한참조
            chatRoomList.add(chatRoomJoin.getChatRoomEntity().getId());
        }
        return chatRoomList;
    }

    // 채팅방 이름 가져오기
    public ChatRoomDto findRoomByRoomID(String roomId) {
        ChatRoomEntity findChatRoom = chatRoomRepo.getById(roomId);
        ChatRoomDto chatRoomDto = new ChatRoomDto(findChatRoom);
        return chatRoomDto;
    }

    // 채팅방 생성하기
    public ChatRoomEntity createChatRoom(Long articleId, String articleTitle) {
        ArticleEntity articleEntity = articleRepo.findById(articleId).orElseThrow(() -> {
            throw new CustomApiNotFoundException("게시판을 찾지 못했습니다!");
        });
        ChatRoomEntity chatRoom = ChatRoomEntity.create(articleId, articleTitle);

        return chatRoom;
    }

    // 채팅-유저 조인 생성 (article에서) + 채팅방 입장(enterChatRoom)
    public ChatRoomJoinEntity createChatRoomJoin(ChatRoomEntity chatRoom, UserProfileEntity user){
        ChatRoomJoinEntity chatRoomJoin = ChatRoomJoinEntity.create(chatRoom,user);

        return chatRoomJoin;
    }


    public void deleteChatRoomJoin(ChatRoomEntity chatRoom, UserProfileEntity userProfile) {
        chatRoomJoinRepo.deleteChatRoomJoinByChatRoomEntityAndUserProfileEntity(chatRoom, userProfile);
    }
}

