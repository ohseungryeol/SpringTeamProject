
package com.example.backend.web.api.chat;

import com.example.backend.service.chat.ChatService;
import com.example.backend.web.dto.CMRespDto;
import com.example.backend.web.dto.chat.ChatMessageDetailDto;
import com.example.backend.web.dto.chat.ChatMessageDto;
import com.example.backend.web.dto.chat.ChatRoomDto;
import com.example.backend.web.dto.chat.PostChatRoomReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // 내 채팅방 목록 띄우기
    @GetMapping("/room/{profileId}")
    public ResponseEntity<Map<String, Object>> rooms(@PathVariable String profileId) {
        Map<String, Object> result = new HashMap<>();
        List<String> roomIdList = null;
        List<ChatRoomDto> roomList = new ArrayList<>();
        HttpStatus httpStatus = null;

        try {
            roomIdList = chatService.findAllRoom(profileId);
            for (String roomId : roomIdList) {
                roomList.add(chatService.findRoomByRoomID(roomId));
            }
            httpStatus = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("roomList", roomList);

        return new ResponseEntity<>(result, httpStatus);
    }

    // 채팅방 입장
    @GetMapping("/room/enter/{roomId}")
    public ResponseEntity<Map<String, Object>> roomDetail(@PathVariable String roomId) {
        Map<String, Object> result = new HashMap<>();
        ChatRoomDto chatRoom = null;
        HttpStatus httpStatus = null;

        try {
            // 방들어가기
            chatRoom = chatService.findRoomByRoomID(roomId);
            System.out.println("roomName: " + chatRoom.getName());
            // 과거 채팅 내역 보여주기

            httpStatus = HttpStatus.OK;
        } catch (RuntimeException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result.put("chatRoom", chatRoom);

        return new ResponseEntity<>(result, httpStatus);
    }
}

