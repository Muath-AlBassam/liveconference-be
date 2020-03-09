package com._4coders.liveconference.exception.conference;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

import java.util.UUID;

@Getter
@Flogger
public class OpenVidConferenceNotExisting extends RuntimeException {
    private UUID sessionId;

    public OpenVidConferenceNotExisting(String message, UUID sessionId) {
        super(message);
        log.atFinest().log("OpenVidConferenceNotExisting was thrown with message [%s] and sessionID [%s]", message, sessionId);
        this.sessionId = sessionId;
    }

    public OpenVidConferenceNotExisting(String message) {
        super(message);
        log.atFinest().log("OpenVidConferenceNotExisting was thrown with message [%s]", message);
    }
}
