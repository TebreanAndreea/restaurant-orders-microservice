package nl.tudelft.sem.yumyumnow.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderServiceTest {

    private TestOrderRepository orderRepository;
    private OrderService orderService;


    @BeforeEach
    public void setup() {
        this.orderRepository = new TestOrderRepository();
        this.orderService = new OrderService(this.orderRepository);
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
}
