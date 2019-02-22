package com.ff.creditsuisse.store;

import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.model.Order;

import java.util.Map;

public interface OrderDataStore {

    void insert(Order order);

    void delete(Order order) throws OrderNotFoundException;

    Map<Double, Double> getQuantityByPrice();

}
