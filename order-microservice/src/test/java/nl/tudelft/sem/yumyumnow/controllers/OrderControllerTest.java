package nl.tudelft.sem.yumyumnow.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.yumyumnow.controller.OrderController;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import nl.tudelft.sem.yumyumnow.services.completion.OrderCompletionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {

    private OrderService orderService;
    private AuthenticationService authenticationService;
    private CompletionFactory orderCompletionService;
    private OrderController orderController;

    private OrderCompletionHandler stubCompletionHandler = new OrderCompletionHandler() {
        @Override
        public Order.StatusEnum handleOrderCompletion(Order order) {
            return Order.StatusEnum.PREPARING;
        }
    };

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.orderService = Mockito.mock(OrderService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.orderCompletionService = Mockito.mock(CompletionFactory.class);
        this.orderController = new OrderController(orderService, authenticationService, orderCompletionService);
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

    /**
     * Tests the getAllOrdersCustomer method with invalid customer ID.
     */
    @Test
    public void getAllOrdersCustomerInvalidId() {
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(false);
        Mockito.when(this.authenticationService.isCustomer(101L)).thenReturn(true);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.getAllOrdersCustomer(100L).getStatusCode());
        assertEquals(HttpStatus.OK, orderController.getAllOrdersCustomer(101L).getStatusCode());
    }

    /**
     * Tests if the method getAllOrdersCustomer returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrdersCustomer() {
        Order order1 = new Order();
        order1.setCustomerId(2L);
        order1.setVendorId(3L);
        Order order2 = new Order();
        order2.setCustomerId(2L);
        order2.setVendorId(7L);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(this.authenticationService.isCustomer(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForCustomer(Mockito.anyLong())).thenReturn(orders);

        List<Order> ordersReceived = orderController.getAllOrdersCustomer(2L).getBody();
        assertNotNull(ordersReceived);
        assertEquals(2L, ordersReceived.get(0).getCustomerId());
        assertEquals(3L, ordersReceived.get(0).getVendorId());
        assertEquals(2L, ordersReceived.get(1).getCustomerId());
        assertEquals(7L, ordersReceived.get(1).getVendorId());
        assertEquals(2, ordersReceived.size());
    }

    /**
     * Tests if the method getAllOrdersCustomer returns a correct List of Orders if there
     * are no orders.
     */
    @Test
    public void testGetAllOrdersCustomerNoOrders() {
        Mockito.when(this.authenticationService.isCustomer(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForCustomer(Mockito.anyLong())).thenReturn(new ArrayList<Order>());

        List<Order> orders = orderController.getAllOrdersCustomer(2L).getBody();
        assertNotNull(orders);
        assertEquals(orders, new ArrayList<Order>());
    }

    /**
     * Tests the getAllOrdersVendor method with invalid Vendor ID.
     */
    @Test
    public void getAllOrdersVendorInvalidId() {
        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(false);
        Mockito.when(this.authenticationService.isVendor(101L)).thenReturn(true);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.getAllOrdersVendor(100L).getStatusCode());
        assertEquals(HttpStatus.OK, orderController.getAllOrdersVendor(101L).getStatusCode());
    }

    /**
     * Tests if the method getAllOrdersVendor returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrdersVendor() {
        Order order1 = new Order();
        order1.setCustomerId(1L);
        order1.setVendorId(3L);
        Order order2 = new Order();
        order2.setCustomerId(2L);
        order2.setVendorId(3L);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(this.authenticationService.isVendor(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForVendor(Mockito.anyLong())).thenReturn(orders);

        List<Order> ordersReceived = orderController.getAllOrdersVendor(3L).getBody();
        assertNotNull(ordersReceived);
        assertEquals(1L, ordersReceived.get(0).getCustomerId());
        assertEquals(3L, ordersReceived.get(0).getVendorId());
        assertEquals(2L, ordersReceived.get(1).getCustomerId());
        assertEquals(3L, ordersReceived.get(1).getVendorId());
        assertEquals(2, ordersReceived.size());
    }

    /**
     * Tests if the method getAllOrdersVendor returns a correct List of Orders if there
     * are no orders.
     */
    @Test
    public void testGetAllOrdersVendorNoOrders() {
        Mockito.when(this.authenticationService.isVendor(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForVendor(Mockito.anyLong())).thenReturn(new ArrayList<Order>());

        List<Order> orders = orderController.getAllOrdersVendor(2L).getBody();
        assertNotNull(orders);
        assertEquals(orders, new ArrayList<Order>());
    }

    /**
     * Tests the modifyOrderAdmin method with invalid admin ID.
     */
    @Test
    public void modifyOrderAdminInvalidAdminId() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.modifyOrderAdmin(100L, 100L, new Order()).getStatusCode());
    }

    /**
     * Tests the modifyOrderAdmin method when the order id is not found.
     */
    @Test
    public void modifyOrderAdminNotFound() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.modifyOrderAdmin(Mockito.anyLong(), Mockito.any(Order.class))).thenReturn(null);

        assertEquals(HttpStatus.NOT_FOUND, orderController.modifyOrderAdmin(100L, 100L, new Order()).getStatusCode());
    }

    /**
     * Tests the modifyOrderAdmin method.
     */
    @Test
    public void modifyOrderAdmin() {
        Order order = new Order();
        order.setOrderId(2L);
        order.setCustomerId(3L);
        order.setVendorId(4L);
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.modifyOrderAdmin(Mockito.anyLong(), Mockito.any(Order.class))).thenReturn(order);

        Order orderReceived = orderController.modifyOrderAdmin(2L, 100L, new Order()).getBody();
        assertNotNull(orderReceived);
        assertEquals(2L, orderReceived.getOrderId());
        assertEquals(3L, orderReceived.getCustomerId());
        assertEquals(4L, orderReceived.getVendorId());
    }

    @Test
    public void testCompleteOrderInvalidRequests() {
        Mockito.when(this.authenticationService.isCustomer(332L)).thenReturn(false);
        Mockito.when(this.orderService.existsAtId(6767L)).thenReturn(false);
        Mockito.when(this.authenticationService.isCustomer(8L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(445L)).thenReturn(true);

        assertEquals(HttpStatus.UNAUTHORIZED, this.orderController.completeOrder(445L, 332L).getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, this.orderController.completeOrder(6767L, 332L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, this.orderController.completeOrder(6767L, 8L).getStatusCode());
    }

    @Test
    public void testNotSavedCorrectly() {
        Mockito.when(this.authenticationService.isCustomer(332L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(445L)).thenReturn(true);
        Mockito.when(this.orderCompletionService.createCompletionResponsibilityChain(Mockito.any()))
                .thenReturn(stubCompletionHandler);
        Mockito.when(this.orderService.setOrderStatus(445L, Order.StatusEnum.PREPARING)).thenReturn(false);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.orderController.completeOrder(445L, 332L).getStatusCode());
    }

    @Test
    public void testSavedCorrectly() {
        Mockito.when(this.authenticationService.isCustomer(332L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(445L)).thenReturn(true);
        Mockito.when(this.orderCompletionService.createCompletionResponsibilityChain(Mockito.any()))
                .thenReturn(stubCompletionHandler);
        Mockito.when(this.orderService.setOrderStatus(445L, Order.StatusEnum.PREPARING)).thenReturn(true);
        ResponseEntity<String> result = this.orderController.completeOrder(445L, 332L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("preparing", result.getBody());
    }

    /**
     * Tests the addDishesToOrder method with invalid customer ID.
     */
    @Test
    public void addDishesToOrderInvalidCustomerId() {
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(false);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.addDishesToOrder(100L, 100L,
                new ArrayList<Dish>()).getStatusCode());
    }

    /**
     * Tests the addDishesToOrder method when the list of dishes to add is empty.
     */
    @Test
    public void addDishesToOrderNoDishes() {
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);

        assertEquals(HttpStatus.BAD_REQUEST, orderController.addDishesToOrder(100L,
                100L, new ArrayList<Dish>()).getStatusCode());
    }

    /**
     * Tests the addDishesToOrder method when the order id is not found.
     */
    @Test
    public void addDishesToOrderNotFound() {
        Dish d1 = new Dish();
        Dish d2 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(d1);
        dishes.add(d2);
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.addDishesToOrder(Mockito.anyLong(), Mockito.any(List.class))).thenReturn(null);

        assertEquals(HttpStatus.NOT_FOUND, orderController.addDishesToOrder(100L, 100L, dishes).getStatusCode());
    }

    /**
     * Tests the addDishesToOrder method.
     */
    @Test
    public void addDishesToOrder() {
        Dish d1 = new Dish();
        Dish d2 = new Dish();
        final Dish d3 = new Dish();
        final Dish d4 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(d1);
        dishes.add(d2);
        List<Dish> allDishes = new ArrayList<>();
        allDishes.add(d1);
        allDishes.add(d2);
        allDishes.add(d3);
        allDishes.add(d4);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.addDishesToOrder(Mockito.anyLong(), Mockito.any(List.class))).thenReturn(allDishes);

        List<Dish> allDishesAfterAdding = orderController.addDishesToOrder(2L, 100L, dishes).getBody();
        assert allDishesAfterAdding != null;
        assertTrue(allDishesAfterAdding.contains(d1));
        assertTrue(allDishesAfterAdding.contains(d2));
        assertTrue(allDishesAfterAdding.contains(d3));
        assertTrue(allDishesAfterAdding.contains(d4));
        assertEquals(allDishesAfterAdding, allDishes);
    }
}
