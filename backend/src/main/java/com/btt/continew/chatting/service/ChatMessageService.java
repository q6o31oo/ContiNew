package com.btt.continew.chatting.service;

import com.btt.continew.chatting.controller.dto.request.ChatMessageRequest;
import com.btt.continew.chatting.controller.dto.response.ChatMessagesResponse;
import com.btt.continew.chatting.domain.ChatMessage;
import com.btt.continew.chatting.domain.ChatRoom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatMessage> opsHashChatMessage;
    private HashOperations<String, String, ChatRoom> opsHasChatRoom;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
        opsHasChatRoom = redisTemplate.opsForHash();
    }


    @Transactional
    public void createMessage(ChatMessageRequest request) {

        ChatMessage chatMessage = ChatMessage.create(request);


        ChatRoom chatRoom = opsHasChatRoom.get(CHAT_ROOMS, request.getRoomId());
        chatRoom.setLastMessage(request.getContent());
        chatRoom.setLastMessageTime(LocalDateTime.now());
        opsHasChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);


        opsHashChatMessage.put(CHAT_MESSAGE, chatMessage.getId(), chatMessage);
    }

    @Transactional
    public ChatMessagesResponse showChatMessage(String roomId,Pageable pageable) {

        List<ChatMessage> temps = opsHashChatMessage.values(CHAT_MESSAGE);

        List<ChatMessage> chatMessageList = new ArrayList<>();

        for (ChatMessage temp : temps){

            if(temp.getRoomId().equals(roomId)){
                chatMessageList.add(temp);
            }
        }

        chatMessageList.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());

        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()), chatMessageList.size());
        Page<ChatMessage> chatMessages = new PageImpl<>(chatMessageList.subList(start,end),pageable,chatMessageList.size());

        return ChatMessagesResponse.from(chatMessages);
    }
}
