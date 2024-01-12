package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public class OrderPaymentHandler extends OrderCompletionHandler {


    /**
     * Initializes an instance of the Payment Handler.
     *
     * @param nextHandler the next Order completion handler.
     */
    public OrderPaymentHandler(OrderCompletionHandler nextHandler) {
        super(nextHandler);
    }

    /**
     * Process the payment of the current order. If everything executes correctly, it will call an email handler.
     *
     * @param order the current order to process.
     * @return the status of the order after processing.
     */
    @Override
    public Order.StatusEnum handleOrderCompletion(Order order) {
        if (order.getStatus() != Order.StatusEnum.PENDING) {
            return order.getStatus();
        }
        if (processPayment(order.getCustomerId(), order.getPrice())) {
            order.setStatus(Order.StatusEnum.ACCEPTED);
            if (this.nextHandler != null) {
                return this.nextHandler.handleOrderCompletion(order);
            }
            return Order.StatusEnum.ACCEPTED;
        }
        return Order.StatusEnum.REJECTED;
    }

    /**
     * Method stubbing a call to a Payment API that would process the payment for an order by a customer.
     *
     * @param customerId the customer making the payment.
     * @param amount     the amount to pay.
     * @return true if the payment was successful.
     */
    public boolean processPayment(Long customerId, Double amount) {
        return customerId != null && amount != null && amount > 0;
    }
}
