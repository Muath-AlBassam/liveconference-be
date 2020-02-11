package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.exception.account.AccountFoundException;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.ipAddress.*;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.UUID;

@RestController
@Flogger
@RequestMapping(value = "/flogger/accounts", produces = {"application/json", "application/hal+json"})
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;


    /**
     * Register an {@code Account} if possible
     *
     * @param toRegister the {@code Account} to register
     * @return HTTP CREATED(201) if the {@code Account} got registered<br/>
     * HTTP BAD_REQUEST(400) if an {@code Account} with the same {@code email} exist<br/>
     * HTTP FORBIDDEN (403) if the requester {@code IpAddress} is harmful
     * HTTP INTERNAL_SERVER_ERROR(500) if {@code UUIDUniquenessException} was thrown
     */
    @PostMapping("/register")
    @JsonView(AccountViews.Owner.class)
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
     * Gets an {@code Account} by the given {@code email}
     *
     * @param email the {@code email} to look for (comes in the form .../account?email=XXX)
     * @return HTTP OK(200) if an {@code Account} with the given {@code email} was found<br/>
     * otherwise HTTP BAD_REQUEST(400)
     */
    @GetMapping("/account")
    public ResponseEntity<Account> getAccountByEmail(@RequestParam(value = "email") @Email String email) {
        log.atFiner().log("Account retrieval by Email [%s] was received", email);
        Account toReturn = accountService.getAccountByEmail(email);
        if (toReturn == null) {
            log.atFinest().log("Account was not Found Returning HTTP BAD_REQUEST (400)");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            log.atFinest().log("Account was Found with data [%s] Returning HTTP OK(200)", toReturn);
            return new ResponseEntity<>(toReturn, HttpStatus.OK);
        }
    }


    /**
     * Gets an {@code Account} by the given {@code uuid}
     *
     * @param uuid the {@code uuid} to look for
     * @return HTTP OK(200) if an {@code Account} with the given {@code uuid} was found<br/>
     * otherwise HTTP BAD_REQUEST(400)
     */
    @GetMapping("/account/{uuid}")
    public ResponseEntity<Account> getAccountByUuid(@PathVariable UUID uuid) {
        log.atFiner().log("Account retrieval by UUID [%s] was received", uuid);
        Account toReturn = accountService.getAccountByUuid(uuid);
        if (toReturn == null) {
            log.atFinest().log("Account was not Found Returning HTTP BAD_REQUEST (400)");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            log.atFinest().log("Account was Found with data [%s] Returning HTTP OK(200)", toReturn);
            return new ResponseEntity<>(toReturn, HttpStatus.OK);
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

