package com.ff.creditsuisse.validator;

import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import org.apache.commons.lang3.StringUtils;

public class OrderValidator {

    public void validateOrder(Order order) throws OrderNotValidException {
        if(order==null){
            throw new OrderNotValidException(order, "order can't be null");
        }
        if (order.getOrderType() == null) {
            throw new OrderNotValidException(order, "orderType can't be null");
        }
        if (StringUtils.isEmpty(order.getUserId())) {
            throw new OrderNotValidException(order, "userId can't be null");
        }
        if (order.getPrice() <= 0) {
            throw new OrderNotValidException(order, "price must be greater than 0");
        }
        if (order.getQuantity() <= 0) {
            throw new OrderNotValidException(order, "quantity must be greater than 0");
        }
    }

}
