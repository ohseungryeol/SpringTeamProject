
package com.example.backend.web.api.chat;

import com.example.backend.domain.chat.ChatMessageEntity;
import com.example.backend.domain.chat.ChatRoomEntity;
import com.example.backend.repository.chat.ChatMessageRepo;
import com.example.backend.repository.chat.ChatRoomRepo;
import com.example.backend.repository.user.UserProfileRepo;
import com.example.backend.service.chat.ChatService;
import com.example.backend.web.dto.chat.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate template;
    private final ChatRoomRepo chatRoomRepo;
    private final UserProfileRepo userProfileRepo;
    private final ChatMessageRepo chatMessageRepo;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {

        // dto로 받아온 채팅 메시지 DB저장 및 전송
        ChatRoomEntity chatRoom = chatRoomRepo.getById(message.getRoomId());

        String content = message.getContent().trim(); // 공백제거
        // 내용이 비어있으면 DB 저장 및 전송 안함
        if(!content.equals("")){
            String time = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
            ChatMessageEntity chatMessage = ChatMessageEntity.createChatMessage(chatRoom, message.getType(), message.getContent(), message.getSender(), time);
            message.setTime(time);

            chatMessageRepo.save(chatMessage);

            template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }
    }

}

