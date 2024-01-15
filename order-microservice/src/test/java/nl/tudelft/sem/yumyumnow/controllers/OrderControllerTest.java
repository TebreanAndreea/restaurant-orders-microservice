package nl.tudelft.sem.yumyumnow.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

public class OrderControllerTest {

    private OrderService orderService;
    private AuthenticationService authenticationService;
    private CompletionFactory orderCompletionService;
    private OrderController orderController;

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
     * Tests the getListOfOrdersForCustomers method with invalid customer ID.
     */
    @Test
    public void getListOfOrdersForCustomersInvalidId() {
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, orderController.getListOfOrdersForCustomers(100L).getStatusCode());
    }

    /**
     * Tests if the method getListOfOrdersForCustomers returns a correct List of Orders.
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

        List<Order> ordersReceived = orderController.getListOfOrdersForCustomers(2L).getBody();
        assertNotNull(ordersReceived);
        assertEquals(2L, ordersReceived.get(0).getCustomerId());
        assertEquals(3L, ordersReceived.get(0).getVendorId());
        assertEquals(2L, ordersReceived.get(1).getCustomerId());
        assertEquals(7L, ordersReceived.get(1).getVendorId());
        assertEquals(2, ordersReceived.size());
    }

    /**
     * Tests if the method getListOfOrdersForCustomers returns a correct List of Orders if there
     * are no orders.
     */
    @Test
    public void testGetAllOrdersCustomerNoOrders() {
        Mockito.when(this.authenticationService.isCustomer(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForCustomer(Mockito.anyLong())).thenReturn(new ArrayList<Order>());

        List<Order> orders = orderController.getListOfOrdersForCustomers(2L).getBody();
        assertNotNull(orders);
        assertEquals(orders, new ArrayList<Order>());
    }

    @Test
    public void testGetAllOrdersCustomerInternalError() {
        Mockito.when(this.authenticationService.isCustomer(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForCustomer(Mockito.anyLong())).thenThrow(new
            NoSuchElementException("No order exists with id 25"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, orderController.getListOfOrdersForCustomers(2L).getStatusCode());
    }

    /**
     * Tests the getListOfOrdersForVendor method with invalid Vendor ID.
     */
    @Test
    public void getListOfOrdersForVendorInvalidId() {
        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, orderController.getListOfOrdersForVendor(100L).getStatusCode());
    }

    /**
     * Tests if the method getListOfOrdersForVendor returns a correct List of Orders.
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

        List<Order> ordersReceived = orderController.getListOfOrdersForVendor(3L).getBody();
        assertNotNull(ordersReceived);
        assertEquals(1L, ordersReceived.get(0).getCustomerId());
        assertEquals(3L, ordersReceived.get(0).getVendorId());
        assertEquals(2L, ordersReceived.get(1).getCustomerId());
        assertEquals(3L, ordersReceived.get(1).getVendorId());
        assertEquals(2, ordersReceived.size());
    }

    /**
     * Tests if the method getListOfOrdersForVendor returns a correct List of Orders if there
     * are no orders.
     */
    @Test
    public void testGetAllOrdersVendorNoOrders() {
        Mockito.when(this.authenticationService.isVendor(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForVendor(Mockito.anyLong())).thenReturn(new ArrayList<Order>());

        List<Order> orders = orderController.getListOfOrdersForVendor(2L).getBody();
        assertNotNull(orders);
        assertEquals(orders, new ArrayList<Order>());
    }

    @Test
    public void testGetAllOrdersVendorInternalError() {
        Mockito.when(this.authenticationService.isVendor(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.orderService.getAllOrdersForVendor(Mockito.anyLong())).thenThrow(new
            NoSuchElementException("No order exists with id 25"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, orderController.getListOfOrdersForVendor(2L).getStatusCode());
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
     * Tests the addDishToOrder method with invalid customer ID.
     */
    @Test
    public void addDishToOrderInvalidCustomerId() {
        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, orderController.addDishToOrder(100L, 100L,
                new Dish()).getStatusCode());
    }

    /**
     * Tests the addDishToOrder method when the dish to add is null.
     */
    @Test
    public void addDishToOrderNoDishes() {
        Order order = new Order();
        order.setCustomerId(100L);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(100L)).thenReturn(order);

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Tests the addDishToOrder method when the order id is not found.
     */
    @Test
    public void addDishToOrderNotFound() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(false);

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, d1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests the addDishToOrder method.
     */
    @Test
    public void addDishToOrder() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        Order order = new Order();
        order.setCustomerId(100L);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(100L)).thenReturn(order);

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, d1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addDishToOrderUnauthorized() {
        Order order = new Order();
        order.setCustomerId(200L);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(100L)).thenReturn(order);

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, new Dish());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void addDishToOrderNullSaved() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        Order order = new Order();
        order.setCustomerId(100L);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(100L)).thenReturn(order);
        Mockito.when(this.orderService.addDishToOrder(100L, d1)).thenReturn(null);

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, d1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddDishToOrderInternalError() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        Order order = new Order();
        order.setCustomerId(100L);

        Mockito.when(this.authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(this.orderService.existsAtId(100L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(100L)).thenReturn(order);
        Mockito.when(this.orderService.addDishToOrder(100L, d1)).thenThrow(new
            NoSuchElementException("No order exists with id 25"));

        ResponseEntity<Void> response = orderController.addDishToOrder(100L, 100L, d1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testModifyOrderNotFound() {
        Mockito.when(this.orderService.existsAtId(15L)).thenReturn(false);
        assertEquals(HttpStatus.NOT_FOUND, this.orderController
                .modifyOrder(15L, 1L, List.of(), null, "", null)
                .getStatusCode());
    }

    @Test
    public void testModifyOrderUnauthorized() {
        Order order = new Order().orderId(16L).customerId(3L);

        Mockito.when(this.orderService.existsAtId(16L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(16L)).thenReturn(order);
        Mockito.when(this.authenticationService.isAdmin(2L)).thenReturn(false);

        assertEquals(HttpStatus.UNAUTHORIZED, this.orderController
                .modifyOrder(16L, 2L, List.of(), null, "", null)
                .getStatusCode());
    }

    @Test
    public void testModifyOrderInvalidStatus() {
        Order order = new Order().orderId(16L).customerId(3L);

        Mockito.when(this.orderService.existsAtId(16L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(16L)).thenReturn(order);
        Mockito.when(this.authenticationService.isAdmin(2L)).thenReturn(true);
        Mockito.when(this.orderService.updateOrder(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any())).thenReturn(true);

        assertEquals(HttpStatus.BAD_REQUEST, this.orderController
                .modifyOrder(16L, 2L, List.of(), null, "haha", null)
                .getStatusCode());

        assertEquals(HttpStatus.OK, this.orderController
                .modifyOrder(16L, 3L, List.of(), null, "", null)
                .getStatusCode());

        assertEquals(HttpStatus.OK, this.orderController
                .modifyOrder(16L, 3L, List.of(), null, null, null)
                .getStatusCode());
    }

    @Test
    public void testModifyOrderOk() {
        Order order = new Order().orderId(25L).customerId(6L);

        List<Dish> dishes = List.of(new Dish().id(111L).name("Pizza !!!"));
        OffsetDateTime time = OffsetDateTime.now();
        Location loc = new Location(9L, 3.0D, 51.0D);

        Mockito.when(this.orderService.existsAtId(25L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(25L)).thenReturn(order);
        Mockito.when(this.orderService.updateOrder(25L, dishes, loc, Order.StatusEnum.ON_TRANSIT, time))
                .thenReturn(true);

        String status = "on-transit";

        assertEquals(HttpStatus.OK, this.orderController
                .modifyOrder(25L, 6L, dishes, loc, status, time)
                .getStatusCode());
    }

    @Test
    public void testModifyOrderInternalError() {
        Order order = new Order().orderId(25L).customerId(6L);

        List<Dish> dishes = List.of(new Dish().id(111L).name("Pizza !!!"));
        OffsetDateTime time = OffsetDateTime.now();
        Location loc = new Location(9L, 3.0D, 51.0D);

        Mockito.when(this.orderService.existsAtId(25L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(25L)).thenReturn(order);
        Mockito.when(this.orderService.updateOrder(25L, dishes, loc, Order.StatusEnum.ON_TRANSIT, time))
                .thenReturn(false);

        String status = "on-transit";

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.orderController
                .modifyOrder(25L, 6L, dishes, loc, status, time)
                .getStatusCode());
    }

    @Test
    public void testModifyOrderInternalError2() {
        Order order = new Order().orderId(25L).customerId(6L);

        List<Dish> dishes = List.of(new Dish().id(111L).name("Pizza !!!"));
        OffsetDateTime time = OffsetDateTime.now();
        Location loc = new Location(9L, 3.0D, 51.0D);

        Mockito.when(this.orderService.existsAtId(25L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(25L)).thenReturn(order);
        Mockito.when(this.orderService.updateOrder(25L, dishes, loc, Order.StatusEnum.ON_TRANSIT, time))
                .thenThrow(new NoSuchElementException("No order exists with id 25"));

        String status = "on-transit";

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.orderController
                .modifyOrder(25L, 6L, dishes, loc, status, time)
                .getStatusCode());
    }

    @Test
    public void testGetOrderUnauthorized() {
        Mockito.when(this.orderService.isUserAssociatedWithOrder(1L, 100L)).thenReturn(false);

        ResponseEntity<Order> response = this.orderController.getOrder(1L, 100L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetOrderNotFound() {
        Mockito.when(this.orderService.isUserAssociatedWithOrder(2L, 101L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(2L)).thenThrow(NoSuchElementException.class);

        ResponseEntity<Order> response = this.orderController.getOrder(2L, 101L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderFound() {
        Order order = new Order();

        Mockito.when(this.orderService.isUserAssociatedWithOrder(3L, 102L)).thenReturn(true);
        Mockito.when(this.orderService.getOrderById(3L)).thenReturn(order);

        ResponseEntity<Order> response = this.orderController.getOrder(3L, 102L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests the deleteOrder method.
     */
    @Test
    public void deleteOrder() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = orderController.deleteOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * Tests the deleteOrder method when the admin is unauthorized.
     */
    @Test
    public void deleteOrderNotAuthorized() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = orderController.deleteOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    /**
     * Tests the deleteOrder method when the order is not found.
     */
    @Test
    public void deleteOrderNotFound() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(false);

        ResponseEntity<Void> statusCode = orderController.deleteOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testDeleteDishFromOrderNotFound() {
        Mockito.when(this.orderService.existsAtId(200L)).thenReturn(false);
        Dish d = new Dish();
        assertEquals(HttpStatus.NOT_FOUND, this.orderController
                .removeDishFromOrder(200L, 12L, d).getStatusCode());
    }

    @Test
    public void testDeleteDishFromOrderNotAuthorized() {
        Mockito.when(this.orderService.existsAtId(200L)).thenReturn(true);
        Mockito.when(this.orderService.isUserAssociatedWithOrder(200L, 12L)).thenReturn(false);
        Dish d = new Dish();
        assertEquals(HttpStatus.UNAUTHORIZED, this.orderController
                .removeDishFromOrder(200L, 12L, d).getStatusCode());
    }

    @Test
    public void testDeleteDishFromOrderOk() {
        Dish d = new Dish();
        Mockito.when(this.orderService.existsAtId(200L)).thenReturn(true);
        Mockito.when(this.orderService.isUserAssociatedWithOrder(200L, 12L)).thenReturn(true);
        Mockito.when(this.orderService.removeDishFromOrder(200L, d)).thenReturn(true);
        assertEquals(HttpStatus.OK, this.orderController
                .removeDishFromOrder(200L, 12L, d).getStatusCode());
    }

    @Test
    public void testDeleteDishFromOrderError() {
        Dish d = new Dish();
        Mockito.when(this.orderService.existsAtId(200L)).thenReturn(true);
        Mockito.when(this.orderService.isUserAssociatedWithOrder(200L, 12L)).thenReturn(true);
        Mockito.when(this.orderService.removeDishFromOrder(200L, d)).thenReturn(false);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.orderController
                .removeDishFromOrder(200L, 12L, d).getStatusCode());
    }
    
    @Test
    public void testSetOrderRequirements() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setSpecialRequirenments("No hot sauce.");

        Order modifiedOrder = new Order();
        modifiedOrder.setOrderId(order.getOrderId());
        modifiedOrder.setCustomerId(order.getCustomerId());
        modifiedOrder.setSpecialRequirenments("Tuna, no crust.");

        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenReturn(order);
        Mockito.when(orderService.modifyOrderRequirements(modifiedOrder)).thenReturn(Optional.of(modifiedOrder));

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSetOrderRequirementsUnauthorized1() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setSpecialRequirenments("No hot sauce.");

        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(false);

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testSetOrderRequirementsUnauthorized2() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setSpecialRequirenments("No hot sauce.");

        Mockito.when(authenticationService.isCustomer(100L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 100L)).thenReturn(false);

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testSetOrderRequirementsUpdateEmpty() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setSpecialRequirenments("No hot sauce.");

        Order modifiedOrder = new Order();
        modifiedOrder.setOrderId(order.getOrderId());
        modifiedOrder.setCustomerId(order.getCustomerId());
        modifiedOrder.setSpecialRequirenments("Tuna, no crust.");

        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenReturn(order);
        Mockito.when(orderService.modifyOrderRequirements(modifiedOrder)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSetOrderRequirementsOrderNotFound() {
        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenThrow(new NoSuchElementException());

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSetOrderRequirementsServerError() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setSpecialRequirenments("No hot sauce.");

        Order modifiedOrder = new Order();
        modifiedOrder.setOrderId(order.getOrderId());
        modifiedOrder.setCustomerId(order.getCustomerId());
        modifiedOrder.setSpecialRequirenments("Tuna, no crust.");

        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenReturn(order);
        Mockito.when(orderService.modifyOrderRequirements(modifiedOrder)).thenThrow(new RuntimeException());

        ResponseEntity<Void> response = orderController.setOrderRequirements(10L, 11L, "Tuna, no crust.");
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSetOrderStatus() {
        Long orderId = 1L;
        Long userId = 100L;
        String body = "accepted";

        Mockito.when(authenticationService.isVendor(userId)).thenReturn(true);
        Mockito.when(orderService.setOrderStatus(orderId, Order.StatusEnum.fromValue(body))).thenReturn(true);

        ResponseEntity<Void> response = orderController.setOrderStatus(orderId, userId, body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSetOrderStatusUnauthorized() {
        Long orderId = 1L;
        Long userId = 200L;
        String body = "accepted";

        Mockito.when(authenticationService.isVendor(userId)).thenReturn(false);

        ResponseEntity<Void> response = orderController.setOrderStatus(orderId, userId, body);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testSetOrderStatusInternalServerError() {
        Long orderId = 1L;
        Long userId = 300L;
        String body = "invalid";

        Mockito.when(authenticationService.isVendor(userId)).thenReturn(true);

        ResponseEntity<Void> response = orderController.setOrderStatus(orderId, userId, body);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOrderStatus() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);
        order.setStatus(Order.StatusEnum.PENDING);

        Mockito.when(authenticationService.isCustomer(order.getCustomerId())).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(order.getOrderId(), order.getCustomerId())).thenReturn(true);
        Mockito.when(orderService.getOrderById(order.getOrderId())).thenReturn(order);

        ResponseEntity<String> response = orderController.getOrderStatus(order.getOrderId(), order.getCustomerId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("pending", response.getBody());
    }

    @Test
    public void testGetOrderStatusUnauthorized1() {
        Mockito.when(authenticationService.isCustomer(10L)).thenReturn(false);

        ResponseEntity<String> response = orderController.getOrderStatus(11L, 10L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetOrderStatusUnauthorized2() {
        Mockito.when(authenticationService.isCustomer(10L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(11L, 10L)).thenReturn(false);

        ResponseEntity<String> response = orderController.getOrderStatus(11L, 10L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetOrderStatusNull() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setCustomerId(11L);

        Mockito.when(authenticationService.isCustomer(order.getCustomerId())).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(order.getOrderId(), order.getCustomerId())).thenReturn(true);
        Mockito.when(orderService.getOrderById(order.getOrderId())).thenReturn(order);

        ResponseEntity<String> response = orderController.getOrderStatus(order.getOrderId(), order.getCustomerId());

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOrderStatusNotFound() {
        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenThrow(new NoSuchElementException());

        ResponseEntity<String> response = orderController.getOrderStatus(10L, 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderStatusError() {
        Mockito.when(authenticationService.isCustomer(11L)).thenReturn(true);
        Mockito.when(orderService.isUserAssociatedWithOrder(10L, 11L)).thenReturn(true);
        Mockito.when(orderService.getOrderById(10L)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = orderController.getOrderStatus(10L, 11L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
