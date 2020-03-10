package com._4coders.liveconference.entities.user;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccountViews;
import com._4coders.liveconference.entities.setting.user.UserSetting;
import com._4coders.liveconference.entities.user.friend.FriendView;
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
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * The main way the user can use the system after login
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor //TODO may cause error
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends RepresentationModel<User> implements Serializable {

    @Transient
    private static final long serialVersionUID = 1224321388765124L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_users"
    )
    @GenericGenerator(
            name = "native_users",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportMedium.class, AccountViews.OwnerInformation.class,
            UserViews.OwnerDetails.class, UserViews.Others.class,
            BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.SupportMedium.class,
            FriendView.OwnerDetails.class, FriendView.Others.class})
    private UUID uuid;
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportMedium.class, AccountViews.OwnerInformation.class,
            UserViews.OwnerDetails.class, UserViews.Others.class,
            BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.SupportMedium.class,
            FriendView.OwnerDetails.class, FriendView.Others.class})
    private String userName;
    //the AccountViews.OwnerInformation.class, comes from BlockedAccount
    @Column(name = "last_login")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.OwnerInformation.class, UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class})
    private Date lastLogin;
    /**
     * Represent the current status of the user
     */
    @Column(name = "status", nullable = false)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.SupportMedium.class,
            UserViews.OwnerDetails.class, UserViews.Others.class,
            BlockedAccountViews.SupportMedium.class,
            FriendView.OwnerDetails.class, FriendView.Others.class})
    private UserStatus status;

    @Column(name = "last_status", nullable = false)
    @JsonIgnore
    private UserStatus lastStatus;


    //ADD LAST STATUS
    /**
     * <p>Represent wither this user has been deleted or not</p>
     * <p>usually this will be false or null</p>
     */
    @Column(name = "is_deleted")
    @JsonView({AccountViews.SupportMedium.class,
            UserViews.OwnerInformation.class, UserViews.Others.class,
            BlockedAccountViews.SupportMedium.class,
            FriendView.OwnerDetails.class, FriendView.Others.class})
    //Others is given here for the case of user messaging a deleted user he should be able to know that he is deleted
    // or not (discuss with the team)
    private Boolean isDeleted;
    @Transient
    @JsonView({UserViews.Others.class})
    private Boolean isFriend;//THINK may remove
    @Transient
    @JsonView({UserViews.Others.class})
    private Boolean isBlocked;
    /**
     * Represent the owner of this user
     */
    @ManyToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id", nullable = false)
    @JsonView({UserViews.Others.class})
    //view Owner... won't be set here because when the User has logged in with his account the Account data should be
    // saved by the consumer of the API and returning the Account with each user object is bad for the size of message
    private Account account;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, optional = false)
    @JsonView({UserViews.OwnerDetails.class})
//        @JsonView({AccountViews.OwnerDetails.class,AccountViews.SupportMedium.class}) only if setting contains the logo
    private UserSetting userSetting;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.OwnerInformation.class, UserViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private Date creationDate;

    //todo add friends, groups, cards and categories
    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    @JsonView({AccountViews.SupportAll.class,
            UserViews.OwnerInformation.class, UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class})
    private Date lastModifiedDate;


    //    @PrePersist
//    private void setUUID() {
//        uuid = UUID.randomUUID();
//    }
}
