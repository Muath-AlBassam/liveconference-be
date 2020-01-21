package com._4coders.liveconference.entities.permission.system;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
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
public class SystemPermission extends RepresentationModel<SystemPermission> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "action", nullable = false, unique = true, columnDefinition = "TEXT")
    private String action;

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
