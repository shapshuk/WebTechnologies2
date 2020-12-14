package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.ValidationException;

import java.lang.reflect.Field;

public class MinLengthFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (String.class.equals(field.getType())) {
            MinLength minLength = field.getAnnotation(MinLength.class);
            try {
                String fieldValue = (String) field.get(entity);
                if (fieldValue != null && fieldValue.trim().length() < minLength.value()) {

                    String annotationArg = String.format("Min length in field %s is %d", field.getName(), minLength.value());
                    return new BrokenField(field.getName(), fieldValue, "minLength", annotationArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }
        return null;
    }
}
