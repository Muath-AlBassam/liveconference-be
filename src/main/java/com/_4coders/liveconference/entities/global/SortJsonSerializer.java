package com._4coders.liveconference.entities.global;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.flogger.Flogger;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Sort;

import java.io.IOException;

/**
 * For serializing {@link Sort} so that it shows what is being sorted rather than saying it's sorted
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 06/03/2020
 */
@Flogger
@JsonComponent
public class SortJsonSerializer extends JsonSerializer<Sort> {
    @Override
    public void serialize(Sort sort, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();//[

        sort.forEach(order -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("property", order.getProperty());
                jsonGenerator.writeStringField("direction", order.getDirection().toString());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                log.atConfig().log("Error in writing Sort object to Json exception message [%s]", e.getMessage());
            }
        });
        jsonGenerator.writeEndArray();
    }

    @Override
    public Class<Sort> handledType() {
        return Sort.class;
    }
}
