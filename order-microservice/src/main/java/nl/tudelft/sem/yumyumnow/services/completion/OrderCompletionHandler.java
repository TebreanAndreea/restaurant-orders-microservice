package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public abstract class OrderCompletionHandler {

    protected final OrderCompletionHandler nextHandler;

    /**
     * Empty constructor. Initializes the next handler to NULL.
     * It will be the last handler in the Chain of Responsibility.
     */
    public OrderCompletionHandler() {
        this.nextHandler = null;
    }

    /**
     * Creates a handler instance.
     *
     * @param nextHandler the next handler to be called in the Chain of Responsibility.
     */
    public OrderCompletionHandler(OrderCompletionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Does something with the current order. If everything executes correctly, it will call the next handler.
     *
     * @param order the current order to process.
     * @return the status of the order after processing.
     */
    public abstract Order.StatusEnum handleOrderCompletion(Order order);
}
