package com._4coders.liveconference.entities.role.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/system_role")
public class SystemRoleController {

    @Autowired
    private SystemRoleService systemRoleService;
}
