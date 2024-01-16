package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public abstract class BaseOrderCompletionHandler implements OrderCompletionHandler {

    protected OrderCompletionHandler nextHandler;

    /**
     * Empty constructor. Initializes the next handler to NULL.
     * It will be the last handler in the Chain of Responsibility.
     */
    public BaseOrderCompletionHandler() {
        this.nextHandler = null;
    }

    @Override
    public void setNext(OrderCompletionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Does something with the current order. If everything executes correctly, it will call the next handler.
     *
     * @param order the current order to process.
     * @return the status of the order after processing.
     */
    @Override
    public abstract Order.StatusEnum handleOrderCompletion(Order order);
}
