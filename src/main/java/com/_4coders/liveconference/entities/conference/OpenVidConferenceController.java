package com._4coders.liveconference.entities.conference;

import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.conference.OpenVidConferenceNotExisting;
import com._4coders.liveconference.validator.UUIDConstraint;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@Flogger
@Validated
@RequestMapping("/flogger/o_conference")
public class OpenVidConferenceController {

    @Autowired
    private OpenVidConferenceService openVidConferenceService;


    @PostMapping
    public ResponseEntity<Session> createConference() {
        log.atFinest().log("Request for creating openVid session was received");
        try {
            Session createdSession = openVidConferenceService.createConference();
            return ResponseEntity.ok(createdSession);
        } catch (UUIDUniquenessException | OpenViduJavaClientException | OpenViduHttpException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(params = {"sessionId", "role"})
    public ResponseEntity<String> generateToken(@RequestParam(value = "sessionId") @UUIDConstraint UUID sessionID,
                                                @RequestParam(value = "role") @NotNull OpenViduRole role) {
        log.atFinest().log("Request for generating Token for sessionID [%s] with Role [%s] was received", sessionID, role);

        try {
            String fetchedToken = openVidConferenceService.generateToken(sessionID, role);
            return ResponseEntity.ok(fetchedToken);
        } catch (OpenVidConferenceNotExisting ex) {
            return ResponseEntity.badRequest().body(null);
        } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(params = "sessionId")
    public ResponseEntity<Session> getConference(@RequestParam(value = "sessionId") @UUIDConstraint UUID sessionID) {
        log.atFinest().log("Request for retrieving OpenVid conference with sessionID [%s]was received", sessionID);
        try {
            Session fetchedSession = openVidConferenceService.getConference(sessionID);
            return ResponseEntity.ok(fetchedSession);
        } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(params = "sessionId")
    public ResponseEntity<Boolean> closeConference(@RequestParam(value = "sessionId") @UUIDConstraint UUID sessionID) {
        log.atFinest().log("Request for closing OpenVid conference with sessionID [%s] was received", sessionID);
        try {
            boolean result = openVidConferenceService.closeConference(sessionID);
            return ResponseEntity.ok(result);
        } catch (OpenVidConferenceNotExisting ex) {
            return ResponseEntity.badRequest().body(null);
        } catch (OpenViduJavaClientException | OpenViduHttpException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
