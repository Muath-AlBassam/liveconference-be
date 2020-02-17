package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddressService;
import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.ipAddress.IpAddress;
import com._4coders.liveconference.entities.ipAddress.IpAddressService;
import com._4coders.liveconference.entities.role.system.SystemRoleService;
import com._4coders.liveconference.entities.user.UserService;
import com._4coders.liveconference.exception.account.AccountFoundException;
import com._4coders.liveconference.exception.account.AccountNotFoundException;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.ipAddress.*;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;


@Service
@Flogger
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountIpAddressService accountIpAddressService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private IpAddressService ipAddressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Updates the {@code Email} for the given {@code Account} Id
     *
     * @param accountId the {@code ID} for the {@code Account} to update
     * @param newEmail  the new {@code email}
     * @throws AccountNotFoundException if no {@code Account} with the given {@code ID} exists
     * @throws AccountFoundException    if an {@code Account} with the newEmail exists.
     */
    public void updateAccountEmail(Long accountId, String newEmail) throws AccountNotFoundException, AccountFoundException {
        log.atFinest().log("Fetching an Account with ID [%d] for email update", accountId);
        if (!accountRepository.existsById(accountId)) {//shall never happen
            log.atFinest().log("No Account with the given ID [%d] exist throwing AccountNotFoundException", accountId);
            throw new AccountNotFoundException(String.format("No Account with given ID [%d] exists", accountId),
                    accountId);
        } else {
            if (existAccountByEmail(newEmail)) {
                log.atFinest().log("An Account with the given Email exists throwing AccountFoundException");
                throw new AccountFoundException(String.format("An account with the given Email [%s] was found", newEmail));
            } else {
                log.atFinest().log("Updating the email...");
                accountRepository.updateEmailById(accountId, newEmail);
            }
        }
    }

    /**
     * Updates the {@code Email} for the given {@code Account} {@code UUID}
     *
     * @param uuid     the {@code UUID} for the {@code Account} to update
     * @param newEmail the new {@code email}
     * @throws AccountNotFoundException if no {@code Account} with the given {@code UUID} exists
     * @throws AccountFoundException    if an {@code Account} with the newEmail exists.
     */
    public void updateAccountEmailByUuid(UUID uuid, String newEmail) throws AccountNotFoundException,
            AccountFoundException {
        log.atFinest().log("Fetching an Account with UUID [%s] for email update", uuid);
        if (!accountRepository.existsAccountByUuid(uuid)) {//shall never happen
            log.atFinest().log("No Account with the given UUID [%s] exist throwing AccountNotFoundException", uuid);
            throw new AccountNotFoundException(String.format("No Account with given ID [%s] exists", uuid), uuid);
        } else {
            if (existAccountByEmail(newEmail)) {
                log.atFinest().log("An Account with the given Email exists throwing AccountFoundException");
                throw new AccountFoundException(String.format("An account with the given Email [%s] was found", newEmail));
            } else {
                log.atFinest().log("Updating the email...");
                accountRepository.updateEmailByUuid(uuid, newEmail);
            }
        }
    }

    /**
     * Updates the {@code Password} for the given {@code Account} Id
     *
     * @param accountId   the {@code ID} for the {@code Account} to update
     * @param newPassword the new {@code Password}
     * @throws AccountNotFoundException if no {@code Account} with the given {@code ID} exists
     */
    public void updateAccountPassword(Long accountId, String newPassword) throws AccountNotFoundException {
        log.atFinest().log("Fetching an Account with ID [%d] for password update", accountId);
        if (!accountRepository.existsById(accountId)) {//shall never happen
            log.atFinest().log("No Account with the given ID exist throwing AccountNotFoundException");
            throw new AccountNotFoundException(String.format("No Account with given ID [%d] exists", accountId),
                    accountId);
        } else {
            newPassword = passwordEncoder.encode(newPassword);
            log.atFinest().log("Updating the password...");
            accountRepository.updatePasswordById(accountId, newPassword);
        }
    }

    /**
     * Updates the {@code Password} for the given {@code Account} {@code UUID}
     *
     * @param uuid        the {@code UUID} for the {@code Account} to update
     * @param newPassword the new {@code Password}
     * @throws AccountNotFoundException if no {@code Account} with the given {@code UUID} exists
     */
    public void updateAccountPasswordByUuid(UUID uuid, String newPassword) throws AccountNotFoundException {
        log.atFinest().log("Fetching an Account with UUID [%s] for password update", uuid);
        if (!accountRepository.existsAccountByUuid(uuid)) {
            log.atFinest().log("No Account with the given UUID exist throwing AccountNotFoundException");
            throw new AccountNotFoundException(String.format("No Account with given ID [%s] exists", uuid), uuid);
        } else {
            newPassword = passwordEncoder.encode(newPassword);
            log.atFinest().log("Updating the password...");
            accountRepository.updatePasswordByUuid(uuid, newPassword);
        }
    }

    /**
     * Returns an {@code Account} from a given {@code email}
     *
     * @param email the {@code email} to look for
     * @return {@code Account} if exists otherwise {@code null}
     */
    public Account getAccountByEmail(String email) {
        log.atFinest().log("Getting an Account with given Email [%s]", email);
        final Account toReturn = accountRepository.getAccountByEmail(email);
        log.atFinest().log("Result of Account retrieval [%s]", toReturn);
        return toReturn;
    }

    /**
     * Returns an {@code Account} from a given {@code uuid}
     *
     * @param uuid the {@code uuid} to look for
     * @return {@code Account} if exists otherwise {@code null}
     */
    public Account getAccountByUuid(UUID uuid) {
        log.atFinest().log("Getting an Account with given UUID [%s]", uuid);
        final Account toReturn = accountRepository.getAccountByUuid(uuid);
        log.atFinest().log("Result of Account retrieval [%s]", toReturn);
        return toReturn;
    }

    /**
     * Returns {@code boolean} if an {@code Account} exist with the given {@code id}
     *
     * @param id the {@code id} to lock for
     * @return true if an {@code Account} with given {@code id} was found, false otherwise
     */
    public boolean existAccountById(Long id) {
        log.atFinest().log("Checking whether an Account with the given id [%d] exist", id);
        final boolean toReturn = accountRepository.existsById(id);
        log.atFinest().log("Result of Account existence checking [%b]", toReturn);
        return toReturn;
    }

    /**
     * Returns {@code boolean} if an {@code Account} exist with the given {@code id} and {@code IsActive} and {@code
     * IsNotBlocked}
     *
     * @param id the {@code id} to lock for
     * @return true if an {@code Account} with given {@code id} and {@code IsActive} and {@code IsNotBlocked} was
     * found, false otherwise
     */
    public boolean existAccountByIdAndIsActiveAndIsNotBlocked(Long id) {
        log.atFinest().log("Checking whether an Account with the given id [%d] exist and is active and is not blocked",
                id);
        final boolean toReturn = accountRepository.existsAccountByIdAndIsActivatedIsTrueAndIsBlockedIsFalse(id);
        log.atFinest().log("Result of Account existence checking [%b]", toReturn);
        return toReturn;
    }

    /**
     * Returns {@code boolean} if an {@code Account} exist with the  given {@code email}
     *
     * @param email the {@code email} to lock for
     * @return true if an {@code Account} with given {@code email} was found, false otherwise
     */
    public boolean existAccountByEmail(String email) {
        log.atFinest().log("Checking whether an Account with the given Email [%s] exist", email);
        final boolean toReturn = accountRepository.existsAccountByEmail(email);
        log.atFinest().log("Result of Account existence checking [%b]", toReturn);
        return toReturn;
    }

    /**
     * Returns {@code boolean} if an {@code Account} exist with the  given {@code UUID}
     *
     * @param uuid the {@code UUID} to lock for
     * @return true if an {@code Account} with give {@code UUID} was found, false otherwise
     */
    public boolean existAccountByUuid(UUID uuid) {
        log.atFinest().log("Checking whether an Account with the given UUID [%s] exist", uuid);
        final boolean toReturn = accountRepository.existsAccountByUuid(uuid);
        log.atFinest().log("Result of Account existence checking [%b]", toReturn);
        return toReturn;
    }

    /**
     * updates the {@code LastLoginDate} for the given {@code Account}
     *
     * @param account the {@code Account} to update
     */
    public void updateLastLoginDate(Account account) {
        accountRepository.updateLastLoginDate(account.getId(), LocalDateTime.now());
    }

    /**
     * Updates the {@code Account} information for the given {@code Account} {@code ID}
     *
     * @param id          the {@code ID} for the {@code Account} to update
     * @param firstName   the new {@code FirstName} to be saved
     * @param middleName  the new {@code MiddleName} to be saved
     * @param lastName    the new {@code LastName} to be saved
     * @param phoneNumber the new {@code PhoneNumber} to be saved
     * @param address     the new {@code Address} to be saved
     * @throws AccountNotFoundException if no {@code Account} with the given {@code ID} exists
     * @throws AccountFoundException    if an {@code Account} with the same {@code PhoneNubmer} exists.
     */
    public Account updateAccountInformationById(Long id, String firstName, String middleName, String lastName,
                                                String phoneNumber, Address address) throws AccountNotFoundException, AccountFoundException {
        Optional<Account> fetchedAccount = accountRepository.findById(id);
        if (fetchedAccount.isEmpty()) {
            log.atFinest().log("No Account with the given ID [%d] exist throwing AccountNotFoundException", id);
            throw new AccountNotFoundException(String.format("No Account with given ID [%d] exists", id), id);
        } else {
            log.atFinest().log("An Account was fetched with ID [%d]", id);
            Account toUpdate = fetchedAccount.get();
            log.atFinest().log("Checking wither an Account With given PhoneNumber [%s] exist or not", phoneNumber);
            if (accountRepository.existsAccountByPhoneNumber(phoneNumber)) {
                log.atFinest().log("Account with given PhoneNumber [%s] exists throwing AccountFoundException", phoneNumber);
                throw new AccountFoundException(String.format("Account with given PhoneNumber [%s] exists",
                        phoneNumber));
            } else {
                log.atFinest().log("No Account was found with the given PhoneNumber");
                log.atFinest().log("Updating the values in memory...");
                log.atFinest().log("updating the PhoneNumber");
                toUpdate.setPhoneNumber(phoneNumber);
                log.atFinest().log("Updating the FirstName [%s], MiddleName [%s] and LastName [%s]", firstName, middleName,
                        lastName);
                toUpdate.setFirstName(firstName);
                toUpdate.setMiddleName(middleName);
                toUpdate.setLastName(lastName);
                log.atFinest().log("Updating the Address");
                toUpdate.setAddress(address);
                log.atFinest().log("Updating the Account in the DB");
                return accountRepository.save(toUpdate);
            }
        }
    }

    /**
     * Registers the given {@code Account}
     *
     * @param toRegister the {@code Account} to register
     * @throws AccountFoundException        when an {@code Account} with the same given {@code Email} exist
     * @throws UUIDUniquenessException      when the generation of unique {@code UUID} has failed
     * @throws InvalidIpAddressException    when {@code IpAddress} retrieval throws {@code InvalidIpAddressException}
     * @throws APIKeyNotProvidedException   when {@code IpAddress} retrieval throws {@code APIKeyNotProvidedException}
     * @throws APIKeyQuotaExceededException when {@code IpAddress} retrieval throws {@code APIKeyQuotaExceededException}
     * @throws IpProviderUnknownException   when {@code IpAddress} retrieval throws {@code IpProviderUnknownException}
     * @throws IpAddressFoundException      when saving an already existing {@code IpAddress}
     */
    public Account registerAccount(Account toRegister, HttpServletRequest request)
            throws AccountFoundException, UUIDUniquenessException, InvalidIpAddressException,
            APIKeyNotProvidedException, APIKeyQuotaExceededException, IpProviderUnknownException,
            IpAddressFoundException, HarmfulIpAddressException {
        log.atFinest().log("Initiate Account registering with Account information as follow: [%s]", toRegister);
        log.atFinest().log("Checking whether the given Email from the Account: [%s] exist or not",
                toRegister.getEmail());
        if (existAccountByEmail(toRegister.getEmail())) {//don't create account
            log.atFiner().log("An Account with the Email: [%s] already exist", toRegister.getEmail());
            log.atFiner().log("Throwing the exception [$s]", AccountFoundException.class);
            throw new AccountFoundException(String.format("An Account with email [%s] already exist", toRegister.getEmail()),
                    toRegister);
        } else {// create
            log.atFinest().log("No Account with Email: [%s] was found", toRegister.getEmail());
            return initiateAccountRequiredValues(toRegister, request);

        }
    }

    private Account initiateAccountRequiredValues(Account toRegister, HttpServletRequest request) throws UUIDUniquenessException, InvalidIpAddressException,
            APIKeyNotProvidedException, APIKeyQuotaExceededException, IpProviderUnknownException,
            IpAddressFoundException, HarmfulIpAddressException {
        log.atFinest().log("Starting to initiate required values");
        log.atFinest().log("Getting unique UUID value ...");
        byte counterLimit = 0;
        UUID uniqueUUID = UUID.randomUUID();
        while (existAccountByUuid(uniqueUUID) && counterLimit >= 0) {
            uniqueUUID = UUID.randomUUID();
            counterLimit++;//play with overflow
            log.atFinest().every(16).log("[%d] iterations has gone from 128 to find unique UUID", counterLimit + 1);
        }
        if (counterLimit < 0) {// no unique UUID was found
            //THINK maybe printing the email is enough?
            log.atSevere().log("Can't register the Account [%s] as no unique UUID was found", toRegister);
            log.atSevere().log("Throwing the exception [$s]", UUIDUniquenessException.class);
            throw new UUIDUniquenessException(String.format("No unique UUID was found after [%d] tries", counterLimit),
                    counterLimit);
        } else {
            log.atFinest().log("Unique UUID was found with value: [%s]", uniqueUUID);
            log.atFinest().log("Setting UUID value");
            toRegister.setUuid(uniqueUUID);

            /*
            //TODO enable in prod
                log.atFinest().log("Validating the Ip Address");
                IpAddress ipAddress = getIpAddress(request);
                log.atFinest().log("Checking wither requester IpAddress is harmful [%s]", ipAddress);
                if (ipAddress.getThreat().getThreat()) {
                    log.atFinest().log("It's harmful ...");
                    log.atSevere().log("Throwing the exception [$s]", HarmfulIpAddressException.class);
                    throw new HarmfulIpAddressException(String.format("A harmful IpAddress isn't allowed for " +
                            "registration with data [%s]", ipAddress), ipAddress);
                }
                log.atFinest().log("It's not harmful");*/

            log.atFinest().log("Encoding the Password");
            toRegister.setPassword(passwordEncoder.encode(toRegister.getPassword()));
            log.atFinest().log("Setting IsActivated to [%b]", false);
            toRegister.setIsActivated(false);
            log.atFinest().log("Giving the SystemRole PERMISSION_USER to this Account");
            toRegister.setRoles(new HashSet<>());
            toRegister.getRoles().add(systemRoleService.getRoleByName("ROLE_USER"));
            log.atFinest().log("All data has been initialized - except IpAddress relation");
            log.atFinest().log("Persisting the Account [%s]", toRegister);
            Account toReturn = accountRepository.save(toRegister);
            log.atFiner().log("The Account with Email [%s] has been registered", toReturn.getEmail());
            log.atFinest().log("Values of the Account: [%s]", toReturn);

            /*
            //TODO enable in prod
                log.atFinest().log("Saving an AccountIpAddress");
                AccountIpAddress saveAccountIpAddress = accountIpAddressService.saveAccountIpAddress(toReturn, ipAddress);
                log.atFinest().log("Saved an AccountIpAddress with data [%s], Account [%s] and IpAddress [%s]",
                        saveAccountIpAddress, saveAccountIpAddress.getAccount(), saveAccountIpAddress.getIpAddress());
                log.atFinest().log("Adding the saved AccountIpAddress to the Account");
                toRegister.setIpAddresses(new HashSet<>());
                toReturn.getIpAddresses().add(saveAccountIpAddress);*/

            return toReturn;
        }
    }

    private IpAddress getIpAddress(HttpServletRequest request) throws InvalidIpAddressException, APIKeyNotProvidedException, APIKeyQuotaExceededException, IpProviderUnknownException, IpAddressFoundException {
        IpAddress ipAddress = ipAddressService.getSingleIpAddress(request);
        if (ipAddressService.ipAddressExists(ipAddress.getIp())) {//it exists thus retrieve it
            ipAddress = ipAddressService.getIpAddress(ipAddress.getIp());
        } else {//we don't have, thus save it
            ipAddress = ipAddressService.saveIpAddress(ipAddress);
        }
        return ipAddress;
    }
}
