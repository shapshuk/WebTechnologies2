package com.restraunt.shapshuk.entity.wallet.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;

import java.math.BigDecimal;

public class Wallet implements IdentifiedRow {

    private Long id;
    private BigDecimal moneyAmount;

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", moneyAmount=" + moneyAmount +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }
}
