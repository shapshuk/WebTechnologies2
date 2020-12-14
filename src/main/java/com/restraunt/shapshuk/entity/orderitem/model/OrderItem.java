package com.restraunt.shapshuk.entity.orderitem.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.entity.dish.model.Dish;

import java.math.BigDecimal;

public class OrderItem implements IdentifiedRow {

    private Long id;
    private Integer dishAmount;
    private BigDecimal itemCost;
    private Dish dish;
    private Long userOrderId;

    public OrderItem() {
        this.dish = new Dish();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", dishAmount=" + dishAmount +
                ", itemCost=" + itemCost +
                ", dish=" + dish +
                ", userOrder=" + userOrderId +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(Integer dishAmount) {
        this.dishAmount = dishAmount;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }
}
