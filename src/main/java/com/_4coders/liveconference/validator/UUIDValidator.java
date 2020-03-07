package com._4coders.liveconference.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<UUIDConstraint, UUID> {
    public void initialize(UUIDConstraint constraint) {
    }

    public boolean isValid(UUID uuid, ConstraintValidatorContext context) {
        return uuid.toString().matches("[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}");
        //validate the uuid using regex and only validates the ones with version 4 others are false
    }
}
