package com._4coders.liveconference.entities.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public void create(@RequestBody Account account) {
        accountService.asd();
    }
}
