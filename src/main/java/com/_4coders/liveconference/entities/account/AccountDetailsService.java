package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccount;
import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SystemBlockedAccountRepository systemBlockedAccountRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account fetchedAccount = accountService.getAccountByEmail(email);
        if (fetchedAccount == null) {
            throw new UsernameNotFoundException("Account with Email: " + email + " was not found");
        } else {
            Set<SystemBlockedAccount> systemBlockedAccounts = systemBlockedAccountRepository.getBlockedAccountsByAccount_Id(fetchedAccount.getId());
            if (systemBlockedAccounts.size() > 3 || systemBlockedAccounts.stream().anyMatch(SystemBlockedAccount::isBlocked)) {
                fetchedAccount.setIsBlocked(true);
            } else {
                fetchedAccount.setIsBlocked(false);
            }
            accountService.updateLastLoginDate(fetchedAccount);
            return new AccountDetails(fetchedAccount);
        }
    }
}
