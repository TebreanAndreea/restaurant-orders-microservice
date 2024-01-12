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
import java.util.stream.Stream;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.lang.Nullable;

public class OrderServiceTest {

    private TestOrderRepository orderRepository;
    private OrderService orderService;

    private CustomerService userService;

    /**
     * setup before each test.
     */

    @BeforeEach
    public void setup() {
        this.orderRepository = new TestOrderRepository();
        this.userService = mock(CustomerService.class);
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
     * Tests the addDishToOrder method when the orderId is found, but there were no previous dishes.
     */
    @Test
    public void addDishToOrderNoPreviousDishes() {
        Order order = this.orderService.createNewOrder(1L, 14L);
        Dish d1 = new Dish();
        d1.setName("Pizza");

        final List<Dish> allDishes = this.orderService.addDishToOrder(order.getOrderId(), d1);
        Order modifiedOrder = this.orderService.getOrderById(order.getOrderId());
        assertFalse(modifiedOrder.getDishes().isEmpty());
        assertEquals(4, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertTrue(modifiedOrder.getDishes().contains(d1));
        assertEquals(modifiedOrder.getDishes().size(), 1);
    }

    /**
     * Tests the addDishToOrder method when the orderId is found.
     */
    @Test
    public void addDishToOrder() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        Dish d2 = new Dish();
        d2.setName("Pasta");

        Order order = this.orderService.createNewOrder(1L, 14L);

        List<Dish> dishes = new ArrayList<>();
        dishes.add(d1);
        dishes.add(d2);

        List<Dish> allDishes = this.orderService.addDishToOrder(order.getOrderId(), d1);
        Order modifiedOrder = this.orderService.getOrderById(order.getOrderId());
        assertFalse(allDishes.isEmpty());
        assertEquals(4, this.orderRepository.getMethodCalls().size());
        assertTrue(modifiedOrder.getDishes().contains(d1));
        assertEquals(modifiedOrder.getDishes().size(), 1);
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));

        allDishes = this.orderService.addDishToOrder(order.getOrderId(), d2);
        modifiedOrder = this.orderService.getOrderById(order.getOrderId());
        assertEquals(7, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertEquals(dishes, modifiedOrder.getDishes());
        assertEquals(modifiedOrder.getDishes().size(), 2);
    }

    /**
     * Tests the addDishToOrder method when the orderId is not found.
     */
    @Test
    public void addDishToOrderNotFound() {
        Dish d1 = new Dish();
        d1.setName("Pizza");

        List<Dish> modifiedOrder = this.orderService.addDishToOrder(2L, d1);
        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(0));
        assertNull(modifiedOrder);
    }

    /**
     * Tests the order modifications with the following attributes.
     *
     * @param dishes the new dish list
     * @param newLoc the new delivery location
     * @param status the new order status
     * @param time the new delivery time
     */
    @ParameterizedTest
    @MethodSource("generateOrderModifications")
    public void modifyOrderSuccess(@Nullable List<Dish> dishes, @Nullable Location newLoc,
                                   @Nullable Order.StatusEnum status, @Nullable OffsetDateTime time) {
        Order order = new Order().orderId(11L).vendorId(12L).customerId(13L).location(new Location(1L, 0.0D, 0.0D))
                .price(14.5D).dishes(new ArrayList<>()).status(Order.StatusEnum.PREPARING).time(OffsetDateTime.MIN);

        this.orderRepository.save(order);
        boolean success = this.orderService.updateOrder(11L, dishes, newLoc, status, time);
        assertTrue(success);
        Order saved = this.orderService.getOrderById(11L);
        if (status != null) {
            assertEquals(status, saved.getStatus());
        }
        if (newLoc != null) {
            assertEquals(newLoc, saved.getLocation());
        }
        if (time != null) {
            assertEquals(time, saved.getTime());
        }
        if (dishes != null) {
            assertEquals(dishes.get(0), saved.getDishes().get(0));
        }
    }

    /**
     * Generates the order modification test arguments.
     * It contains a few null value to test that it is not updating them.
     *
     * @return the order modification test arguments
     */
    public static Stream<Arguments> generateOrderModifications() {
        return Stream.of(Arguments.of(List.of(new Dish().id(11L).name("Pizza")), new Location(3L, 120.0D, 32.0D),
                Order.StatusEnum.DELIVERED, OffsetDateTime.now()),
                Arguments.of(List.of(new Dish().id(11L).name("Pizza")), new Location(3L, 120.0D, 32.0D),
                        Order.StatusEnum.PREPARING, null),
                Arguments.of(List.of(new Dish().id(11L).name("Pizza")), new Location(3L, 120.0D, 32.0D),
                        null, null),
                Arguments.of(List.of(new Dish().id(11L).name("Burger")), null,
                        null, OffsetDateTime.now()),
                Arguments.of(null, new Location(3L, 120.0D, 32.0D),
                        Order.StatusEnum.DELIVERED, null),
                Arguments.of(null, null, null, null));
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

    @Test
    public void testUserNotAssociatedWithOrder() {
        this.orderService.createNewOrder(11L, 13L).setOrderId(20L);
        assertFalse(this.orderService.isUserAssociatedWithOrder(20L, 12L));
    }

    @Test
    public void testUserAssociatedWithOrder() {
        this.orderService.createNewOrder(11L, 13L).setOrderId(20L);
        assertTrue(this.orderService.isUserAssociatedWithOrder(20L, 11L));
        assertTrue(this.orderService.isUserAssociatedWithOrder(20L, 13L));
    }

    @Test
    public void testNonExistentOrder() {
        assertFalse(orderService.isUserAssociatedWithOrder(9L, 15L));
    }

    /**
     * Tests if the method deleteOrder deletes the order.
     */
    @Test
    public void testDeleteOrder() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(2L, 15L);


        Boolean deleted1 = this.orderService.deleteOrder(order1.getOrderId());
        Boolean deleted2 = this.orderService.deleteOrder(38290L);
        assertEquals(5, this.orderRepository.getMethodCalls().size());
        List<Order> foundOrders = this.orderService.getAllOrders();

        assertEquals(deleted1, true);
        assertEquals(deleted2, false);
        assertEquals(foundOrders.size(), 1);
    }
}
