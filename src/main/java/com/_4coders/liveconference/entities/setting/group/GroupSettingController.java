package com._4coders.liveconference.entities.setting.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/group_setting")
public class GroupSettingController {

    @Autowired
    private GroupSettingService groupSettingService;
}
