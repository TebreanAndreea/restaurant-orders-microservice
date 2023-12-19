package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.yumyumnow.controller.OrderController;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

public class OrderControllerTest {

    private OrderService orderService;
    private AuthenticationService authenticationService;
    private OrderController orderController;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.orderService = Mockito.mock(OrderService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.orderController = new OrderController(orderService, authenticationService);
    }

    /**
     * Tests the createOrder method with invalid customer and vendor IDs.
     */
    @Test
    public void testInvalidIds() {
        Order order = new Order();
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(false);
        Mockito.when(this.authenticationService.isCustomer(101L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(250L)).thenReturn(false);
        Mockito.when(this.authenticationService.isVendor(260L)).thenReturn(true);
        Mockito.when(this.orderService.createNewOrder(Mockito.anyLong(), Mockito.anyLong())).thenReturn(order);

        assertEquals(HttpStatus.NOT_FOUND, orderController.createOrder(100L, 250L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, orderController.createOrder(101L, 250L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, orderController.createOrder(100L, 260L).getStatusCode());
        assertEquals(HttpStatus.OK, orderController.createOrder(101L, 260L).getStatusCode());
    }

    /**
     * Tests if the method createOrder returns a correct Order object.
     */
    @Test
    public void testOrderIsOk() {
        Order order = new Order();
        order.setCustomerId(2L);
        order.setVendorId(3L);
        Mockito.when(this.authenticationService.isCustomer(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.createNewOrder(Mockito.anyLong(), Mockito.anyLong())).thenReturn(order);

        Order orderSent = orderController.createOrder(2L, 3L).getBody();
        assertNotNull(orderSent);
        assertEquals(2L, order.getCustomerId());
        assertEquals(3L, order.getVendorId());
    }
}
