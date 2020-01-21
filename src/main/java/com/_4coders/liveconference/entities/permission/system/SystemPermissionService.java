package com._4coders.liveconference.entities.permission.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemPermissionService {
    @Autowired
    private SystemPermissionRepository systemPermissionRepository;

}
