package com._4coders.liveconference.entities.permission.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupPermissionService {

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;
}
