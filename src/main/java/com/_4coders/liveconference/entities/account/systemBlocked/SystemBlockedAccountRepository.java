package com._4coders.liveconference.entities.account.systemBlocked;

import com._4coders.liveconference.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SystemBlockedAccountRepository extends JpaRepository<SystemBlockedAccount, Long> {

    Set<SystemBlockedAccount> getBlockedAccountsByAccount_Email(String email);

    Set<SystemBlockedAccount> getBlockedAccountsByAccount(Account account);

    Set<SystemBlockedAccount> getBlockedAccountsByAccount_Id(Long id);

    int countBlockedAccountsByAccount_Email(String email);

    int countBlockedAccountsByAccount(Account account);

    int countBlockedAccountsByAccount_Id(Long id);

}
