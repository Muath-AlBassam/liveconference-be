package com._4coders.liveconference.entities.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public void asd() {
        accountRepository.deleteById((long) 9);
    }
}
