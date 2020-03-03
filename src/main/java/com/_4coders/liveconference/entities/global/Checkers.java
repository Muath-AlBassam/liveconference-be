package com._4coders.liveconference.entities.global;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.user.User;
import org.springframework.stereotype.Component;

@Component
public class Checkers {

    public boolean checkIfTargetUserBlockedRequester(Account requester, User target) {
        return target.getAccount().getBlockedAccounts().stream().anyMatch(blockedAccount -> blockedAccount.getBlocked().getId().equals(requester.getId()));
    }
}
