package com._4coders.liveconference.entities.permission.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/group_permission")
public class GroupPermissionController {

    @Autowired
    private GroupPermissionService groupPermissionService;
}
