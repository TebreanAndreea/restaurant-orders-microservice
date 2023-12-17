package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class OrderServiceTest {

    private TestOrderRepository orderRepository;
    private OrderService orderService;

    private UserService userService;


    @BeforeEach
    public void setup() {
        this.orderRepository = new TestOrderRepository();
        this.userService = new UserService(new RestTemplate());
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
        Order order = this.orderService.createNewOrder(1L, 14L);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));

        Order storedOrder = this.orderService.getOrderById(0L);
        assertEquals(order.getCustomerId(), storedOrder.getCustomerId());
        assertEquals(order.getVendorId(), storedOrder.getVendorId());
    }
}
