package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public class OrderEmailHandler extends BaseOrderCompletionHandler {

    /**
     * Initializes an instance of the Email Handler.
     *
     * @param nextHandler the next Order completion handler. Can be null.
     */
    public OrderEmailHandler(OrderCompletionHandler nextHandler) {
        super();
        this.setNext(nextHandler);
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
