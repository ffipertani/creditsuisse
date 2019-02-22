package com.ff.creditsuisse.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderSummary {
    private double price;
    private double quantity;
    private OrderType orderType;

    public OrderSummary(double price, double quantity, OrderType orderType) {
        this.price = price;
        this.quantity = quantity;
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
