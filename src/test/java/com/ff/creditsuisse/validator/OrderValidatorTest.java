package com.ff.creditsuisse.validator;


import com.ff.creditsuisse.exception.OrderNotValidException;
import com.ff.creditsuisse.model.Order;
import com.ff.creditsuisse.model.OrderType;
import org.junit.Test;

public class OrderValidatorTest {
    private OrderValidator orderValidator = new OrderValidator();

    @Test(expected = OrderNotValidException.class)
    public void shouldValidateOrderType() throws OrderNotValidException {
        Order order = new Order("userId", 100, 200, null);

        orderValidator.validateOrder(order);
    }

    @Test(expected = OrderNotValidException.class)
    public void shouldValidateUserId() throws OrderNotValidException {
        Order order = new Order(null, 100, 200, OrderType.BUY);

        orderValidator.validateOrder(order);
    }

    @Test(expected = OrderNotValidException.class)
    public void shouldValidatePrice() throws OrderNotValidException {
        Order order = new Order("userid", 20, 0, OrderType.BUY);

        orderValidator.validateOrder(order);
    }

    @Test(expected = OrderNotValidException.class)
    public void shouldValidateQuantity() throws OrderNotValidException {
        Order order = new Order("userid", 0, 200, OrderType.BUY);

        orderValidator.validateOrder(order);
    }

    @Test
    public void shouldPassValidation() throws OrderNotValidException {
        Order order = new Order("userid", 10, 200, OrderType.BUY);

        orderValidator.validateOrder(order);
    }


}