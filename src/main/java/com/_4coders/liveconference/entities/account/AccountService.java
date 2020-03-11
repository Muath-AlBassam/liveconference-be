package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddressService;
import com._4coders.liveconference.entities.account.activation.AccountActivation;
import com._4coders.liveconference.entities.account.activation.AccountActivationService;
import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.global.MailSendingService;
import com._4coders.liveconference.entities.ipAddress.IpAddress;
import com._4coders.liveconference.entities.ipAddress.IpAddressService;
import com._4coders.liveconference.entities.role.system.SystemRoleService;
import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.UserService;
import com._4coders.liveconference.entities.user.UserStatus;
import com._4coders.liveconference.exception.account.*;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.ipAddress.*;
import com._4coders.liveconference.exception.user.UserNotFoundException;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private AccountActivationService accountActivationService;

    @Autowired
    private MailSendingService mailSendingService;

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

    public boolean clearCurrentInUseUser(Account account) throws AccountNotFoundException {
        log.atFinest().log("Clearing current in use user for Account with UUID [%s]", account.getUuid());
        log.atFinest().log("Checking the the given Account exists...");
        if (!accountRepository.existsById(account.getId())) {
            log.atFinest().log("No Account was found with the given data UUID[%s], ID[%s] and Email [%s] throwing " +
                    "AccountNotFoundException", account.getUuid(), account.getId(), account.getEmail());
            throw new AccountNotFoundException(String.format("No Account with given ID [%s] exists", account.getId()),
                    account.getId());
        } else {
            accountRepository.setCurrentInUseUserToNull(account.getId());
            account.setCurrentInUseUser(null);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                    authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return true;
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
            throw new AccountNotFoundException(String.format("No Account with given UUID [%s] exists", uuid), uuid);
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

    public boolean updateCurrentInUseUser(Account account, UUID uuid) throws UserNotFoundException {
        Optional<User> toSet = account.getUsers().stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
        if (toSet.isEmpty()) {
            log.atFinest().log("No User was found with UUID [%s] for the Account with UUID[%s]", uuid, account.getUuid());
            throw new UserNotFoundException(String.format("No User was found with UUID [%s] for the Account with UUID[%s]", uuid, account.getUuid()), uuid);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AccountDetails principal = (AccountDetails) authentication.getPrincipal();
            if (account.getCurrentInUseUser() != null) {
                User currUser =
                        principal.getAccount().getUsers().stream().filter(user -> user.getUuid().equals(account.getCurrentInUseUser().getUuid())).findFirst().get();
                currUser.setLastStatus(account.getCurrentInUseUser().getStatus());
                currUser.setStatus(UserStatus.OFFLINE);
                userService.updateUserStatusAndLastStatusByUserUuid(account, currUser.getUuid(),
                        currUser.getStatus().toString(),
                        currUser.getLastStatus().toString());
            }
            accountRepository.updateCurrentInUseUser(account.getId(), toSet.get().getId());
            toSet.get().setStatus(toSet.get().getLastStatus());
            principal.getAccount().setCurrentInUseUser(toSet.get());
            principal.getAccount().setDefaultUser(principal.getAccount().getUsers().stream().filter(user -> user.getUuid().equals(principal.getAccount().getDefaultUser().getUuid())).findFirst().get());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(principal,
                    authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            userService.updateUserStatusAndLastStatusByUserUuid(account, toSet.get().getUuid(),
                    toSet.get().getLastStatus().toString(), null);
            return true;
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

    public boolean existsBlockedAccountByBlockerId(Long blockerId, Long targetIdToLookFor) {
        log.atFinest().log("Checking if there exist blocked Account [%d] by Blocker ID [%d]", blockerId, targetIdToLookFor);
        return accountRepository.existsBlockedAccountByBlockerId(blockerId, targetIdToLookFor);
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
     * Checks wither an {@link Account} exists by the given {@code Email} and {@code PhoneNumber}
     *
     * @param email       the {@code email} to look for
     * @param phoneNumber the {@code PhoneNumber} to look for
     * @return 0 if no {@link Account} exists with the given @code Email} and {@code PhoneNumber}<br/>
     * 1 if an {@link Account} exists with the given {@code email} only <br/>
     * 2 if an {@link Account} exists with the given {@code PhoneNumber} only <br/>
     * 3 if an {@link Account} exists with the given {@code Email} and {@code PhoneNumber}
     */
    public int existAccountByEmailOrPhoneNumber(String email, String phoneNumber) {
        log.atFinest().log("Checking whether an Account with the given Email [%s] and PhoneNumber [%s] exists",
                email, phoneNumber);
        final int toReturn = accountRepository.existAccountByEmailOrPhoneNumber(email, phoneNumber);
        log.atFinest().log("Result of Account existence checking [%d]", toReturn);
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

    public Account activateAccount(String email, String activationCode) throws AccountNotFoundException,
            AccountAlreadyActivatedException, AccountActivationCodeMatchException {
        log.atFinest().log("Activating the Account with Email [%s] with the given Code [%s]", email, activationCode);
        log.atFinest().log("Fetching an Account with the given email");
        Account fetchedAccount = getAccountByEmail(email);
        if (fetchedAccount == null) {
            log.atFinest().log("Throwing AccountNotFoundException as there Exists no Account with the given Email");
            throw new AccountNotFoundException(String.format("No Account was found with the Email [%s]", email), email);
        } else {
            if (fetchedAccount.getIsActivated()) {
                log.atFinest().log("Throwing AccountAlreadyActivatedException as we're trying to activate an already " +
                        "activated Account");
                throw new AccountAlreadyActivatedException(String.format("The given Account is already activated, account" +
                        " email [%s]", fetchedAccount.getEmail()));
            } else {
                accountActivationService.activateAccountActivation(fetchedAccount, activationCode);
                fetchedAccount.setIsActivated(true);
                return updateAccount(fetchedAccount);

            }
        }
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
     * @throws AccountFoundException        when an {@code Account} with the same given {@code Email} or {@code PhoneNumber} exist
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
            IpAddressFoundException, HarmfulIpAddressException, AccountAlreadyActivatedException {
        log.atFinest().log("Initiate Account registering with Account information as follow: [%s]", toRegister);
        log.atFinest().log("Checking whether the given Email from the Account: [%s] exist or not",
                toRegister.getEmail());
        switch (existAccountByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())) {
            case 1://Email already exists
                log.atFiner().log("An Account with the Email: [%s] already exist",
                        toRegister.getEmail());
                log.atFiner().log("Throwing the exception [$s]", AccountFoundByEmailException.class);
                throw new AccountFoundByEmailException(String.format("An Account with Email [%s] already exist",
                        toRegister.getEmail()), toRegister.getEmail());
            case 2://PhoneNumber exist already
                log.atFiner().log("An Account with the PhoneNumber: [%s] already exist",
                        toRegister.getPhoneNumber());
                log.atFiner().log("Throwing the exception [$s]", AccountFoundByPhoneNumberException.class);
                throw new AccountFoundByPhoneNumberException(String.format("An Account with PhoneNumber [%s] already exist",
                        toRegister.getPhoneNumber()), toRegister.getPhoneNumber());
            case 3://both
                log.atFiner().log("An Account with the Email: [%s] and PhoneNumber [%s] already exist",
                        toRegister.getEmail(), toRegister.getPhoneNumber());
                log.atFiner().log("Throwing the exception [$s]", AccountFoundException.class);
                throw new AccountFoundException(String.format("An Account with Email [%s] and PhoneNumber [%s] " +
                                "already exist",
                        toRegister.getEmail(), toRegister.getPhoneNumber()));
            default:
                log.atFinest().log("No Account with Email: [%s] was found", toRegister.getEmail());
                return persistNewAccount(toRegister, request);
        }
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Generates new Activation code for the given {@link Account} {@code Email}
     *
     * @param emailToSendTo the {@code Email} of the {@link Account}
     * @return true if the code was generated and an email with the code was sent
     * @throws AccountNotFoundException if no {@link Account} exists with the given {@code Email}
     * @throws MessagingException       if an error occurs while sending the email
     */
    public boolean generateNewActivationCode(String emailToSendTo) throws AccountNotFoundException,
            MessagingException, AccountAlreadyActivatedException {
        Account fetchedAccount = accountRepository.getAccountByEmailAndIsBlockedIsFalse(emailToSendTo);
        if (fetchedAccount == null) {
            log.atFinest().log("Throwing AccountNotFoundException as there exists no account with the given Email " +
                            "[%s] or it's blocked ",
                    emailToSendTo);
            throw new AccountNotFoundException(String.format("No account was found with the given Email [%s] or it's blocked ",
                    emailToSendTo), emailToSendTo);
        } else {
            final String activationNewCodeReason = "Generating new code for Account";
            AccountActivation accountActivation = accountActivationService.setActivationForAccount(fetchedAccount, activationNewCodeReason);
            mailSendingService.sendActivationCodeEmail(fetchedAccount, accountActivation);
            return true;
        }
    }

    private Account persistNewAccount(Account toRegister, HttpServletRequest request) throws IpAddressFoundException, AccountAlreadyActivatedException {
        initiateAccountRequiredValues(toRegister, request);
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

        final String activationReason = "Activation Code for new Account";
        AccountActivation accountActivation = accountActivationService.setActivationForAccount(toReturn, activationReason);
        try {
            mailSendingService.sendActivationCodeEmail(toRegister, accountActivation);
        } catch (MessagingException ex) {//TODO solve
            log.atSevere().log(ex.getCause().getMessage());
        }

        return toReturn;
    }

    private void initiateAccountRequiredValues(Account toRegister, HttpServletRequest request) throws UUIDUniquenessException, InvalidIpAddressException,
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
