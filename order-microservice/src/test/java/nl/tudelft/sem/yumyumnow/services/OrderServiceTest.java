package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.database.TestVendorRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public class OrderServiceTest {

    private TestOrderRepository orderRepository;
    private TestVendorRepository vendorRepository;

    private TestRatingRepository ratingRepository;
    private OrderService orderService;
    private VendorService vendorService;
    private CustomerService userService;

    /**
     * setup before each test.
     */

    @BeforeEach
    public void setup() {
        this.orderRepository = new TestOrderRepository();
        this.vendorRepository = new TestVendorRepository();
        this.ratingRepository = new TestRatingRepository();
        this.userService = mock(CustomerService.class);
        this.orderService = new OrderService(this.orderRepository, this.userService);
        this.vendorService = new VendorService(this.vendorRepository, this.userService, this.orderService);
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
        Location location = new Location();
        location.setLatitude(83.56);
        location.setLongitude(23.34);
        when(userService.getDefaultHomeAddress(1L))
            .thenReturn(location);
        Order order = this.orderService.createNewOrder(1L, 14L);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));

        Order storedOrder = this.orderService.getOrderById(order.getOrderId());
        assertEquals(order.getCustomerId(), storedOrder.getCustomerId());
        assertEquals(order.getVendorId(), storedOrder.getVendorId());
        assertEquals(order.getLocation(), storedOrder.getLocation());
        assertNotNull(storedOrder.getLocation());
    }

    @Test
    public void testAddOrderNullLocation() {
        when(userService.getDefaultHomeAddress(1L))
            .thenReturn(null);
        Order order = this.orderService.createNewOrder(1L, 14L);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));

        Order storedOrder = this.orderService.getOrderById(order.getOrderId());
        assertEquals(order.getCustomerId(), storedOrder.getCustomerId());
        assertEquals(order.getVendorId(), storedOrder.getVendorId());
        assertNull(storedOrder.getLocation());
    }

    /**
     * Tests if the method getAllOrders returns a correct List of Orders.
     */
    @Test
    public void testGetAllOrders() {
        Order order1 = this.orderService.createNewOrder(1L, 14L);
        Order order2 = this.orderService.createNewOrder(1L, 14L);
        List<Order> orders = List.of(order1, order2);

        List<Order> storedOrder = this.orderService.getAllOrders();
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(storedOrder.size(), 2);
        assertTrue(orders.containsAll(storedOrder));
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
        List<Order> orders = List.of(order1, order2);

        List<Order> storedOrder = this.orderService.getAllOrdersForCustomer(1L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(storedOrder.size(), 2);
        assertTrue(orders.containsAll(storedOrder));
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
        List<Order> orders = List.of(order1, order2);

        List<Order> storedOrder = this.orderService.getAllOrdersForVendor(14L);
        assertEquals(3, this.orderRepository.getMethodCalls().size());

        assertEquals(storedOrder.size(), 2);
        assertTrue(orders.containsAll(storedOrder));
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

    @Test
    public void testOrderExists() {
        Order order = new Order().orderId(12L);
        this.orderRepository.save(order);
        assertTrue(this.orderService.existsAtId(12L));
        assertFalse(this.orderService.existsAtId(20L));
        assertEquals("existsById", this.orderRepository.getMethodCalls().get(2));
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

    @Test
    public void testGetAllRatingsForVendorNoOrders() {
        Vendor vendor = new Vendor();
        vendor.setName("Pizza Hut");
        vendorRepository.save(vendor);

        List<Long> getAllRatings = orderService.getAllRatingsForVendor(vendor.getId());
        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("findAll", this.orderRepository.getMethodCalls().get(0));
        assertNull(getAllRatings);
    }

    @Test
    public void testGetAllRatingsForVendor() {
        Vendor vendor = new Vendor();
        vendor.setName("Pizza Hut");
        vendorRepository.save(vendor);

        Rating rating = new Rating();
        rating.setComment("Good");
        rating.setGrade(5L);
        ratingRepository.save(rating);

        Order order = new Order();
        order.setOrderId(10L);
        order.setVendorId(vendor.getId());
        order.setRatingId(rating.getId());
        orderRepository.save(order);

        List<Long> getAllRatings = orderService.getAllRatingsForVendor(vendor.getId());
        assertEquals(2, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));
        assertEquals(0, getAllRatings.get(0));
        assertEquals(1, getAllRatings.size());
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


        List<Order> orderReceived = orderService.getListOfOrdersForVendorForClient(1L, customer);
        List<Order> answer = List.of(order1, order2);
        assertEquals(answer, orderReceived);
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


        List<Order> orderReceived = orderService.getListOfOrdersForVendorForClient(1L, customer);
        assertEquals(new ArrayList<>(), orderReceived);
    }

    @Test
    public void getListOfOrdersForVendorForClienNullOrders() {
        Customer customer = new Customer();
        customer.setPastOrders(null);

        List<Order> orderReceived = orderService.getListOfOrdersForVendorForClient(1L, customer);
        assertNull(orderReceived);
    }

    @Test
    public void getListOfOrdersForVendorForClientTesNullCustomer() {
        List<Order> orderReceived = orderService.getListOfOrdersForVendorForClient(1L, null);
        assertNull(orderReceived);
    }
}
