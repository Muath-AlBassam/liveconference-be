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

    @OneToOne
    @JoinColumn(name = "fk_user_initiator", referencedColumnName = "id", nullable = false, updatable = false)
    private User initiator;

    @OneToOne
    @JoinColumn(name = "fk_user_target", referencedColumnName = "id", nullable = false, updatable = false)
    private User target;

    @OneToMany
    @JoinColumn(name = "fk_message", referencedColumnName = "id")
    private Set<Message> messages;
}
