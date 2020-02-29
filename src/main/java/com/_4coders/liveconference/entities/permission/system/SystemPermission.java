package com._4coders.liveconference.entities.permission.system;


import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * permission to allow access for resources or action in the system
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 21/1/2020
 */
@Entity
@Table(name = "system_permissions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SystemPermission extends RepresentationModel<SystemPermission> implements Serializable {

    @Transient
    private static final long serialVersionUID = -985623178561234L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_system_permissions"
    )
    @GenericGenerator(
            name = "native_system_permissions",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private UUID uuid;

    /**
     * the action this permission allows
     */
    @Column(name = "action", nullable = false, unique = true, updatable = false, columnDefinition = "TEXT")
    @JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.SupportLittle.class,
            UserViews.SupportLittle.class})
    private String action;

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
