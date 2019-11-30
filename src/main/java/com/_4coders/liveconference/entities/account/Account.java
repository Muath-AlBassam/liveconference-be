package com._4coders.liveconference.entities.account;

import com._4coders.liveconference.entities.Auditing.AuditingEntity;
import com._4coders.liveconference.entities.address.Address;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Account extends AuditingEntity {

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

    //TODO add blockedAccounts and IPAddresses

}
