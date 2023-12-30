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

public class PaymentHandlerTest {

    private OrderPaymentHandler paymentHandler;
    private final OrderCompletionHandler stubHandler = new OrderCompletionHandler() {
        @Override
        public Order.StatusEnum handleOrderCompletion(Order order) {
            return Order.StatusEnum.ON_TRANSIT;
        }
    };

    @BeforeEach
    public void setup() {
        this.paymentHandler = new OrderPaymentHandler(null);
    }

    @ParameterizedTest
    @MethodSource("generateInvalidStatus")
    public void testInvalidStatus(Order.StatusEnum status) {
        Order order = new Order().status(status);
        assertEquals(status, this.paymentHandler.handleOrderCompletion(order));
    }

    private static Stream<Arguments> generateInvalidStatus() {
        return Arrays.stream(Order.StatusEnum.values())
                .filter(x -> x != Order.StatusEnum.PENDING)
                .map(Arguments::of);
    }

    @Test
    public void testPaymentFailed() {
        Order order1 = new Order().status(Order.StatusEnum.PENDING).customerId(10L).price(-5.0D);
        Order order2 = new Order().status(Order.StatusEnum.PENDING).customerId(null).price(45.0D);
        Order order3 = new Order().status(Order.StatusEnum.PENDING).customerId(null).price(0.0D);
        assertEquals(Order.StatusEnum.REJECTED, this.paymentHandler.handleOrderCompletion(order1));
        assertEquals(Order.StatusEnum.REJECTED, this.paymentHandler.handleOrderCompletion(order2));
        assertEquals(Order.StatusEnum.REJECTED, this.paymentHandler.handleOrderCompletion(order3));
    }

    @Test
    public void testPaymentEndOfChain() {
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(1L).price(36.5D);
        assertEquals(Order.StatusEnum.ACCEPTED, this.paymentHandler.handleOrderCompletion(order));
    }

    @Test
    public void testPaymentCallsNextHandler() {
        this.paymentHandler = new OrderPaymentHandler(stubHandler);
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(100L).price(20.15D);
        assertEquals(Order.StatusEnum.ON_TRANSIT, this.paymentHandler.handleOrderCompletion(order));
    }


}
