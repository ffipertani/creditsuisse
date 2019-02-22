package com.ff.creditsuisse;

import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import com.ff.creditsuisse.model.OrderSummary;
import com.ff.creditsuisse.store.InMemoryOrderDataStore;
import com.ff.creditsuisse.store.OrderDataStore;
import com.ff.creditsuisse.validator.OrderValidator;

import java.util.*;

public class LiveOrderClient {
    private final LiveOrderService liveOrderService;

    public LiveOrderClient() {
        OrderDataStore buyDataStore = new InMemoryOrderDataStore(new TreeMap(Comparator.reverseOrder()), new HashMap<>());
        OrderDataStore sellDataStore = new InMemoryOrderDataStore(new TreeMap(), new HashMap<>());
        this.liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, new OrderValidator());
    }

    public void addOrder(Order order) throws OrderNotValidException {
        liveOrderService.addOrder(order);
    }

    public void cancelOrder(Order order) throws OrderNotFoundException {
        liveOrderService.cancelOrder(order);
    }

    public List<OrderSummary> liveOrders() {
        return liveOrderService.getLiveOrders();
    }
}
