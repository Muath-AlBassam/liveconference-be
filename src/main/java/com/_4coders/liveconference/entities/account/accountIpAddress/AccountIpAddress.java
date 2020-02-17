package com._4coders.liveconference.entities.account.accountIpAddress;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccountViews;
import com._4coders.liveconference.entities.ipAddress.IpAddress;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts_ipAddresses")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AccountIpAddress extends RepresentationModel<AccountIpAddress> implements Serializable {
    @Transient
    private static final long serialVersionUID = 8798401497231234L;

    @EmbeddedId
    @EqualsAndHashCode.Include
    private AccountIpAddressKey key;

    @ManyToOne
    @MapsId("accountID")
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    @ManyToOne
    @MapsId("ipAddressID")
    @JoinColumn(name = "fk_ipAddress_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            AccountIpAddressViews.OwnerDetails.class, AccountIpAddressViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class})
    private IpAddress ipAddress;

    @Column(name = "date_of_matching", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            AccountIpAddressViews.OwnerDetails.class, AccountIpAddressViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private Date dateOfMatching;


    @Column(name = "last_date_used", nullable = false)
    @LastModifiedDate
    @JsonView({AccountViews.SupportAll.class,
            UserViews.SupportAll.class,
            AccountIpAddressViews.SupportAll.class,
            BlockedAccountViews.SupportMedium.class})
    private Date lastDateUsed;

    @Column(name = "is_authroized", nullable = false)
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            AccountIpAddressViews.OwnerDetails.class, AccountIpAddressViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            BlockedAccountViews.SupportMedium.class})
    private Boolean isAuthorized;

}
