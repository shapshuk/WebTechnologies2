package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.ValidationException;
import com.restraunt.shapshuk.validation.cost.Digits;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class DigitsFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (BigDecimal.class.equals(field.getType())) {

            Digits digits = field.getAnnotation(Digits.class);
            try {
                BigDecimal fieldValue = (BigDecimal) field.get(entity);

                if (fieldValue != null) {
                    BrokenField annotationArg = findBrokenFields(field, digits, fieldValue);
                    if (annotationArg != null) {
                        return annotationArg;
                    }
                }

            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }
        return null;
    }

    private BrokenField findBrokenFields(Field field, Digits digits, BigDecimal fieldValue) {

        String fieldValueString = fieldValue.toString();

        if (fieldValueString.split("\\.").length == 2) {

            if (validateIntegerFractionDecimal(digits, fieldValueString)) {

                String annotationArg = String.format("Max digits in integer part of cost is %d, in fraction part is %d", digits.integer(), digits.fraction());
                return new BrokenField(field.getName(), fieldValue, "digits", annotationArg);
            }
        } else {

            if (validateIntegerDecimal(digits, fieldValueString)) {

                String annotationArg = String.format("Max digits in integer part of cost is %d", digits.integer());
                return new BrokenField(field.getName(), fieldValue, "digits", annotationArg);
            }
        }
        return null;
    }

    private boolean validateIntegerDecimal(Digits digits, String fieldValueString) {

        String integer = fieldValueString.split("\\.")[0];

        return digits.integer() < integer.length();
    }

    private boolean validateIntegerFractionDecimal(Digits digits, String fieldValueString) {

        String integer = fieldValueString.split("\\.")[0];
        String fraction = fieldValueString.split("\\.")[1];

        return digits.integer() < integer.length()
                || digits.fraction() < fraction.length();
    }
}
