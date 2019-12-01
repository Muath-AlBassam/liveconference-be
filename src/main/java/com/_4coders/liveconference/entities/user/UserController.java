package com._4coders.liveconference.entities.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/users")
public class UserController {
    @Autowired
    private UserService userService;

}
