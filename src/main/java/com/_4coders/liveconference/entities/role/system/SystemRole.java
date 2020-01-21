package com._4coders.liveconference.entities.role.system;

import com._4coders.liveconference.entities.permission.system.SystemPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
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
public class SystemRole extends RepresentationModel<SystemRole> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "TEXT")
    private String name;

    @ManyToMany
    @JoinTable(name = "system_roles_system_permissions",
            joinColumns = @JoinColumn(name = "fk_system_roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_system_permissions_id", referencedColumnName = "id"))
    @JsonManagedReference
    private Set<SystemPermission> permissions;

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
