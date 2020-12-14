package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.PhoneNumber;
import com.restraunt.shapshuk.validation.ValidationException;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class PhoneNumberFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (String.class.isAssignableFrom(field.getType())) {
            PhoneNumber phoneNumber = field.getAnnotation(PhoneNumber.class);
            String regex = phoneNumber.regex();
            Pattern pattern = Pattern.compile(regex);
            try {
                String fieldValue = (String) field.get(entity);
                if (fieldValue != null
                        && !fieldValue.isEmpty()
                        && !pattern.matcher(fieldValue).find()) {
                    String annotationRegexArg = "Phone number must have: character '+' and 12 characters after";
                    return new BrokenField(field.getName(), fieldValue, "phoneNumber", annotationRegexArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }

        return null;
    }
}
