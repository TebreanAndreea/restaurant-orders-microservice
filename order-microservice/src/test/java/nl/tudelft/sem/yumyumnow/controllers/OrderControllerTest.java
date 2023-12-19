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

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Tests if the method getAllOrders returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrders() {
        Order order1 = new Order();
        order1.setCustomerId(2L);
        order1.setVendorId(3L);
        Order order2 = new Order();
        order2.setCustomerId(6L);
        order2.setVendorId(7L);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(this.authenticationService.isAdmin(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrders()).thenReturn(orders);

        List<Order> ordersReceived = orderController.getAllOrdersAdmin(2L).getBody();
        assertNotNull(ordersReceived);
        assertEquals(2L, ordersReceived.get(0).getCustomerId());
        assertEquals(3L, ordersReceived.get(0).getVendorId());
        assertEquals(6L, ordersReceived.get(1).getCustomerId());
        assertEquals(7L, ordersReceived.get(1).getVendorId());
        assertEquals(2, ordersReceived.size());
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders if there are no orders in the database.
     */
    @Test
    public void testGetAllOrdersNoOrders() {
        Mockito.when(this.authenticationService.isAdmin(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrders()).thenReturn(new ArrayList<Order>());

        List<Order> orders = orderController.getAllOrdersAdmin(2L).getBody();
        assertNotNull(orders);
        assertEquals(orders, new ArrayList<Order>());
    }

    /**
     * Tests the getAllOrder method with invalid admin ID.
     */
    @Test
    public void testInvalidAdminId() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);
        Mockito.when(this.authenticationService.isAdmin(101L)).thenReturn(true);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.getAllOrdersAdmin(100L).getStatusCode());
        assertEquals(HttpStatus.OK, orderController.getAllOrdersAdmin(101L).getStatusCode());
    }
}
