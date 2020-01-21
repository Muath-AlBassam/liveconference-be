package com._4coders.liveconference.entities.user;

import com._4coders.liveconference.entities.account.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends RepresentationModel<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "TEXT")
    private String userName;

    @Column(name = "last_login")
    private Date lastLogin;

    /**
     * Represent the current status of the user
     */
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * <p>Represent wither this user has been deleted or not</p>
     * <p>usually this will be false or null</p>
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * Represent the owner of this user
     */
    @ManyToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference //TODO DISCUSS LATER
    private Account account;

    //todo add friends, groups, setting, cards and categories

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
