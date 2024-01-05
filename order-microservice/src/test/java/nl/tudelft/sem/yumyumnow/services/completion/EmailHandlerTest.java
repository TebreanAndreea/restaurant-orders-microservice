package nl.tudelft.sem.yumyumnow.services.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Stream;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EmailHandlerTest {

    private OrderEmailHandler emailHandler;
    private final OrderCompletionHandler stubHandler = new OrderCompletionHandler() {
        @Override
        public Order.StatusEnum handleOrderCompletion(Order order) {
            return Order.StatusEnum.ON_TRANSIT;
        }
    };

    @BeforeEach
    public void setup() {
        this.emailHandler = new OrderEmailHandler(null);
    }

    @ParameterizedTest
    @MethodSource("generateInvalidStatus")
    public void testInvalidStatus(Order.StatusEnum status) {
        Order order = new Order().status(status);
        assertEquals(status, this.emailHandler.handleOrderCompletion(order));
    }

    private static Stream<Arguments> generateInvalidStatus() {
        return Arrays.stream(Order.StatusEnum.values())
                .filter(x -> x != Order.StatusEnum.ACCEPTED)
                .map(Arguments::of);
    }

    @Test
    public void testVendorNotificationFailed() {
        Order order1 = new Order().status(Order.StatusEnum.ACCEPTED).vendorId(10L).orderId(null);
        Order order2 = new Order().status(Order.StatusEnum.ACCEPTED).vendorId(null).orderId(449593L);
        Order order3 = new Order().status(Order.StatusEnum.ACCEPTED).vendorId(null).orderId(null);
        assertEquals(Order.StatusEnum.REJECTED, this.emailHandler.handleOrderCompletion(order1));
        assertEquals(Order.StatusEnum.REJECTED, this.emailHandler.handleOrderCompletion(order2));
        assertEquals(Order.StatusEnum.REJECTED, this.emailHandler.handleOrderCompletion(order3));
    }

    @Test
    public void testVendorNotificationEndOfChain() {
        Order order = new Order().status(Order.StatusEnum.ACCEPTED).vendorId(1L).orderId(2L);
        assertEquals(Order.StatusEnum.PREPARING, this.emailHandler.handleOrderCompletion(order));
    }

    @Test
    public void testVendorNotificationCallsNextHandler() {
        this.emailHandler = new OrderEmailHandler(stubHandler);
        Order order = new Order().status(Order.StatusEnum.ACCEPTED).vendorId(1L).orderId(2L);
        assertEquals(Order.StatusEnum.ON_TRANSIT, this.emailHandler.handleOrderCompletion(order));
    }


}
