package com._4coders.liveconference.entities.permission.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * permission to allow access for resources or action in the group
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 21/1/2020
 */
@Entity
@Table(name = "group_permissions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GroupPermission extends RepresentationModel<GroupPermission> implements Serializable {

    @Transient
    private static final long serialVersionUID = 72123712318792318L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_group_permissions"
    )
    @GenericGenerator(
            name = "native_group_permissions",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    /**
     * the action this permission allows
     */
    @Column(name = "action", nullable = false, unique = true, updatable = false, columnDefinition = "TEXT")
    private String action;


    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}

