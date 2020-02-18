package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.user.UserService;
import com._4coders.liveconference.exception.account.AccountFoundException;
import com._4coders.liveconference.exception.account.AccountNotFoundException;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.ipAddress.*;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@RestController
@Flogger
@RequestMapping(value = "/flogger/accounts", produces = {"application/json", "application/hal+json"})
@Validated
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;


    /**
     * Register an {@code Account} if possible
     *
     * @param toRegister the {@code Account} to register
     * @return HTTP CREATED(201) if the {@code Account} got registered</br>
     * HTTP BAD_REQUEST(400) if an {@code Account} with the same {@code email} exist</br>
     * HTTP FORBIDDEN (403) if the requester {@code IpAddress} is harmful
     * HTTP INTERNAL_SERVER_ERROR(500) if {@code UUIDUniquenessException} was thrown
     */
    @PostMapping("/register")
    @JsonView(AccountViews.OwnerDetails.class)
    @Transactional
    public ResponseEntity<Account> registerAccount(@RequestBody @Valid Account toRegister, HttpServletRequest request) {
        log.atFiner().log("Account registration was received with values [%s]", toRegister);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(accountService.registerAccount(toRegister, request));
        } catch (AccountFoundException | InvalidIpAddressException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (HarmfulIpAddressException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UUIDUniquenessException | IpAddressFoundException | APIKeyQuotaExceededException | APIKeyNotProvidedException | IpProviderUnknownException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Gets the current logged in {@code Account} from the {@code principal}
     *
     * @return HTTP OK(200) with the {@code Account} data if the request was authenticated
     */
    @GetMapping
    @JsonView(AccountViews.OwnerDetails.class)
    public ResponseEntity<Account> getCurrentLoggedInAccountDetails(@AuthenticationPrincipal AccountDetails accountDetails) {
        log.atFinest().log("Request for Account Details for currently logged in Account with email and UUID [%s] " +
                "[%s]", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        return ResponseEntity.ok(accountDetails.getAccount());
    }

    @GetMapping("/info")
    @JsonView(AccountViews.OwnerInformation.class)
    public ResponseEntity<Account> getCurrentLoggedInAccountInformation(@AuthenticationPrincipal AccountDetails accountDetails) {
        log.atFinest().log("Request for Account information for currently logged in Account with email and UUID [%s] " +
                "[%s]", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        return ResponseEntity.ok(accountDetails.getAccount());
    }

    /**
     * Gets an {@code Account} by the given {@code email}
     *
     * @param email the {@code email} to look for (comes in the form .../accounts?email=XXX)
     * @return HTTP OK(200) if an {@code Account} with the given {@code email} was found</br>
     * otherwise HTTP BAD_REQUEST(400)
     */
    @GetMapping(params = "email")
    public ResponseEntity<Account> getAccountByEmail(@RequestParam(value = "email") @Email String email) {
        log.atFiner().log("Account retrieval by Email [%s] was received", email);
        Account toReturn = accountService.getAccountByEmail(email);
        if (toReturn == null) {
            log.atFinest().log("Account was not Found Returning HTTP BAD_REQUEST (400)");
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Account was Found with data [%s] Returning HTTP OK(200)", toReturn);
            return ResponseEntity.ok(toReturn);
        }
    }


    /**
     * Gets an {@code Account} by the given {@code uuid}
     *
     * @param uuid the {@code uuid} to look for
     * @return HTTP OK(200) if an {@code Account} with the given {@code uuid} was found</br>
     * otherwise HTTP BAD_REQUEST(400)
     */
    @GetMapping(params = "uuid")
    public ResponseEntity<Account> getAccountByUuid(@RequestParam(value = "uuid") UUID uuid) {
        log.atFiner().log("Account retrieval by UUID [%s] was received", uuid);
        Account toReturn = accountService.getAccountByUuid(uuid);
        if (toReturn == null) {
            log.atFinest().log("Account was not Found Returning HTTP BAD_REQUEST (400)");
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Account was Found with data [%s] Returning HTTP OK(200)", toReturn);
            return ResponseEntity.ok(toReturn);
        }
    }


    @PatchMapping(value = "/update_password")
    @Transactional
    public ResponseEntity<String> loggedInAccountPasswordUpdate(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                @RequestBody @NotBlank String newPassword) {
        log.atFinest().log("Request for updating password for the currently logged in Account with email and UUID [%s]" +
                " [%s]", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        try {
            accountService.updateAccountPassword(accountDetails.getAccount().getId(), newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().body("Unknown account");
        }
    }


    @JsonView({AccountViews.OwnerInformation.class})
    @PutMapping(value = "/update", params = {"firstName", "middleName", "lastName", "phoneNumber"})
    @Transactional
    public ResponseEntity<Account> updateLoggedInAccountInformation(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                    @RequestParam("firstName") @NotBlank String firstName,
                                                                    @RequestParam("middleName") @NotBlank String middleName,
                                                                    @RequestParam("lastName") @NotBlank String lastName,
                                                                    @RequestParam("phoneNumber") @Size(min = 13, max = 13) String phoneNumber,
                                                                    @RequestBody @Valid Address address) {
        log.atFinest().log("Received request for currently logged in Account with Email [%s] and UUID [%s] to update " +
                "it's information", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        try {
            Account toReturn = accountService.updateAccountInformationById(accountDetails.getAccount().getId(),
                    firstName, middleName, lastName, phoneNumber, address);
            log.atFinest().log("Returning HTTP 200 as the Account was updated");
            return ResponseEntity.ok(toReturn);
        } catch (AccountFoundException | AccountNotFoundException ex) {
            log.atFinest().log("Returning HTTP 400 as the an Account was fount with the same phone or no Account was " +
                    "found by the same id");
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping(value = "/update_password", params = "uuid")
    @Transactional
    public ResponseEntity<String> accountPasswordUpdateByUuid(@RequestParam(value = "uuid") UUID accountUuid,
                                                              @RequestBody @NotBlank String newPassword) {
        log.atFinest().log("Request for updating password for Account with UUID [%s]", accountUuid);
        try {
            accountService.updateAccountPasswordByUuid(accountUuid, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().body("Unknown account");
        }
    }

    @PatchMapping(value = "/update", params = "new_email")
    @Transactional
    public ResponseEntity<String> loggedInAccountEmailUpdate(@AuthenticationPrincipal AccountDetails accountDetails,
                                                             @RequestParam("new_email") @Email String newEmail) {
        log.atFinest().log("Request for updating email for the currently logged in Account with email and UUID [%s]" +
                " [%s]", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        try {
            accountService.updateAccountEmail(accountDetails.getAccount().getId(), newEmail);
            return ResponseEntity.ok("Email updated successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().body("Unknown account");
        } catch (AccountFoundException ex) {
            return ResponseEntity.badRequest().body("The given email is already used");
        }
    }


    @PatchMapping(value = "/update", params = {"uuid", "new_email"})
    @Transactional
    public ResponseEntity<String> accountEmailUpdateByUuid(@RequestParam(value = "uuid") UUID accountUuid,
                                                           @RequestParam("new_email") @Email String newEmail) {
        log.atFinest().log("Request for updating email for Account with UUID [%s]", accountUuid);
        try {
            accountService.updateAccountEmailByUuid(accountUuid, newEmail);
            return ResponseEntity.ok("Email updated successfully");
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.badRequest().body("Unknown account");
        } catch (AccountFoundException ex) {
            return ResponseEntity.badRequest().body("The given email is already used");
        }
    }









    /*@Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;


    @Transactional
    @GetMapping("testa")
    public Account testA(@AuthenticationPrincipal AccountDetails account) {
        return account.getAccount().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class).testA(account)).withSelfRel());
    }

    @GetMapping("sessions")
    public Map se(@AuthenticationPrincipal AccountDetails account) {
        return sessionRepository.findByPrincipalName(account.getUsername());
    }

    @GetMapping("sessionsd")
    public List<? extends SessionInformation> sed(@AuthenticationPrincipal AccountDetails account) {
        SpringSessionBackedSessionRegistry sessionRegistry =
                new SpringSessionBackedSessionRegistry(sessionRepository);

        return sessionRegistry.getAllSessions(account.getUsername(), true);
    }*/


}

