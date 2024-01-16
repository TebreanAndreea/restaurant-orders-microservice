package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.database.TestVendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.lang.Nullable;

public class UpdatesOrderServiceTest {
    private TestOrderRepository orderRepository;
    private TestVendorRepository vendorRepository;

    private TestRatingRepository ratingRepository;
    private OrderService orderService;
    private UpdatesOrderService updatesOrderService;
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
        this.updatesOrderService = new UpdatesOrderService(orderRepository, userService, orderService);
        this.vendorService = new VendorService(this.vendorRepository, this.userService, this.orderService);
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

        Order modifiedOrder = this.updatesOrderService.modifyOrderAdmin(order.getOrderId(), newOrder);
        assertEquals(3, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertEquals(modifiedOrder.getCustomerId(), 5L);
        assertEquals(modifiedOrder.getVendorId(), 9L);
        assertEquals(modifiedOrder.getOrderId(), order.getOrderId());
        assertNotEquals(modifiedOrder.getCustomerId(), 1L);
        assertNotEquals(modifiedOrder.getVendorId(), 14L);
    }

    /**
     * Tests the modifyOrderAdmin method when the orderId is not found.
     */
    @Test
    public void modifyOrderAdminNotFound() {
        Order order = this.orderService.createNewOrder(1L, 14L);

        Order modifiedOrder = this.updatesOrderService.modifyOrderAdmin(2L, order);
        assertEquals(2, this.orderRepository.getMethodCalls().size());
        assertEquals("findById", this.orderRepository.getMethodCalls().get(1));
        assertNull(modifiedOrder);
    }

    @Test
    public void testSetOrderStatus() {
        Order order = new Order().orderId(998L).status(Order.StatusEnum.PREPARING);
        this.orderRepository.save(order);
        assertEquals(Order.StatusEnum.PREPARING, this.orderService.getOrderById(998L).getStatus());
        this.updatesOrderService.setOrderStatus(998L, Order.StatusEnum.ON_TRANSIT);
        assertTrue(this.updatesOrderService.setOrderStatus(998L, Order.StatusEnum.ON_TRANSIT));
        assertEquals(Order.StatusEnum.ON_TRANSIT, this.orderService.getOrderById(998L).getStatus());
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
        boolean success = this.updatesOrderService.updateOrder(11L, dishes, newLoc, status, time);
        assertTrue(success);
        Order saved = this.orderService.getOrderById(11L);
        if (status != null) {
            assertEquals(status, saved.getStatus());
        } else {
            assertEquals(order.getStatus(), saved.getStatus());
        }
        if (newLoc != null) {
            assertEquals(newLoc, saved.getLocation());
        } else {
            assertEquals(order.getLocation(), saved.getLocation());
        }
        if (time != null) {
            assertEquals(time, saved.getTime());
        } else {
            assertEquals(order.getTime(), saved.getTime());
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
            this.updatesOrderService.updateOrder(15L, List.of(dish), newLoc, status, time);
        }, "No order exists with id 15");
    }

    @Test
    public void testRemoveDishNonExistingOrder() {
        assertThrows(NoSuchElementException.class, () ->
            this.updatesOrderService.removeDishFromOrder(12L, new Dish()));
    }

    @Test
    public void testRemoveDishNotPresent() {
        Dish d1 = new Dish().id(1L).name("Pizza Regina");
        Dish d2 = new Dish().id(2L).name("Pizza Hawaii");
        Order order = new Order().orderId(15L).dishes(new ArrayList<>(List.of(d1)));
        this.orderRepository.save(order);
        assertTrue(order.getDishes().stream().noneMatch(x -> x.getId().equals(2L)));
        assertTrue(this.updatesOrderService.removeDishFromOrder(15L, d2));
        assertEquals(1, this.orderService.getOrderById(15L).getDishes().size());
    }

    @Test
    public void testRemoveDishPresent() {
        Dish d1 = new Dish().id(1L).name("Pizza Regina");
        Dish d2 = new Dish().id(2L).name("Pizza Hawaii");
        Order order = new Order().orderId(15L).dishes(new ArrayList<>(List.of(d1, d2)));
        this.orderRepository.save(order);
        assertEquals(2, order.getDishes().size());
        assertTrue(this.updatesOrderService.removeDishFromOrder(15L, d2));
        assertEquals(1, this.orderService.getOrderById(15L).getDishes().size());
    }

    @Test
    public void testModifyRequirements() {
        Order order = new Order();
        order.setOrderId(10L);
        order.setSpecialRequirenments("No hot sauce.");

        orderRepository.save(order);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("save", this.orderRepository.getMethodCalls().get(0));

        Order modifiedOrder = new Order();
        modifiedOrder.setOrderId(order.getOrderId());
        modifiedOrder.setSpecialRequirenments("Tuna, no crust.");

        Optional<Order> retrievedOrder = updatesOrderService.modifyOrderRequirements(modifiedOrder);

        assertEquals(10L, retrievedOrder.get().getOrderId());
        assertEquals("Tuna, no crust.", retrievedOrder.get().getSpecialRequirenments());
        assertEquals(3, this.orderRepository.getMethodCalls().size());
        assertEquals("existsById", this.orderRepository.getMethodCalls().get(1));
        assertEquals("save", this.orderRepository.getMethodCalls().get(2));
    }

    @Test
    public void testModifyRequirementsOrderNotFound() {
        Order modifiedOrder = new Order();
        modifiedOrder.setOrderId(10L);
        modifiedOrder.setSpecialRequirenments("Tuna, no crust.");

        Optional<Order> retrievedOrder = updatesOrderService.modifyOrderRequirements(modifiedOrder);

        assertEquals(1, this.orderRepository.getMethodCalls().size());
        assertEquals("existsById", this.orderRepository.getMethodCalls().get(0));
        assertEquals(Optional.empty(), retrievedOrder);
    }
}
