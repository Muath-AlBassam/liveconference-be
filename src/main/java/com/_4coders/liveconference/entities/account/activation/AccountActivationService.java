package com._4coders.liveconference.entities.account.activation;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountService;
import com._4coders.liveconference.exception.account.AccountActivationCodeMatchException;
import com._4coders.liveconference.exception.account.AccountActivationExpiredException;
import com._4coders.liveconference.exception.account.AccountAlreadyActivatedException;
import com._4coders.liveconference.exception.account.AccountNotFoundException;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Flogger
public class AccountActivationService {
    @Autowired
    private AccountActivationRepository accountActivationRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Sets new {@link AccountActivation} for the given {@link Account}.<br/>
     * note that any previously active codes will be disposed of when this method is called as a new one will be made
     *
     * @param account the {@link Account} which the {@link AccountActivation} will be set for
     * @param reason  the {@code Reason} for a new code
     * @return {@link AccountActivation} that got assigned for the given {@link Account}
     * @throws AccountNotFoundException if the given {@link Account} doesn't exist
     */
    public AccountActivation setActivationForAccount(Account account, String reason) throws AccountNotFoundException, AccountAlreadyActivatedException {
        log.atFinest().log("Setting an activation code for the Account with ID [%d]", account.getId());
        if (!accountService.existAccountById(account.getId())) {
            log.atFinest().log("Throwing AccountNotFoundException as there exists no account with the given ID [%d]",
                    account.getId());
            throw new AccountNotFoundException(String.format("No account was found with the given ID [%d]",
                    account.getId()), account.getId());
        }
        if (account.getIsActivated()) {
            log.atFinest().log("Throwing AccountAlreadyActivatedException as we're trying to set activation code for " +
                    "an already activated Account");
            throw new AccountAlreadyActivatedException(String.format("The given Account is already activated, account" +
                    " email [%s]", account.getEmail()));
        }
        log.atFinest().log("Checking that the account doesn't have an active code...");
        final AccountActivation fetchedAccountActivation = accountActivationRepository.getAccountActivationByAccountToActivate_IdAndActivatedIsNullAndExpiryDateAfter(account.getId(), LocalDateTime.now());
        if (fetchedAccountActivation != null) {//previous code still exist and not used
            fetchedAccountActivation.setActivated(false);//end the code (deny it's usage)
            accountActivationRepository.save(fetchedAccountActivation);//update the code
            return setActivationForAccount(account, reason);//recursive so that a new code get created
        } else {//no previous code exist and not used, thus create a new one
            AccountActivation accountActivation = new AccountActivation();
            accountActivation.setUp(reason);
            accountActivation.setAccountToActivate(account);
            log.atFine().log("Activation Code: [%s]", accountActivation.getCode());
            return accountActivationRepository.save(accountActivation);
        }
    }

    /**
     * Validates the given {@code ActivationCode} and set's it's activation status to true if the give {@code
     * ActivationCode} is correct
     *
     * @param account        the Account that the given {@code ActivationCode} is associated with
     * @param activationCode the {@code ActivationCode}
     * @throws AccountActivationCodeMatchException if the given {@code ActivationCode} doesn't match the fetched one
     */
    public void activateAccountActivation(Account account, String activationCode) throws AccountActivationCodeMatchException {
        AccountActivation fetchedAccountActivation =
                accountActivationRepository.getAccountActivationByAccountToActivate_IdAndActivatedIsNullAndExpiryDateAfter(account.getId(), LocalDateTime.now());
        if (fetchedAccountActivation == null) {//either non-exist or expiry date is over
            log.atFinest().log("Throwing AccountActivationExpiredException as no AccountActivation code was " +
                    "fount for the Account with Email [%s]", account.getEmail());
            throw new AccountActivationExpiredException(String.format("No AccountActivation Code was found " +
                    "for the given Account with Email [%s]", account.getEmail()), account.getEmail());
        } else {//was found
            if (!fetchedAccountActivation.getCode().equals(activationCode)) {
                log.atFinest().log("Throwing AccountActivationCodeMatchException as the given code [%s] doesn't match" +
                        "the fetched one [%s]", activationCode, fetchedAccountActivation.getCode());
                throw new AccountActivationCodeMatchException(String.format("the given code [%s] doesn't match the " +
                        "fetched one [%s]", activationCode, fetchedAccountActivation.getCode()), activationCode,
                        fetchedAccountActivation.getCode());
            } else {
                fetchedAccountActivation.setActivated(true);
                accountActivationRepository.save(fetchedAccountActivation);
            }
        }
    }
}

