package com._4coders.liveconference.entities.setteing.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingService {

    @Autowired
    private UserSettingRepository userSettingRepository;
}
