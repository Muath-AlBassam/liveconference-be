package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddress;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccount;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccountViews;
import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccount;
import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccountViews;
import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.role.system.SystemRole;
import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * The main entry point for users of the system where each {@code Account} has many {@code Users}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Account extends RepresentationModel<Account> implements Serializable {

    @Transient
    private static final long serialVersionUID = 478432134132211L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_account"
    )
    @GenericGenerator(
            name = "native_account",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.Others.class,
            UserViews.Others.class,
            BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private UUID uuid;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "TEXT")
    @Email
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String email;

    @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String firstName;

    @Column(name = "middle_name", columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String middleName;

    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true, columnDefinition = "TEXT")
    @Size(min = 13, max = 13)//Based on KSA numbering system
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String phoneNumber;

    /**
     * indicate wither this account has gone throw the activation and verification steps<br/>
     * ex. email verification
     */
    @Column(name = "is_activated", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private Boolean isActivated;

    /**
     * indicate wither this account is blocked or not
     */
    @Transient
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class})
    private Boolean isBlocked;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Valid
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private Address address;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportMedium.class})
    @ToString.Exclude
    private Set<User> users;

    @OneToOne
    @JoinColumn(name = "fk_default_user_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportMedium.class})
    @ToString.Exclude
    private User defaultUser;

    /**
     * This is used only in the backEnd (here (:) and will be used in most of places that depend on the current
     * user<br/>
     * by default the {@code currentInUseUser} will be the {@code defaultUser} but it may change based on the user's
     * actions
     */
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private User currentInUseUser;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private LocalDateTime registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private LocalDateTime lastModifiedDate;

    @Column(name = "last_login_date")
    @Setter(AccessLevel.PRIVATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})//may change info... to deta...
    private LocalDateTime lastLoginDate;

    @Column(name = "last_logout_date")
    @Setter(AccessLevel.PRIVATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.SupportAll.class, UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private LocalDateTime lastLogoutDate;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "account")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class})
    @ToString.Exclude
    private Set<AccountIpAddress> ipAddresses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class})
    @ToString.Exclude
    private Set<SystemBlockedAccount> systemBlockedAccountHistory;

    /**
     * Shows the {@code BlockedAccount} that this {@code Account} has blocked
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "blocker")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class})
    @ToString.Exclude
    private Set<BlockedAccount> blockedAccounts;//think may change to Page


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinTable(name = "accounts_system_roles",
            joinColumns = @JoinColumn(name = "fk_account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_system_roles_id", referencedColumnName = "id"))
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private Set<SystemRole> roles;


//    @PrePersist may remove all of these or play it with faith or maybe another solution can be found
//    private void setUUID() {
//        uuid = UUID.randomUUID();
//    }

}
