package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.controller.AdminController;
import nl.tudelft.sem.yumyumnow.controller.OrderController;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
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

public class AdminControllerTest {

    private OrderService orderService;
    private AuthenticationService authenticationService;
    private CompletionFactory orderCompletionService;
    private AdminController adminController;

    private final OrderCompletionHandler stubCompletionHandler = new OrderCompletionHandler() {
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
        this.adminController = new AdminController(orderService, authenticationService, orderCompletionService);
    }

    /**
     * Tests the removeOrder method.
     */
    @Test
    public void removeOrder() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * Tests the removeOrder method when the admin is unauthorized.
     */
    @Test
    public void removeOrderNotAuthorized() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    /**
     * Tests the removeOrder method when the order is not found.
     */
    @Test
    public void removeOrderNotFound() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(false);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.NOT_FOUND));
        this.orderCompletionService = Mockito.mock(CompletionFactory.class);
        this.adminController = new AdminController(orderService, authenticationService, orderCompletionService);
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

        List<Order> ordersReceived = adminController.getAllOrders(2L).getBody();
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

        List<Order> orders = adminController.getAllOrders(2L).getBody();
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

        assertEquals(HttpStatus.BAD_REQUEST, adminController.getAllOrders(100L).getStatusCode());
        assertEquals(HttpStatus.OK, adminController.getAllOrders(101L).getStatusCode());
    }

    /**
     * Tests the modifyOrderAdmin method with invalid admin ID.
     */
    @Test
    public void modifyOrderAdminInvalidAdminId() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);

        assertEquals(HttpStatus.UNAUTHORIZED, adminController.updateOrder(100L, 100L, new Order()).getStatusCode());
    }

    /**
     * Tests the modifyOrderAdmin method when the order id is not found.
     */
    @Test
    public void modifyOrderAdminNotFound() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.modifyOrderAdmin(Mockito.anyLong(), Mockito.any(Order.class))).thenReturn(null);

        assertEquals(HttpStatus.NOT_FOUND, adminController.updateOrder(100L, 100L, new Order()).getStatusCode());
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
        Mockito.when(this.orderService.existsAtId(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.modifyOrderAdmin(Mockito.anyLong(), Mockito.any(Order.class))).thenReturn(order);

        ResponseEntity<Void> orderReceived = adminController.updateOrder(2L, 100L, new Order());
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.OK, orderReceived.getStatusCode());
    }

    @Test
    public void modifyOrderAdminInternalServerError() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.modifyOrderAdmin(Mockito.anyLong(), Mockito.any(Order.class))).thenReturn(null);

        ResponseEntity<Void> orderReceived = adminController.updateOrder(2L, 100L, new Order());
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, orderReceived.getStatusCode());
    }
}
