package com._4coders.liveconference.entities.user;

import com._4coders.liveconference.entities.Auditing.AuditingEntity;
import com._4coders.liveconference.entities.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends AuditingEntity {

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

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "TEXT")
    private String userName;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "status", nullable = false)
    private Status status;


    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id")
    private Account account;

    //todo add friends, groups, setting, cards and categories
}
