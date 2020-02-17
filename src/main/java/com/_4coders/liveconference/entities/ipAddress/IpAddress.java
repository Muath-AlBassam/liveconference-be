package com._4coders.liveconference.entities.ipAddress;

import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddress;
import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddressViews;
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
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ipAddresses")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class IpAddress extends RepresentationModel<IpAddress> implements Serializable {
    @Transient
    private static final long serialVersionUID = 4800489213413210451L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ip", nullable = false, unique = true, updatable = false, columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            AccountIpAddressViews.OwnerDetails.class, AccountIpAddressViews.SupportLittle.class,
            UserViews.SupportLittle.class,
            BlockedAccountViews.SupportMedium.class})
    private String ip;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "organization", columnDefinition = "TEXT")
    private String organization;

    @Column(name = "postal", columnDefinition = "TEXT")
    private String postal;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ipAddress__location_information_id", referencedColumnName = "id", nullable = false)
    private LocationInformation locationInformation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ipAddress__asn_id", referencedColumnName = "id", nullable = false)
    private Asn asn;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ipAddress__carries_id", referencedColumnName = "id")
    private Carrier carrier;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "ipAddresses_ipAddress__languages",
            joinColumns = @JoinColumn(name = "fk_ipAddresses_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_ipAddress__languages_fk", referencedColumnName = "id"))
    private Set<Language> languages;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "ipAddress")
    @ToString.Exclude
    private Set<AccountIpAddress> accounts;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ipAddress__currency_id", referencedColumnName = "id")
    private Currency currency;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_ipAddress__threats_id", referencedColumnName = "id")
    private Threat threat;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Transient
    private int count;
    @Transient
    private String flagLink;
    @Transient
    private String emojiFlag;
    @Transient
    private String emojiUnicode;

}
