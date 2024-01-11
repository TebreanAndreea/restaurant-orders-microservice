package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import org.springframework.stereotype.Service;

@Service
public class CompletionFactory {

    /**
     * Creates the default Chain of Responsibility handlers for an order completion.
     *
     * @param service the IntegrationService that might be required by the handlers.
     * @return the handler at the start of the Order Completion chain of handlers.
     */
    public OrderCompletionHandler createCompletionResponsibilityChain(IntegrationService service) {
        OrderCompletionHandler deliveryHandler = new OrderDeliveryHandler(service, null);
        OrderCompletionHandler emailHandler = new OrderEmailHandler(deliveryHandler);
        return new OrderPaymentHandler(emailHandler);
    }
}
