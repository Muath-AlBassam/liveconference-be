package com._4coders.liveconference.entities.setting.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingService {

    @Autowired
    private UserSettingRepository userSettingRepository;
}
