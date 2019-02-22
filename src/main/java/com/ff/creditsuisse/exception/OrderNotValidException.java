package com.ff.creditsuisse.exception;

import com.ff.creditsuisse.model.Order;

public class OrderNotValidException extends Exception {

    public OrderNotValidException(Order order, String message) {
        super("Order[" + order + "] is not valid:" + message);
    }
}
