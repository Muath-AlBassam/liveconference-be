package com._4coders.liveconference.entities.conference;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonView(ConferenceViews.Others.class)
public class OpenVidToken implements Serializable {

    private static final long serialVersionUID = -4837004214L;

    private String token;
    private String sessionId;
    private Role role;
    private String data;

    enum Role {
        SUBSCRIBER, PUBLISHER, MODERATOR
    }
}
