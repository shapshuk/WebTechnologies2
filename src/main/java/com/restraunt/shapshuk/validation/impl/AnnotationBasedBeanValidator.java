package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.ValidBean;
import com.restraunt.shapshuk.validation.ValidationException;
import com.restraunt.shapshuk.validation.ValidationResult;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class AnnotationBasedBeanValidator implements BeanValidator {

    private static final Logger LOGGER = Logger.getLogger(AnnotationBasedBeanValidator.class.getName());

    private final Map<Class<? extends Annotation>, FieldValidator> validationFunctions;
    private final Set<Class<? extends Annotation>> supportedFieldAnnotations;

    public AnnotationBasedBeanValidator(Map<Class<? extends Annotation>, FieldValidator> validationFunctions) {
        this.validationFunctions = validationFunctions;
        supportedFieldAnnotations = this.validationFunctions.keySet();
    }

    @Override
    public ValidationResult validate(Object bean) {

        ValidationResult validationResult = new ValidationResult();

        if (isNull(bean)) {
            String message = "Passed bean is null";
            LOGGER.error(message);
            throw new ValidationException(message);
        }

        Class<?> clazz = bean.getClass();
        if (!clazz.isAnnotationPresent(ValidBean.class)) {

            String message = MessageFormat.format("{0} does not have the {1} annotation.",
                    clazz, ValidBean.class.getName());
            LOGGER.error(message);
            throw new ValidationException(message);
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            List<BrokenField> brokenFields = supportedFieldAnnotations.stream()
                    .filter(field::isAnnotationPresent)
                    .map(validationFunctions::get)
                    .map(fieldValidator -> fieldValidator.validate(bean, field))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            validationResult.addBrokenFields(brokenFields);
        }

        return validationResult;
    }
}
