package com._4coders.liveconference.entities.account.accountIpAddress;

import com._4coders.liveconference.entities.account.AccountDetails;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import java.util.Set;
import java.util.UUID;

@RestController
@Flogger
@RequestMapping(value = "/flogger/account_ip_address", produces = {"application/json", "application/hal+json"})
@Validated
public class AccountIpAddressController {

    @Autowired
    private AccountIpAddressService accountIpAddressService;

    /**
     * Gets the {@code Set of AccountIpAddress} for the currently logged in {@code Account}
     *
     * @return HTTP OK(200) with the data or without
     */
    @GetMapping("/account")
    //Think Page may be better than a set
    public ResponseEntity<Set<AccountIpAddress>> getAccountIpAddress(@AuthenticationPrincipal AccountDetails accountDetails, Sort sort) {
        log.atFiner().log("Request for getting Set of AccountIpAddress for currently logged in Account with email " +
                        "[%s], UUID [%s] and Sort [%s]", accountDetails.getAccount().getEmail(),
                accountDetails.getAccount().getUuid(), sort);
        //TODO should return 4xx if there is no content and Must catch Mapping....Exception
        return ResponseEntity.ok(accountIpAddressService.getAccountIpAddressByAccountId(accountDetails.getAccount().getId(), sort));
    }

    /**
     * Gets the {@code Set of AccountIpAddress} for the {@code Account} with given {@code Email}
     *
     * @return HTTP OK(200) with the data or without
     */
    @GetMapping(value = "/account", params = "email")
    public ResponseEntity<Set<AccountIpAddress>> getAccountIpAddressByAccountEmail(@RequestParam(value = "email") @Email String email, Sort sort) {
        log.atFiner().log("Request for getting Set of AccountIpAddress for Account with Email " +
                "[%s] and Sort [%s]", email, sort);
        return ResponseEntity.ok(accountIpAddressService.getAccountIpAddressByAccountEmail(email, sort));
    }

    /**
     * Gets the {@code Set of AccountIpAddress} for the {@code Account} with given {@code UUID}
     *
     * @return HTTP OK(200) with the data or without
     */
    @GetMapping(value = "/account", params = "uuid")
    public ResponseEntity<Set<AccountIpAddress>> getAccountIpAddressByAccountUuid(@RequestParam(value = "uuid") UUID uuid,
                                                                                  Sort sort) {
        log.atFiner().log("Request for getting Set of AccountIpAddress for Account with UUID " +
                "[%s] and Sort [%s]", uuid, sort);
        return ResponseEntity.ok(accountIpAddressService.getAccountIpAddressByAccountUuid(uuid, sort));
    }
}
