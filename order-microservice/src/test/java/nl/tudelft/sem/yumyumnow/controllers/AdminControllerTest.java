package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.controller.AdminController;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.CustomerService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
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
    private CustomerService customerService;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.orderService = Mockito.mock(OrderService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.customerService = Mockito.mock(CustomerService.class);
        this.adminController = new AdminController(orderService, authenticationService,
            orderCompletionService, customerService);
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
        this.adminController = new AdminController(orderService, authenticationService,
            orderCompletionService, customerService);
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

    @Test
    public void testGetOrder() {
        Order order = new Order();
        order.setOrderId(10L);

        Mockito.when(authenticationService.isAdmin(11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenReturn(order);

        ResponseEntity<Order> response = adminController.getOrderAdmin(order.getOrderId(), 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    public void testGetOrderUnauthorized() {
        Order order = new Order();
        order.setOrderId(10L);

        Mockito.when(authenticationService.isAdmin(11L)).thenReturn(false);

        ResponseEntity<Order> response = adminController.getOrderAdmin(order.getOrderId(), 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetOrderNotFound() {
        Mockito.when(authenticationService.isAdmin(11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenThrow(new NoSuchElementException());

        ResponseEntity<Order> response = adminController.getOrderAdmin(10L, 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderError() {
        Order order = new Order();
        order.setOrderId(10L);

        Mockito.when(authenticationService.isAdmin(11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenThrow(new RuntimeException());

        ResponseEntity<Order> response = adminController.getOrderAdmin(order.getOrderId(), 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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

    @Test
    public void getListOfOrdersForVendorForClientTestUnauthorized() {
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(false);
        Mockito.when(this.authenticationService.isVendor(2L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(1L)).thenReturn(true);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.UNAUTHORIZED, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClientTestUnauthorized2() {
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(2L)).thenReturn(false);
        Mockito.when(this.authenticationService.isCustomer(1L)).thenReturn(true);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.UNAUTHORIZED, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClientTestUnauthorized3() {
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(2L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(1L)).thenReturn(false);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(2L, 1L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.UNAUTHORIZED, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClientTestError() {
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(1L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(2L)).thenReturn(true);
        Mockito.when(this.customerService.getCustomer(2L)).thenThrow(RuntimeException.class);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClientTesNotFound() {
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(1L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(2L)).thenReturn(true);
        Mockito.when(this.customerService.getCustomer(2L)).thenReturn(null);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.NOT_FOUND, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClienNullOrders() {
        Customer customer = new Customer();
        customer.setPastOrders(null);
        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(1L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(2L)).thenReturn(true);
        Mockito.when(this.customerService.getCustomer(2L)).thenReturn(customer);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.NOT_FOUND, orderReceived.getStatusCode());
    }

    @Test
    public void getListOfOrdersForVendorForClienZeroOrders() {
        Order order1 = new Order();
        order1.setVendorId(12L);
        Order order2 = new Order();
        order2.setVendorId(13L);
        Order order3 = new Order();
        order3.setVendorId(14L);
        List<Order> orders = List.of(order1, order2, order3);
        Customer customer = new Customer();
        customer.setPastOrders(orders);

        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(1L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(2L)).thenReturn(true);
        Mockito.when(this.customerService.getCustomer(2L)).thenReturn(customer);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.OK, orderReceived.getStatusCode());
        assertEquals(new ArrayList<>(), orderReceived.getBody());
    }

    @Test
    public void getListOfOrdersForVendorForClientTest() {
        Order order1 = new Order();
        order1.setVendorId(1L);
        Order order2 = new Order();
        order2.setVendorId(1L);
        Order order3 = new Order();
        order3.vendorId(14L);
        List<Order> orders = List.of(order1, order2, order3);
        Customer customer = new Customer();
        customer.setPastOrders(orders);

        Mockito.when(this.authenticationService.isAdmin(3L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(1L)).thenReturn(true);
        Mockito.when(this.authenticationService.isCustomer(2L)).thenReturn(true);
        Mockito.when(this.customerService.getCustomer(2L)).thenReturn(customer);

        ResponseEntity<List<Order>> orderReceived = adminController.getListOfOrdersForVendorForClient(1L, 2L, 3L);
        List<Order> answer = List.of(order1, order2);
        assertNotNull(orderReceived);
        assertEquals(HttpStatus.OK, orderReceived.getStatusCode());
        assertEquals(answer, orderReceived.getBody());
    }
}
