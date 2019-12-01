package com._4coders.liveconference.entities.address;

import com._4coders.liveconference.entities.account.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
public class Address extends RepresentationModel<Address> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "country", nullable = false, columnDefinition = "TEXT")
    private String country;

    @Column(name = "province", columnDefinition = "TEXT")
    private String province;

    @Column(name = "city", nullable = false, columnDefinition = "TEXT")
    private String city;

    @Column(name = "street", nullable = false, columnDefinition = "TEXT")
    private String street;

    @Column(name = "building_number", columnDefinition = "TEXT")
    private String buildingNumber;

    @Column(name = "postal_code", nullable = false, columnDefinition = "TEXT")
    private String postalCode;

    @Column(name = "zip_code", nullable = false, columnDefinition = "TEXT")
    private String zipCode;

    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "phone_number", nullable = false, columnDefinition = "TEXT")
    @Size(min = 14, max = 14)//Based on KSA numbering system
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id")
    @JsonBackReference
    private Account account;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;
}
