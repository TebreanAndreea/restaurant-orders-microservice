package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    private TestOrderRepository orderRepository;
    private OrderService orderService;

    private UserService userService;

    /**
     * setup before each test.
     */

    @BeforeEach
    public void setup() {
        this.orderRepository = new TestOrderRepository();
        this.userService = mock(UserService.class);
        this.orderService = new OrderService(this.orderRepository, this.userService);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 112L;
        assertThrows(NoSuchElementException.class, () -> this.orderService.getOrderById(id),
                "No order exists with id 112");

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(0));
    }

    @Test
    public void testAddOrder() {
        when(userService.getDefaultHomeAddress(1L))
            .thenReturn(new Location());
        Order order = this.orderService.createNewOrder(1L, 14L);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));

        Order storedOrder = this.orderService.getOrderById(1L);
        assertEquals(order.getCustomerId(), storedOrder.getCustomerId());
        assertEquals(order.getVendorId(), storedOrder.getVendorId());
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrders() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(1L, 14L);


        List<Order> storedOrder = this.orderService.getAllOrders();
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(order1, storedOrder.get(0));
        assertEquals(order2, storedOrder.get(1));
        assertEquals(storedOrder.size(), 2);
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders if there are no orders in the database.
     */
    @Test
    public void testGetAllOrdersNoOrders() {
        List<Order> storedOrder = this.orderService.getAllOrders();
        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(0));
        assertTrue(storedOrder.isEmpty());
        assertEquals(storedOrder.size(), 0);
    }

    /**
     * Tests if the method getAllOrdersForCustomer returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrdersForCustomer() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(1L, 14L);


        List<Order> storedOrder = this.orderService.getAllOrdersForCustomer(1L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(order1, storedOrder.get(0));
        assertEquals(order2, storedOrder.get(1));
        assertEquals(storedOrder.size(), 2);
    }

    /**
     * Tests if the method getAllOrdersForCustomer returns a correct List of Orders if there are orders
     * in the database but none belonging to the customer.
     */
    @Test
    public void testGetAllOrdersForCustomerNoOrdersForCustomer() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(1L, 14L);

        List<Order> storedOrder = this.orderService.getAllOrdersForCustomer(2L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(2));
        assertTrue(storedOrder.isEmpty());
        assertEquals(storedOrder.size(), 0);
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders if there are no orders in the database.
     */
    @Test
    public void testGetAllOrdersForCustomerNoOrders() {
        List<Order> storedOrder = this.orderService.getAllOrdersForCustomer(1L);
        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(0));
        assertTrue(storedOrder.isEmpty());
        assertEquals(storedOrder.size(), 0);
    }

    /**
     * Tests if the method getAllOrdersForVendor returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrdersForVendor() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(3L, 14L);


        List<Order> storedOrder = this.orderService.getAllOrdersForVendor(14L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(order1, storedOrder.get(0));
        assertEquals(order2, storedOrder.get(1));
        assertEquals(storedOrder.size(), 2);
    }

    /**
     * Tests if the method getAllOrdersForVendor returns a correct List of Orders if there are orders
     * in the database but none belonging to the Vendor.
     */
    @Test
    public void testGetAllOrdersForVendorNoOrdersForVendor() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(1L, 14L);

        List<Order> storedOrder = this.orderService.getAllOrdersForVendor(2L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(2));
        assertTrue(storedOrder.isEmpty());
        assertEquals(storedOrder.size(), 0);
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders if there are no orders in the database.
     */
    @Test
    public void testGetAllOrdersForVendorNoOrders() {
        List<Order> storedOrder = this.orderService.getAllOrdersForVendor(1L);
        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(0));
        assertTrue(storedOrder.isEmpty());
        assertEquals(storedOrder.size(), 0);
    }

    /**
     * Tests the modifyOrderAdmin method when the orderId is found.
     */
    @Test
    public void modifyOrderAdmin() {
        Order order = this.orderService.createNewOrder(1L, 14L);
        Order newOrder = new Order();
        newOrder.setCustomerId(5L);
        newOrder.setVendorId(9L);

        Order modifiedOrder = this.orderService.modifyOrderAdmin(order.getOrderId(), newOrder);
        assertEquals(3, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertEquals(modifiedOrder.getCustomerId(), 5L);
        assertEquals(modifiedOrder.getVendorId(), 9L);
        assertNotEquals(modifiedOrder.getCustomerId(), 1L);
        assertNotEquals(modifiedOrder.getVendorId(), 14L);
    }

    /**
     * Tests the modifyOrderAdmin method when the orderId is not found.
     */
    @Test
    public void modifyOrderAdminNotFound() {
        Order order = this.orderService.createNewOrder(1L, 14L);

        Order modifiedOrder = this.orderService.modifyOrderAdmin(2L, order);
        assertEquals(2, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertNull(modifiedOrder);
    }

    @Test
    public void testOrderExists() {
        Order order = new Order().orderId(12L);
        this.orderRepository.save(order);
        assertTrue(this.orderService.existsAtId(12L));
        assertFalse(this.orderService.existsAtId(20L));
        assertEquals("existsById", this.orderRepository.getMethodCalls().get(2));
    }

    @Test
    public void testSetOrderStatus() {
        Order order = new Order().orderId(998L).status(Order.StatusEnum.PREPARING);
        this.orderRepository.save(order);
        assertEquals(Order.StatusEnum.PREPARING, this.orderService.getOrderById(998L).getStatus());
        this.orderService.setOrderStatus(998L, Order.StatusEnum.ON_TRANSIT);
        assertEquals(Order.StatusEnum.ON_TRANSIT, this.orderService.getOrderById(998L).getStatus());
    }

    /**
     * Tests the addDishesToOrder method when the orderId is found, but there were no previous dishes.
     */
    @Test
    public void addDishesToOrderNoPreviousDishes() {
        Order order = this.orderService.createNewOrder(1L, 14L);
        Dish d1 = new Dish();
        Dish d2 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(d1);
        dishes.add(d2);

        final List<Dish> allDishes = this.orderService.addDishesToOrder(order.getOrderId(), dishes);
        Order modifiedOrder = this.orderService.getOrderById(order.getOrderId());
        assertTrue(!modifiedOrder.getDishes().isEmpty());
        assertEquals(4, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertEquals(dishes, modifiedOrder.getDishes());
        assertEquals(modifiedOrder.getDishes().size(), 2);
        assertEquals(dishes, allDishes);
    }

    /**
     * Tests the addDishesToOrder method when the orderId is found.
     */
    @Test
    public void addDishesToOrder() {
        final Dish d1 = new Dish();
        final Dish d2 = new Dish();
        Dish d3 = new Dish();
        Dish d4 = new Dish();
        Order order = this.orderService.createNewOrder(1L, 14L);
        order.addDishesItem(d3);
        order.addDishesItem(d4);
        this.orderService.modifyOrderAdmin(order.getOrderId(), order);


        List<Dish> dishes = new ArrayList<>();
        dishes.add(d1);
        dishes.add(d2);

        List<Dish> allDishes = this.orderService.addDishesToOrder(order.getOrderId(), dishes);
        Order modifiedOrder = this.orderService.getOrderById(order.getOrderId());
        assertTrue(!modifiedOrder.getDishes().isEmpty());
        assertEquals(6, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertEquals(dishes, modifiedOrder.getDishes());
        assertEquals(modifiedOrder.getDishes().size(), 4);
    }

    /**
     * Tests the addDishesToOrder method when the orderId is not found.
     */
    @Test
    public void addDishesToOrderNotFound() {
        Order order = this.orderService.createNewOrder(1L, 14L);

        List<Dish> modifiedOrder = this.orderService.addDishesToOrder(2L, new ArrayList<Dish>());
        assertEquals(2, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertNull(modifiedOrder);
    }

    @Test
    public void modifyOrderSuccess() {
        Order order = new Order().orderId(11L).vendorId(12L).customerId(13L).location(new Location(1L, 0.0D, 0.0D))
                .price(14.5D).dishes(new ArrayList<>()).status(Order.StatusEnum.PREPARING).time(OffsetDateTime.MIN);

        this.orderRepository.save(order);

        Dish dish = new Dish().id(20L).name("Fish and Chips").allergens(List.of("gluten", "fish"));
        Location newLoc = new Location(2L, 180.0D, 180.0D);
        Order.StatusEnum status = Order.StatusEnum.ACCEPTED;
        OffsetDateTime time = OffsetDateTime.now();
        boolean success = this.orderService.updateOrder(11L, List.of(dish), newLoc, status, time);
        assertTrue(success);
        Order saved = this.orderService.getOrderById(11L);
        assertEquals(status, saved.getStatus());
        assertEquals(time, saved.getTime());
        assertEquals(newLoc, saved.getLocation());
        assertEquals(dish, saved.getDishes().get(0));
    }

    @Test
    public void modifyOrderNotFound() {
        Order order = new Order().orderId(11L).vendorId(12L).customerId(13L).location(new Location(1L, 0.0D, 0.0D))
                .price(14.5D).dishes(new ArrayList<>()).status(Order.StatusEnum.PREPARING).time(OffsetDateTime.MIN);

        this.orderRepository.save(order);

        Dish dish = new Dish().id(20L).name("Fish and Chips").allergens(List.of("gluten", "fish"));
        Location newLoc = new Location(2L, 180.0D, 180.0D);
        Order.StatusEnum status = Order.StatusEnum.ACCEPTED;
        OffsetDateTime time = OffsetDateTime.now();

        assertThrows(NoSuchElementException.class, () -> {
            this.orderService.updateOrder(15L, List.of(dish), newLoc, status, time);
        }, "No order exists with id 15");

    }
}
