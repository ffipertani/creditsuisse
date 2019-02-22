package com.ff.creditsuisse.exception;

import com.ff.creditsuisse.model.Order;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(Order order){
        super("Order["+order+"] not found");
    }
}
