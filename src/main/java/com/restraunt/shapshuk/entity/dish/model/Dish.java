package com.restraunt.shapshuk.entity.dish.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.NotEmpty;
import com.restraunt.shapshuk.validation.ValidBean;
import com.restraunt.shapshuk.validation.cost.Digits;
import com.restraunt.shapshuk.validation.cost.MinCost;

import java.math.BigDecimal;

@ValidBean("dish")
public class Dish implements IdentifiedRow {

    private Long id;
    @MinLength(4)
    private String name;
    @NotEmpty
    @MinCost(value = "0.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal cost;
    @NotEmpty
    private String description;
    @NotEmpty
    private String picture;
    private DishCategory dishCategory;

    public Dish() {
        this.dishCategory = new DishCategory();
    }

    @Override
    public String toString() {

        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", picture='" + picture.substring(0, 20) + '\'' +
                ", dishCategory=" + dishCategory +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DishCategory getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(DishCategory dishCategory) {
        this.dishCategory = dishCategory;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}



