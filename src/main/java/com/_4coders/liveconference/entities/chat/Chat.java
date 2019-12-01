package com._4coders.liveconference.entities.chat;

import com._4coders.liveconference.entities.Auditing.AuditingEntity;
import com._4coders.liveconference.entities.message.Message;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Represent the {@code Chat} between 2 {@code Users}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@Table(name = "chat")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Chat extends AuditingEntity {

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

    /**
     * Represent the {@code User} that started the chat
     */
    @OneToOne
    @JoinColumn(name = "fk_user_initiator", referencedColumnName = "id", nullable = false, updatable = false)
    private User initiator;

    /**
     * Represent the target {@code User} of the chat that the {@code initiator} onend with
     */
    @OneToOne
    @JoinColumn(name = "fk_user_target", referencedColumnName = "id", nullable = false, updatable = false)
    private User target;

    @OneToMany
    @JoinColumn(name = "fk_message", referencedColumnName = "id")
    private Set<Message> messages;//TODO: change to Page(must)
}
