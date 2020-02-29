package com._4coders.liveconference.entities.account.accountIpAddress;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.ipAddress.IpAddress;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.util.sort.SortUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Flogger
public class AccountIpAddressService {

    @Autowired
    private AccountIpAddressRepository accountIpAddressRepository;

    /**
     * Saves a new {@code AccountIpAddress} from the give {@code Account} and {@code IpAddress} and returns it
     *
     * @return the saved {@code AccountIpAddress} never null
     */
    //THINK throw an exception if exist?
    public AccountIpAddress saveAccountIpAddress(Account account, IpAddress ipAddress) {
        log.atFinest().log("Saving an AccountIpAddress with Account [%s] and ipAddress [%s]", account, ipAddress);
        AccountIpAddress toSave = new AccountIpAddress();
        toSave.setAccount(account);
        toSave.setIpAddress(ipAddress);
        toSave.setIsAuthorized(true);
        toSave.setKey(new AccountIpAddressKey());
//        toSave.getKey().setAccountID(account.getId());
//        toSave.getKey().setIpAddressID(ipAddress.getId());
        return accountIpAddressRepository.save(toSave);
    }

    /**
     * Getting the {@code AccountIpAddress} given an {@code Account} {@code Id}
     *
     * @param accountId the {@code Id} of the {@code Account} to search for
     * @param sort      the way data should be sorted
     * @return {@code Set<AccountIpAddress>} for the given {@code Id}
     * @throws MappingSortPropertiesToSchemaPropertiesException if the sort properties given aren't recognized
     */
    public Set<AccountIpAddress> getAccountIpAddressByAccountId(Long accountId, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of AccountIpAddress by accountId [%d] and Sort [%s]", accountId, sort);
        log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
        sort = SortUtil.accountIpAddressSortMapping(sort);
        log.atFinest().log("Fetching AccountIpAddresses...");
        Set<AccountIpAddress> toReturn = accountIpAddressRepository.getAccountIpAddressesByAccount_Id(accountId, sort);
        log.atFinest().log("Result of AccountIpAddresses fetching is [%s]", toReturn);
        return toReturn;
    }

    /**
     * Getting the {@code AccountIpAddress} given an {@code Account} {@code Email}
     *
     * @param accountEmail the {@code Email} of the {@code Account} to search for
     * @param sort         the way data should be sorted
     * @return {@code Set<AccountIpAddress>} for the given {@code Email}
     * @throws MappingSortPropertiesToSchemaPropertiesException if the sort properties given aren't recognized
     */
    public Set<AccountIpAddress> getAccountIpAddressByAccountEmail(String accountEmail, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of AccountIpAddress by accountEmail [%s] and Sort [%s]", accountEmail, sort);
        log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
        sort = SortUtil.accountIpAddressSortMapping(sort);
        log.atFinest().log("Fetching AccountIpAddresses...");
        Set<AccountIpAddress> toReturn = accountIpAddressRepository.getAccountIpAddressesByAccount_Email(accountEmail,
                sort);
        log.atFinest().log("Result of AccountIpAddresses fetching is [%s]", toReturn);
        return toReturn;
    }


    /**
     * Getting the {@code AccountIpAddress} given an {@code Account} {@code UUID}
     *
     * @param accountUuid the {@code UUID} of the {@code Account} to search for
     * @param sort        the way data should be sorted
     * @return {@code Set<AccountIpAddress>} for the given {@code UUID}
     * @throws MappingSortPropertiesToSchemaPropertiesException if the sort properties given aren't recognized
     */
    public Set<AccountIpAddress> getAccountIpAddressByAccountUuid(UUID accountUuid, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of AccountIpAddress by accountUuid [%s] and Sort [%s]", accountUuid, sort);
        log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
        sort = SortUtil.accountIpAddressSortMapping(sort);
        log.atFinest().log("Fetching AccountIpAddresses...");
        Set<AccountIpAddress> toReturn = accountIpAddressRepository.getAccountIpAddressesByAccount_Uuid(accountUuid, sort);
        log.atFinest().log("Result of AccountIpAddresses fetching is [%s]", toReturn);
        return toReturn;
    }
}
