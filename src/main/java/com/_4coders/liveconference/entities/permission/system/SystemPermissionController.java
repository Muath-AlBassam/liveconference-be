package com._4coders.liveconference.entities.permission.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/system_permission")
public class SystemPermissionController {
    @Autowired
    private SystemPermissionService systemPermissionService;

}
