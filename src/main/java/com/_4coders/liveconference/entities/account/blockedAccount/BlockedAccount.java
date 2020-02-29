package com._4coders.liveconference.entities.account.blockedAccount;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "blocked_accounts")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BlockedAccount extends RepresentationModel<BlockedAccount> implements Serializable {

    @Transient
    private static final long serialVersionUID = 75123187L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_blocked_accounts"
    )
    @GenericGenerator(
            name = "native_blocked_accounts",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_account_blocker_id", referencedColumnName = "id", nullable = false)
    private Account blocker;

    @ManyToOne
    @JoinColumn(name = "fk_account_blocked_id", referencedColumnName = "id", nullable = false)
    @JsonView({BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.SupportMedium.class})
    private Account blocked;

    /**
     * indicate which user caused the blockade
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_blocked_id", referencedColumnName = "id", nullable = false)
    @JsonView({AccountViews.SupportMedium.class, AccountViews.OwnerInformation.class,
            BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.SupportMedium.class})
    private User blockedUser;

    /*may add logo later and the other things or just get it from the User by AccountViews.OwnerInformation.class,*/
//    @Transient
//    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
//            BlockedAccountViews.SupportMedium.class})
//    private UUID blockUserUuid;
//
//    @Transient
//    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
//            BlockedAccountViews.SupportMedium.class})
//    private String blockedUserName;

    @Column(name = "is_blocked", nullable = false)
    @JsonView({AccountViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private boolean isBlocked;

    @Column(name = "block_reason", columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private String blockReason;

    @Column(name = "block_remove_reason", columnDefinition = "TEXT")
    @JsonView({AccountViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private String blockRemovedReason;

    @Column(name = "blocked_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private LocalDateTime blockedDate;

    @Column(name = "block_removed_date")
    @LastModifiedDate
    @JsonView({AccountViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private LocalDateTime blockRemovedDate;
}
