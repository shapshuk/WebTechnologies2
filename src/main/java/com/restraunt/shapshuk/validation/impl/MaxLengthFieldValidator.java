package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.MaxLength;
import com.restraunt.shapshuk.validation.ValidationException;

import java.lang.reflect.Field;

public class MaxLengthFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (String.class.equals(field.getType())) {
            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            try {
                String fieldValue = (String) field.get(entity);
                if (fieldValue != null && fieldValue.trim().length() > maxLength.value()) {

                    String annotationArg = String.format("Max length in field %s is %d", field.getName(), maxLength.value());
                    return new BrokenField(field.getName(), fieldValue, "maxLength", annotationArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }
        return null;
    }
}
