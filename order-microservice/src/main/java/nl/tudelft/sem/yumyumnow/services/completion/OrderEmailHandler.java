package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;

public class OrderEmailHandler extends OrderCompletionHandler {

    /**
     * Initializes an instance of the Payment Handler. The next handler is the delivery handler.
     */
    public OrderEmailHandler(IntegrationService integrationService) {
        super(new OrderDeliveryHandler(integrationService));
    }

    /**
     * Notify the vendor about the current order. If everything executes correctly, it will call a delivery handler.
     *
     * @param order the current order to process.
     * @return the status of the order after processing.
     */
    @Override
    public Order.StatusEnum handleOrderCompletion(Order order) {
        if (order.getStatus() != Order.StatusEnum.ACCEPTED) {
            return order.getStatus();
        }
        if (notifyVendor(order.getVendorId(), order.getOrderId())) {
            order.setStatus(Order.StatusEnum.PREPARING);
            if (this.nextHandler != null) {
                return this.nextHandler.handleOrderCompletion(order);
            }
            return Order.StatusEnum.PREPARING;
        }
        return Order.StatusEnum.REJECTED;
    }

    /**
     * Method stubbing a call to an Email API that would notify the vendor that the order must be prepared.
     *
     * @param vendorId the vendor to notify.
     * @param orderId  the orderId representing the order to prepare.
     * @return true if the vendor was notified successfully and is now preparing the dishes.
     */
    public boolean notifyVendor(Long vendorId, Long orderId) {
        return vendorId != null && orderId != null;
    }
}
