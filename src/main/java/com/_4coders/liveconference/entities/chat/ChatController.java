package com._4coders.liveconference.entities.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;
}
