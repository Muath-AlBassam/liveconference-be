package com._4coders.liveconference.entities.message;

import com._4coders.liveconference.entities.Auditing.AuditingEntity;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Message extends AuditingEntity {

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
     * this attribute represent the content of the message sent
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
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

    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Column(name = "type", nullable = false, updatable = false)
    private Type type;
}
