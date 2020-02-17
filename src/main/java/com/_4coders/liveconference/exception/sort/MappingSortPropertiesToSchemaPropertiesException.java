package com._4coders.liveconference.exception.sort;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that the mapping from the given {@code Sort} properties to the schema properties has failed because of a
 * wrong named properties in the {@code Sort}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 11/02/2020
 */
@Getter
@Flogger
public class MappingSortPropertiesToSchemaPropertiesException extends RuntimeException {
    private String wrongNamedSortProperty;

    public MappingSortPropertiesToSchemaPropertiesException(String message, String wrongNamedSortProperty) {
        super(message);
        log.atFine().log("MappingSortPropertiesToSchemaPropertiesException was thrown with message [%s] and " +
                "wrong named sort property [%s]", message, wrongNamedSortProperty);
        this.wrongNamedSortProperty = wrongNamedSortProperty;
    }

    public MappingSortPropertiesToSchemaPropertiesException(String message) {
        super(message);
        log.atFine().log("MappingSortPropertiesToSchemaPropertiesException was thrown with message [%s]", message);
    }
}
