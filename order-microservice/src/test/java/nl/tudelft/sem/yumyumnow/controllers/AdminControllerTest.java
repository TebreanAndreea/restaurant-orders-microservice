package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.yumyumnow.controller.AdminController;
import nl.tudelft.sem.yumyumnow.controller.OrderController;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import nl.tudelft.sem.yumyumnow.services.completion.OrderCompletionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

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
}
