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
            return order.getStatus() == Order.StatusEnum.ACCEPTED ? Order.StatusEnum.ON_TRANSIT :
                    Order.StatusEnum.REJECTED;
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

    @ParameterizedTest
    @MethodSource("generateFailedPayments")
    public void testPaymentFailed(Long customerId, Double price) {
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(customerId).price(price);
        assertEquals(Order.StatusEnum.REJECTED, this.paymentHandler.handleOrderCompletion(order));
    }

    /**
     * Generates the cases where the payment fails.
     *
     * @return a stream of arguments representing the failing cases
     */
    public static Stream<Arguments> generateFailedPayments() {
        return Stream.of(Arguments.of(10L, -5.0D),
                Arguments.of(15L, 0.0D),
                Arguments.of(null, 45.0D),
                Arguments.of(null, -0.5D),
                Arguments.of(null, 0.0D),
                Arguments.of(12L, null),
                Arguments.of(null, null));
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
