package com._4coders.liveconference.entities.message;

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
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represent the {@code Message} sent to other {@code Users}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "messages")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Message extends RepresentationModel<Message> implements Serializable {

    @Transient
    private static final long serialVersionUID = 873127984140146545L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_messages"
    )
    @GenericGenerator(
            name = "native_messages",
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
     * this attribute represent the content of the message sent
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @NotEmpty
    private String content;

    @OneToOne
    @JoinColumn(name = "fk_user_sender", referencedColumnName = "id", nullable = false, updatable = false)
    private User sender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_deleted_by", referencedColumnName = "id")
    private User deletedBy;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_date")
    private Date deletedDate;

    @Column(name = "type", nullable = false, updatable = false)
    private MessageType type;

    @Column(name = "sent_date", nullable = false, updatable = false)
    @CreatedDate
    private Date sentDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
