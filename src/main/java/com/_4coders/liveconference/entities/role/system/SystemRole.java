package com._4coders.liveconference.entities.role.system;

import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.permission.system.SystemPermission;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Roles groups 1 -> N permission which easies the grouping of users
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 21/1/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "system_roles")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SystemRole extends RepresentationModel<SystemRole> implements Serializable {

    @Transient
    private static final long serialVersionUID = 47843213413671264L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private UUID uuid;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "system_roles_system_permissions",
            joinColumns = @JoinColumn(name = "fk_system_roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_system_permissions_id", referencedColumnName = "id"))
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private Set<SystemPermission> permissions;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({AccountViews.SupportAll.class, UserViews.SupportAll.class})
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    @JsonView({AccountViews.Admin.class, UserViews.Admin.class})
    private Date lastModifiedDate;

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
