package com.ff.creditsuisse;


import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import com.ff.creditsuisse.model.OrderSummary;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ff.creditsuisse.model.OrderType.BUY;
import static com.ff.creditsuisse.model.OrderType.SELL;
import static org.assertj.core.api.Assertions.assertThat;

public class LiveOrderClientTest {

    private LiveOrderClient liveOrderClient = new LiveOrderClient();

    @Before
    public void setup() throws OrderNotValidException {

        liveOrderClient.addOrder(new Order("user1", 100, 100, BUY));
        liveOrderClient.addOrder(new Order("user2", 100, 200, BUY));
        liveOrderClient.addOrder(new Order("user3", 200, 100, BUY));
        liveOrderClient.addOrder(new Order("user1", 200, 400, BUY));

        liveOrderClient.addOrder(new Order("user1", 100, 100, SELL));
        liveOrderClient.addOrder(new Order("user2", 100, 200, SELL));
        liveOrderClient.addOrder(new Order("user3", 200, 100, SELL));
        liveOrderClient.addOrder(new Order("user1", 200, 400, SELL));
    }

    @Test
    public void shouldGetPrices() {
        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(400d,200,BUY));
        expected.add(new OrderSummary(200,100,BUY));
        expected.add(new OrderSummary(100,300,BUY));
        expected.add(new OrderSummary(100,300,SELL));
        expected.add(new OrderSummary(200,100,SELL));
        expected.add(new OrderSummary(400,200,SELL));

        List<OrderSummary> result = liveOrderClient.liveOrders();

        assertThat(result).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldCancelAnOrder() throws OrderNotFoundException {
        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(400d,200,BUY));
        expected.add(new OrderSummary(200,100,BUY));
        expected.add(new OrderSummary(100,100,BUY));
        expected.add(new OrderSummary(100,300,SELL));
        expected.add(new OrderSummary(200,100,SELL));

        liveOrderClient.cancelOrder(new Order("user3", 200, 100, BUY));
        liveOrderClient.cancelOrder( new Order("user1", 200, 400, SELL));

        List<OrderSummary> result = liveOrderClient.liveOrders();

        assertThat(result).containsExactlyElementsOf(expected);
    }
}