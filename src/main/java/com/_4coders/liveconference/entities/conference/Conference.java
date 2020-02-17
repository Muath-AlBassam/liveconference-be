package com._4coders.liveconference.entities.conference;

import com._4coders.liveconference.entities.channel.Channel;
import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "conferences")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Conference extends RepresentationModel<Conference> implements Serializable {

    @Transient
    private static final long serialVersionUID = -48321237218763214L;

    @Id
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;


    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    @NotBlank//TODO check if @URL is better here
    private String URL;

    @Column(name = "initiation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date initiationDate;

    @OneToOne
    @MapsId("id")
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, updatable = false)
    private Channel ownerChannel;

    @ManyToOne
    @JoinColumn(name = "fk_user_initiator", nullable = false, updatable = false)
    private User initiator;

    @OneToMany(mappedBy = "conference")
    private Set<ConferenceUser> conferenceUsers;
}
