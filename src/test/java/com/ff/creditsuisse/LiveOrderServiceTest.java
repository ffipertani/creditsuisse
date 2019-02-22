package com.ff.creditsuisse;


import com.ff.creditsuisse.exception.OrderNotFoundException;
import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import com.ff.creditsuisse.model.OrderSummary;
import com.ff.creditsuisse.store.OrderDataStore;
import com.ff.creditsuisse.validator.OrderValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ff.creditsuisse.model.OrderType.BUY;
import static com.ff.creditsuisse.model.OrderType.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LiveOrderServiceTest {
    private static Map<Double, Double> buyData = createBuyData();
    private static Map<Double, Double> sellData = createSellData();
    @Mock
    private OrderDataStore sellDataStore;
    @Mock
    private OrderDataStore buyDataStore;
    @Mock
    private OrderValidator orderValidator;


    @Before
    public void setup() {
        given(buyDataStore.getQuantityByPrice()).willReturn(buyData);
        given(sellDataStore.getQuantityByPrice()).willReturn(sellData);
    }

    @Test
    public void shouldValidateAnOrder() throws OrderNotValidException {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);
        Order order = new Order("user1", 10, 20, BUY);

        liveOrderService.addOrder(order);

        verify(orderValidator, times(1)).validateOrder(order);
    }


    @Test
    public void shouldAddABuyOrder() throws OrderNotValidException {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);
        Order order = new Order("user1", 10, 20, BUY);

        liveOrderService.addOrder(order);

        verify(sellDataStore, times(0)).insert(order);
        verify(buyDataStore, times(1)).insert(order);
    }


    @Test
    public void shouldAddASellOrder() throws OrderNotValidException {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);
        Order order = new Order("user1", 10, 20, SELL);

        liveOrderService.addOrder(order);

        verify(sellDataStore, times(1)).insert(order);
        verify(buyDataStore, times(0)).insert(order);
    }

    @Test
    public void shouldCancelABuyOrder() throws OrderNotFoundException {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);
        Order order = new Order("user1", 10, 20, BUY);

        liveOrderService.cancelOrder(order);

        verify(sellDataStore, times(0)).delete(order);
        verify(buyDataStore, times(1)).delete(order);
    }

    @Test
    public void shouldCancelASellOrder() throws OrderNotFoundException {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);
        Order order = new Order("user1", 10, 20, SELL);

        liveOrderService.cancelOrder(order);

        verify(sellDataStore, times(1)).delete(order);
        verify(buyDataStore, times(0)).delete(order);
    }

    @Test
    public void shouldReturnBuyLiveOrders() {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);

        List<OrderSummary> result = liveOrderService.getLiveOrders(BUY);

        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(2D, 12D, BUY));
        expected.add(new OrderSummary(3D, 13D, BUY));
        assertThat(result).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnSellLiveOrders() {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);

        List<OrderSummary> result = liveOrderService.getLiveOrders(SELL);

        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(21D, 121D, SELL));
        expected.add(new OrderSummary(31D, 131D, SELL));
        assertThat(result).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnCombinedLiveOrders() {
        LiveOrderService liveOrderService = new LiveOrderService(sellDataStore, buyDataStore, orderValidator);

        List<OrderSummary> result = liveOrderService.getLiveOrders();

        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(2D, 12D, BUY));
        expected.add(new OrderSummary(3D, 13D, BUY));
        expected.add(new OrderSummary(21D, 121D, SELL));
        expected.add(new OrderSummary(31D, 131D, SELL));
        assertThat(result).containsExactlyElementsOf(expected);
    }


    private static Map createBuyData() {
        Map map = new HashMap();
        map.put(2D, 12D);
        map.put(3D, 13D);
        return map;
    }


    private static Map createSellData() {
        Map map = new HashMap();
        map.put(21D, 121D);
        map.put(31D, 131D);
        return map;
    }
}