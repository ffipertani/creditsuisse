package com.ff.creditsuisse;

import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import com.ff.creditsuisse.model.OrderSummary;
import com.ff.creditsuisse.model.OrderType;
import com.ff.creditsuisse.store.OrderDataStore;
import com.ff.creditsuisse.validator.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiveOrderService {
    private static final Logger log = LoggerFactory.getLogger(LiveOrderService.class.toString());
    private final OrderDataStore sellDataStore;
    private final OrderDataStore buyDataStore;
    private final OrderValidator orderValidator;

    public LiveOrderService(OrderDataStore sellDataStore,
                            OrderDataStore buyDataStore,
                            OrderValidator orderValidator) {
        this.sellDataStore = sellDataStore;
        this.buyDataStore = buyDataStore;
        this.orderValidator = orderValidator;
    }

    public void addOrder(Order order) throws OrderNotValidException {
        log.info("Adding an order {}", order);
        orderValidator.validateOrder(order);
        if (order.getOrderType() == OrderType.BUY) {
            buyDataStore.insert(order);
        } else {
            sellDataStore.insert(order);
        }
    }

    public void cancelOrder(Order order) throws OrderNotFoundException {
        log.info("Cancelling an order {}", order);
        if (order.getOrderType() == OrderType.BUY) {
            buyDataStore.delete(order);
        } else {
            sellDataStore.delete(order);
        }
    }

    public List<OrderSummary> getLiveOrders() {
        log.info("Calculating live orders");
        List<OrderSummary> result = getLiveOrders(OrderType.BUY);
        result.addAll(getLiveOrders(OrderType.SELL));
        return result;
    }

    public List<OrderSummary> getLiveOrders(OrderType orderType) {
        List<OrderSummary> result = new ArrayList<>();
        OrderDataStore dataStore;
        if (orderType == OrderType.BUY) {
            dataStore = buyDataStore;
        } else {
            dataStore = sellDataStore;
        }

        Map<Double, Double> prices = dataStore.getQuantityByPrice();
        for (Double price : prices.keySet()) {
            double quantity = prices.get(price);
            if (quantity > 0) {
                result.add(new OrderSummary(price, quantity, orderType));
            }
        }
        return result;
    }
}
