package com._4coders.liveconference.entities.address;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccountViews;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Represent the address of an {@code Account}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "addresses")
@Data
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Address extends RepresentationModel<Address> implements Serializable {

    @Transient
    private static final long serialVersionUID = -9427451234L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "country", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String country;

    @Column(name = "province", columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String province;

    @Column(name = "city", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String city;

    @Column(name = "street", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String street;

    @Column(name = "building_number", columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String buildingNumber;

    @Column(name = "postal_code", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String postalCode;

    @Column(name = "zip_code", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String zipCode;

    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String fullName;

    @Column(name = "phone_number", nullable = false, columnDefinition = "TEXT")
    @Size(min = 13, max = 13)//Based on KSA numbering system
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    @JsonView({AccountViews.OwnerInformation.class, AccountViews.SupportMedium.class,
            UserViews.SupportMedium.class,
            SystemBlockedAccountViews.Admin.class})
    private Date lastModifiedDate;
}
