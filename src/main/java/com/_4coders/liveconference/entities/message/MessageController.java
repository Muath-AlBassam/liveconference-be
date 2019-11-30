package com._4coders.liveconference.entities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
}
