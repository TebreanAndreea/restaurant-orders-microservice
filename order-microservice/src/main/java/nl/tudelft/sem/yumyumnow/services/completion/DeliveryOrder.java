package nl.tudelft.sem.yumyumnow.services.completion;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;

public class DeliveryOrder {

    private final long customerId;
    private final long vendorId;
    private final long orderId;
    public final Location destination;

    /**
     * Creates a delivery object that forms the body of the new delivery POST request.
     *
     * @param order the order to convert.
     */
    public DeliveryOrder(Order order) {
        this.customerId = order.getCustomerId();
        this.vendorId = order.getVendorId();
        this.orderId = order.getOrderId();
        this.destination = order.getLocation();
    }

    /**
     * Getter for the customer ID. Specifies JSON format.
     *
     * @return the customer ID
     */
    @JsonProperty("customer_id")
    public long getCustomerId() {
        return customerId;
    }

    /**
     * Getter for the vendor ID. Specifies JSON format.
     *
     * @return the vendor ID
     */
    @JsonProperty("vendor_id")
    public long getVendorId() {
        return vendorId;
    }

    /**
     * Getter for the order ID. Specifies JSON format.
     *
     * @return the order ID
     */
    @JsonProperty("order_id")
    public long getOrderId() {
        return orderId;
    }

    /**
     * Getter for the order destination. Specifies JSON format.
     *
     * @return the order destination
     */
    @JsonProperty("destination")
    public Location getDestination() {
        return destination;
    }
}
