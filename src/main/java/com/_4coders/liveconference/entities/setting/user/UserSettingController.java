package com._4coders.liveconference.entities.setting.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/user_setting")
public class UserSettingController {

    @Autowired
    private UserSettingService userSettingService;
}
