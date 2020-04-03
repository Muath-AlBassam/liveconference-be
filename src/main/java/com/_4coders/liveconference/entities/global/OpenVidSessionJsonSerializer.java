package com._4coders.liveconference.entities.global;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.Session;
import lombok.extern.flogger.Flogger;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@Flogger
@JsonComponent
public class OpenVidSessionJsonSerializer extends JsonSerializer<Session> {
    @Override
    public void serialize(Session session, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();//{
        try {
            jsonGenerator.writeStringField("id", session.getSessionId());
            jsonGenerator.writeObjectField("createdAt", session.createdAt());
            jsonGenerator.writeBooleanField("recorded", session.isBeingRecorded());
            jsonGenerator.writeFieldName("connections");
            jsonGenerator.writeStartArray();
            for (Connection connection : session.getActiveConnections()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("connectionId", connection.getConnectionId());
                jsonGenerator.writeStringField("token", connection.getToken());
                jsonGenerator.writeStringField("platform", connection.getPlatform());
                jsonGenerator.writeObjectField("role", connection.getRole());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeFieldName("properties");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("customSessionId", session.getProperties().customSessionId());
            jsonGenerator.writeObjectField("recordingMode", session.getProperties().recordingMode());
            jsonGenerator.writeObjectField("mediaMode", session.getProperties().mediaMode());
            jsonGenerator.writeObjectField("defaultRecordingLayout", session.getProperties().defaultRecordingLayout());
            jsonGenerator.writeObjectField("defaultOutputMode", session.getProperties().defaultOutputMode());
            jsonGenerator.writeEndObject();
            jsonGenerator.writeFieldName("clientData");
            jsonGenerator.writeStartArray();
            session.getActiveConnections().forEach(connection -> {
                try {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("connectionId", connection.getConnectionId());
                    jsonGenerator.writeStringField("data", connection.getClientData());
                    jsonGenerator.writeEndObject();
                } catch (IOException e) {
                    log.atConfig().log("Error in writing OpenVidu Session object to Json exception message [%s] " +
                                    "inside the active session",
                            e.getMessage());
                }

            });
            jsonGenerator.writeEndArray();

        } catch (IOException e) {
            log.atConfig().log("Error in writing OpenVidu Session object to Json exception message [%s]",
                    e.getMessage());
        }
        jsonGenerator.writeEndObject();
    }

    @Override
    public Class<Session> handledType() {
        return Session.class;
    }
}
