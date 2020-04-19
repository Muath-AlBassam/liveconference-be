package com._4coders.liveconference.entities.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Encapsulation of the {@code Account} class that will be used in authentication and in session while user is on
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 26/1/2020
 */
@Getter
@ToString
@EqualsAndHashCode
public class AccountDetails implements UserDetails {
    private Account account;

    public AccountDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles().stream().map(systemRole -> new SimpleGrantedAuthority(systemRole.getName())).collect(Collectors.toSet());
    }

    /**
     * @return the email of the {@link Account}
     */
    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getIsActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.getIsBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getIsActivated();//TODO FIX HERE
    }

    @Override
    public boolean isEnabled() {
        return account.getIsActivated() && !account.getIsBlocked();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }
}
