package com._4coders.liveconference.entities.account.accountIpAddress;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface AccountIpAddressRepository extends JpaRepository<AccountIpAddress, AccountIpAddressKey> {
    //TODO add 3 getAccountIp...By ...id,email,uuid (for ipAddress and Account)

    public Set<AccountIpAddress> getAccountIpAddressesByAccount_Id(Long accountId, Sort sort);

    public Set<AccountIpAddress> getAccountIpAddressesByAccount_Email(String email, Sort sort);

    public Set<AccountIpAddress> getAccountIpAddressesByAccount_Uuid(UUID uuid, Sort sort);
}
