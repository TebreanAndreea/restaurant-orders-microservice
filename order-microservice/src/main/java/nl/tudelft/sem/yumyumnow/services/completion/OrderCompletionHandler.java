package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public interface OrderCompletionHandler {

    /**
     * Set the next handler in the CoR.
     *
     * @param nextHandler the next handler. (Can be null)
     */
    void setNext(OrderCompletionHandler nextHandler);

    /**
     * Handles the order to complete, and calls the next handler if it is successful.
     *
     * @param order the order to complete.
     * @return the order status after handling.
     */
    Order.StatusEnum handleOrderCompletion(Order order);
}
