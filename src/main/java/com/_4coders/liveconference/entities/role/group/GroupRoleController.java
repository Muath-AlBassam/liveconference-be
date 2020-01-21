package com._4coders.liveconference.entities.role.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/group_role")
public class GroupRoleController {

    @Autowired
    private GroupRoleService groupRoleService;
}
