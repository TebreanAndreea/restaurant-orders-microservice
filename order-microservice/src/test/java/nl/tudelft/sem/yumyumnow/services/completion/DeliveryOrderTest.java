package nl.tudelft.sem.yumyumnow.services.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.Test;

public class DeliveryOrderTest {

    @Test
    public void testGetters() {
        Location location = new Location(12L, 3.0022D, 56.2393D);
        Order order = new Order().orderId(120L).customerId(9293L).vendorId(45L).location(location);
        assertEquals(120L, new DeliveryOrder(order).getOrderId());
        assertEquals(9293L, new DeliveryOrder(order).getCustomerId());
        assertEquals(45L, new DeliveryOrder(order).getVendorId());
        assertEquals(location, new DeliveryOrder(order).getDestination());
    }
}
