package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.mockito.Mockito;

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

        Order storedOrder = this.orderService.getOrderById(0L);
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
}
