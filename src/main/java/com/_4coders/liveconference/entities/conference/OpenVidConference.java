package com._4coders.liveconference.entities.conference;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@RedisHash("openvid_conference")
public class OpenVidConference implements Serializable {

    @Transient
    private static final long serialVersionUID = -48321237004214L;

    private UUID uuid;
}
