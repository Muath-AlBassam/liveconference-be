package com._4coders.liveconference.entities.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;
}
