package nl.tudelft.sem.yumyumnow.services.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DeliveryStatusTest {

    @ParameterizedTest
    @MethodSource("generateConversion")
    public void testConversionFromDelivery(String value, Order.StatusEnum status) {
        assertEquals(status, new DeliveryStatus(value).getDefaultStatus());
    }

    @ParameterizedTest
    @MethodSource("generateConversion")
    public void testConversionToDelivery(String value, Order.StatusEnum status) {
        assertEquals(value, new DeliveryStatus(status).getDeliveryStatus());
    }

    /**
     * Generator method for the conversion table.
     *
     * @return a stream of all the delivery microservice status and their corresponding status in our microservice.
     */
    public static Stream<Arguments> generateConversion() {
        return Stream.of(Arguments.of("Pending", Order.StatusEnum.PENDING),
                Arguments.of("Accepted", Order.StatusEnum.ACCEPTED),
                Arguments.of("Rejected", Order.StatusEnum.REJECTED),
                Arguments.of("Preparing", Order.StatusEnum.PREPARING),
                Arguments.of("Given_To_Courier", Order.StatusEnum.GIVEN_TO_COURIER),
                Arguments.of("On_Transit", Order.StatusEnum.ON_TRANSIT),
                Arguments.of("Delivered", Order.StatusEnum.DELIVERED));
    }

    @Test
    public void testNullOrEmptyDeliveryStatus() {
        assertEquals(Order.StatusEnum.REJECTED, new DeliveryStatus((String) null).getDefaultStatus());
        assertEquals(Order.StatusEnum.REJECTED, new DeliveryStatus("").getDefaultStatus());
    }

    @Test
    public void testNullStatus() {
        assertEquals("", new DeliveryStatus((Order.StatusEnum) null).getDeliveryStatus());
    }

    @Test
    public void testInvalidStatus() {
        assertEquals(Order.StatusEnum.REJECTED, new DeliveryStatus("invalid status").getDefaultStatus());
    }
}
