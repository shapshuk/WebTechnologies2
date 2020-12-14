package com.restraunt.shapshuk.validation.impl;


import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.ValidationException;

import java.lang.reflect.Field;
import java.util.Collection;

public class NotEmptyFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (Collection.class.isAssignableFrom(field.getType())) {
            try {
                Collection<?> fieldValue = (Collection<?>) field.get(entity);

                if (fieldValue == null || fieldValue.isEmpty()) {
                    String annotationArg = String.format("Field %s is empty", field.getName());
                    return new BrokenField(field.getName(), fieldValue, "notEmpty", annotationArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        } else if (String.class.isAssignableFrom(field.getType())) {
            try {
                String fieldValue = (String) field.get(entity);

                if (fieldValue == null || fieldValue.isEmpty()) {
                    String annotationArg = String.format("Field %s is empty", field.getName());
                    return new BrokenField(field.getName(), fieldValue, "notEmpty", annotationArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }

        return null;
    }
}
