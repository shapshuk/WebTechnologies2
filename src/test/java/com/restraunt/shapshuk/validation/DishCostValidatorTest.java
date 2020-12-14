package com.restraunt.shapshuk.validation;

import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class DishCostValidatorTest {

    private static final Logger LOGGER = Logger.getLogger(DishCostValidatorTest.class.getName());
    private static BeanValidator beanValidator;

    @BeforeAll
    static void initValidator() {

        ApplicationContext.initialize();
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        beanValidator = applicationContext.getBean(BeanValidator.class);
    }

    @Test
    void shouldFailValidationOnNegativeCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("-1.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertNotNull(result);
        List<BrokenField> brokenFields = result.getBrokenFields();
        Assert.assertEquals(1, brokenFields.size());

        BrokenField brokenField = brokenFields.get(0);
        LOGGER.info("Broken field is " + brokenField.getFieldName());
    }

    @Test
    void shouldNotFailValidationOnZeroCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("0.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldNotFailValidationOnPositiveCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldFailValidationOnMoreThan3IntegerDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1000.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);

        Assert.assertNotNull(result);
        List<BrokenField> brokenFields = result.getBrokenFields();
        Assert.assertEquals(1, brokenFields.size());

        BrokenField brokenField = brokenFields.get(0);
        LOGGER.info("Broken field is " + brokenField.getFieldName());
    }

    @Test
    void shouldFailValidationOnMoreThan2FractionDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("0.001"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);

        Assert.assertNotNull(result);
        List<BrokenField> brokenFields = result.getBrokenFields();
        Assert.assertEquals(1, brokenFields.size());

        BrokenField brokenField = brokenFields.get(0);
        LOGGER.info("Broken field is " + brokenField.getFieldName());
    }

    @Test
    void shouldNotFailValidationOnLessThan3IntegerDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("10.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldNotFailValidationOnLessThan2FractionDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1.1"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldNotFailValidationOn3IntegerDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("100.0"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldNotFailValidationOn2FractionDigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1.01"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldFailValidationOnNameField() {

        Dish dish = new Dish();
        dish.setName("sou");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1.01"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);

        Assert.assertNotNull(result);
        List<BrokenField> brokenFields = result.getBrokenFields();
        Assert.assertEquals(1, brokenFields.size());

        BrokenField brokenField = brokenFields.get(0);
        LOGGER.info("Broken field is " + brokenField.getFieldName());
    }

    @Test
    void shouldNotFailValidationOnNameField() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1.01"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);
        Assert.assertEquals(0, result.getBrokenFields().size());
    }

    @Test
    void shouldFailValidationOnMoreThanOnlyInteger3DigitsCost() {

        Dish dish = new Dish();
        dish.setName("soup");
        dish.setPicture("0x2F396A2F34414");
        dish.setCost(new BigDecimal("1000"));
        dish.setDescription("yummy");

        ValidationResult result = beanValidator.validate(dish);

        Assert.assertNotNull(result);
        List<BrokenField> brokenFields = result.getBrokenFields();
        Assert.assertEquals(1, brokenFields.size());

        BrokenField brokenField = brokenFields.get(0);
        LOGGER.info("Broken field is " + brokenField.getFieldName());
    }
}
