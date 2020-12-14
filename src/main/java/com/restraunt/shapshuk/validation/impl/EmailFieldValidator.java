package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.Email;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.ValidationException;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class EmailFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (String.class.isAssignableFrom(field.getType())) {
            Email email = field.getAnnotation(Email.class);
            String regex = email.regex();
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            try {
                String fieldValue = (String) field.get(entity);
                if (fieldValue != null
                        && !fieldValue.isEmpty()
                        && !pattern.matcher(fieldValue).find()) {
                    String annotationRegexArg = "Email must have: @-character and places before @-character";
                    return new BrokenField(field.getName(), fieldValue, "email", annotationRegexArg);
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }

        return null;
    }
}
