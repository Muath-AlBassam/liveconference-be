package com._4coders.liveconference.entities.chat;

import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represent the {@code Chat} between 2 {@code Users}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chats")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Chat extends RepresentationModel<Chat> implements Serializable {

    @Transient
    private static final long serialVersionUID = 249785431218787542L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_chats"
    )
    @GenericGenerator(
            name = "native_chats",
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

//    @OneToMany
//    @JoinColumn(name = "fk_message", referencedColumnName = "id")
//    private Set<Message> messages;//TODO: change to Page(must) in service

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
