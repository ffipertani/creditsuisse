package com.ff.creditsuisse.store;

import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryOrderDataStore implements OrderDataStore {
    private static final Logger log = LoggerFactory.getLogger(InMemoryOrderDataStore.class.toString());
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Order, Integer> orders;
    private final SortedMap<Double, Double> quantities;

    public InMemoryOrderDataStore(SortedMap<Double, Double> quantities, Map<Order, Integer> orders) {
        this.quantities = quantities;
        this.orders = orders;
    }

    @Override
    public void insert(Order order) {
        log.trace("Inserting order {}. Waiting for lock", order);
        lock.writeLock().lock();
        try {
            log.info("Inserting order {}", order);
            orders.put(order, orders.getOrDefault(order, 0) + 1);
            quantities.put(order.getPrice(), quantities.getOrDefault(order.getPrice(), 0D) + order.getQuantity());
            if (log.isDebugEnabled()) {
                log.debug("Order {} inserted. Price={} Quantity={}", order, order.getPrice(), quantities.get(order.getPrice()));
            }
        } finally {
            lock.writeLock().unlock();
            log.trace("Insert order {} completed.", order);
        }
    }

    @Override
    public void delete(Order order) throws OrderNotFoundException {
        log.trace("Deleting order {}. Waiting for lock", order);
        lock.writeLock().lock();
        try {
            if (!orders.containsKey(order)) {
                log.warn("Order {} does not exist", order);
                throw new OrderNotFoundException(order);
            }
            log.info("Deleting order {}", order);
            quantities.put(order.getPrice(), quantities.getOrDefault(order.getPrice(), 0D) - order.getQuantity());
            if (quantities.get(order.getPrice()) <= 0) {
                quantities.remove(order.getPrice());
            }

            orders.put(order, orders.getOrDefault(order, 0) - 1);
            if (orders.get(order) <= 0) {
                orders.remove(order);
            }
            if (log.isDebugEnabled()) {
                log.debug("Order {} deleted. Price={} Quantity={}", order, order.getPrice(), quantities.get(order.getPrice()));
            }
        } finally {
            lock.writeLock().unlock();
            log.trace("Delete order {} completed.", order);
        }
    }

    @Override
    public Map<Double, Double> getQuantityByPrice() {
        log.debug("Requested quantities per price");
        lock.readLock().lock();
        Map<Double, Double> result = new TreeMap(quantities);
        lock.readLock().unlock();
        log.debug("Current quantities per price {}", result);
        return result;
    }
}
