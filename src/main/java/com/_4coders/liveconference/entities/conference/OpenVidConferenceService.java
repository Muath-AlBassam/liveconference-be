package com._4coders.liveconference.entities.conference;

import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.conference.OpenVidConferenceNotExisting;
import io.openvidu.java.client.*;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Flogger
public class OpenVidConferenceService {

    //user: OPENVIDUAPP, pass: [your private secret])
    private final static String OPEN_VID_URL = "https://localhost:4443/";
    private final static String SECRET = "MY_SECRET";
    private final static OpenVidu openVidu = new OpenVidu(OPEN_VID_URL, SECRET);


    public Session createConference() throws UUIDUniquenessException, OpenViduJavaClientException, OpenViduHttpException {
        UUID uniqueUUID = UUID.randomUUID();
        Session fetchedSession = getSessionByID(uniqueUUID);
        if (fetchedSession != null) {
            byte counterLimit = 0;
            while (fetchedSession.getSessionId().equals(uniqueUUID.toString()) && counterLimit >= 0) {//may change to
                // handle exception to reduce performance overhead and memory usage
                uniqueUUID = UUID.randomUUID();
                counterLimit++;//play with overflow
                log.atFinest().every(16).log("[%d] iterations has gone from 128 to find unique UUID", counterLimit + 1);
                fetchedSession = getSessionByID(uniqueUUID);
            }
            if (counterLimit < 0) {// no unique UUID was found
                log.atSevere().log("Can't initiate a new OpenVid Conference as no unique UUID was found");
                log.atSevere().log("Throwing the exception [$s]", UUIDUniquenessException.class);
                throw new UUIDUniquenessException(String.format("No unique UUID was found after [%d] tries", counterLimit),
                        counterLimit);
            } else {
                return createSessionWithUUID(uniqueUUID);
            }
        } else {
            return createSessionWithUUID(uniqueUUID);
        }
    }

    public String generateToken(UUID sessionID, OpenViduRole role) throws OpenViduJavaClientException,
            OpenViduHttpException, OpenVidConferenceNotExisting {
        log.atFinest().log("Generating Toke for sessionID [%s] with Role [%s]", sessionID, role);
        Session fetchedSession = getSessionByID(sessionID);
        if (fetchedSession == null) {
            log.atFinest().log("Throwing OpenVidConferenceNotExisting as no conference was found with the given " +
                    "sessionID [%s]", sessionID);
            throw new OpenVidConferenceNotExisting(String.format("Throwing OpenVidConferenceNotExisting as no conference was found with the given " +
                    "sessionID  [%s]", sessionID), sessionID);
        } else {
            TokenOptions tokenOptions = new TokenOptions.Builder().role(role).build();
            return fetchedSession.generateToken(tokenOptions);
        }
    }

    public Session getConference(UUID sessionID) throws OpenViduJavaClientException, OpenViduHttpException {
        log.atFinest().log("Retrieving Session with UUID [%s]", sessionID);
        return getSessionByID(sessionID);
    }

    public boolean closeConference(UUID sessionID) throws OpenViduJavaClientException, OpenViduHttpException, OpenVidConferenceNotExisting {
        log.atFinest().log("Closing OpenVid Session for sessionID [%s] ", sessionID);
        Session fetchedSession = getSessionByID(sessionID);
        if (fetchedSession == null) {
            log.atFinest().log("Throwing OpenVidConferenceNotExisting as no conference was found with the given " +
                    "sessionID [%s]", sessionID);
            throw new OpenVidConferenceNotExisting(String.format("Throwing OpenVidConferenceNotExisting as no conference was found with the given " +
                    "sessionID  [%s]", sessionID), sessionID);
        } else {
            fetchedSession.close();
            return true;
        }
    }

    //Think shall we provide retrieving all session or not (i think not)

    private Session createSessionWithUUID(UUID uuid) throws OpenViduJavaClientException, OpenViduHttpException {
        log.atFinest().log("Creating OpenVidu session with UUID [%s]", uuid);
        SessionProperties sessionProperties =
                new SessionProperties.Builder().customSessionId(uuid.toString()).build();
        return openVidu.createSession(sessionProperties);
    }

    private Session getSessionByID(UUID sessionID) throws OpenViduJavaClientException, OpenViduHttpException {
        log.atFinest().log("Updating session info from OpenVidu");
        boolean changedHappened = openVidu.fetch();
        log.atFine().log("Update resulted in changes [%b]", changedHappened);
        Optional<Session> requiredSession =
                openVidu.getActiveSessions().stream().filter(session -> session.getSessionId().equals(sessionID.toString())).findFirst();//results in 1 only
        return requiredSession.orElse(null);
    }

}
