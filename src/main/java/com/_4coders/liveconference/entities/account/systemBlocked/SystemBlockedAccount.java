package com._4coders.liveconference.entities.account.systemBlocked;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccountViews;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "system_blockedAccounts")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SystemBlockedAccount extends RepresentationModel<SystemBlockedAccount> implements Serializable {

    @Transient
    private static final long serialVersionUID = 1720475L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id", nullable = false)
    @JsonView(SystemBlockedAccountViews.Admin.class)
    private Account account;

    @Column(name = "is_blocked", nullable = false)
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private boolean isBlocked;

    @Column(name = "is_permanent")
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private boolean isPermanent;

    @Column(name = "block_reason", nullable = false, columnDefinition = "TEXT")
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private String blockReason;

    @Column(name = "block_remove_reason", columnDefinition = "TEXT")
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private String blockRemovedReason;

    @Column(name = "blocked_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private LocalDateTime blockedDate;

    @Column(name = "block_removed_date")
    @LastModifiedDate
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            BlockedAccountViews.SupportAll.class,
            SystemBlockedAccountViews.Admin.class})
    private LocalDateTime blockRemovedDate;

}
