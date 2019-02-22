package com.ff.creditsuisse.store;

import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.ff.creditsuisse.model.OrderType.BUY;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryOrderDataStoreTest {

    private Map<Order, Integer> orderList = new HashMap<>();
    private TreeMap<Double, Double> map = new TreeMap();
    private InMemoryOrderDataStore inMemoryOrderDataStore = new InMemoryOrderDataStore(map, orderList);

    @Before
    public void setup() {
        orderList.put(new Order("userId", 1D, 1D, BUY), 1);
        orderList.put(new Order("userId2", 2D, 2D, BUY), 1);
        map.put(1D, 1D);
        map.put(2D, 2D);
    }

    @Test
    public void shouldInserAnOrderWithANewPrice() {
        Order order = new Order("userId", 3D, 3D, BUY);

        inMemoryOrderDataStore.insert(order);

        assertThat(orderList).containsKey(order);
        assertThat(map.get(3D)).isEqualTo(3D);
    }

    @Test
    public void shouldInsertAnOrderWithAnExistingPrice() {
        Order order = new Order("userId", 3D, 2D, BUY);

        inMemoryOrderDataStore.insert(order);

        assertThat(orderList).containsKey(order);
        assertThat(map.get(2D)).isEqualTo(5D);
    }

    @Test
    public void shouldCancelAnOrder() throws OrderNotFoundException {
        Order order = new Order("userId", 1D, 1D, BUY);

        inMemoryOrderDataStore.delete(order);

        assertThat(orderList).doesNotContainKey(order);
        assertThat(map.containsKey(1D)).isFalse();
    }

    @Test(expected = OrderNotFoundException.class)
    public void shouldReturnAnExceptionIfTheOrderToCancelDoesntExist() throws OrderNotFoundException {
        Order order = new Order("userId", 2D, 1D, BUY);

        inMemoryOrderDataStore.delete(order);
    }

    @Test
    public void shouldReturnACopyOfTheQuantities() {
        Map<Double, Double> result = inMemoryOrderDataStore.getQuantityByPrice();

        assertThat(result).isEqualTo(map);

    }
}