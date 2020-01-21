package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.role.system.SystemRole;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
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
public class Account extends RepresentationModel<Account> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @EqualsAndHashCode.Include
    private UUID uuid;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "TEXT")
    @Email
    private String email;

    @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
    private String firstName;

    @Column(name = "middle_name", columnDefinition = "TEXT")
    private String middleName;

    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true, columnDefinition = "TEXT")
    @Size(min = 14, max = 14)//Based on KSA numbering system
    private String phoneNumber;

    @Column(name = "is_activated", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActivated;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address address;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<User> users;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private Date registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @ManyToMany
    @JoinTable(name = "accounts_system_roles",
            joinColumns = @JoinColumn(name = "fk_account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_system_roles_id", referencedColumnName = "id"))
    private Set<SystemRole> roles;

    //TODO add blockedAccounts and IPAddresses

}
