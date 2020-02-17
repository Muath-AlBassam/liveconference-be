package com._4coders.liveconference.entities.setting.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupSettingService {

    @Autowired
    private GroupSettingRepository groupSettingRepository;
}
