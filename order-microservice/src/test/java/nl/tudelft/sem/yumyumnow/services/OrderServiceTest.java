package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
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
}
