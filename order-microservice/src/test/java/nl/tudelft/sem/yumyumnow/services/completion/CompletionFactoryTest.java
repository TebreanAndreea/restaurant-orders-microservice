package nl.tudelft.sem.yumyumnow.services.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import org.junit.jupiter.api.Test;

public class CompletionFactoryTest {

    @Test
    public void testCompletionFactory() {
        CompletionFactory factory = new CompletionFactory();
        IntegrationService integration = new IntegrationService();
        BaseOrderCompletionHandler first = (BaseOrderCompletionHandler) factory
                .createCompletionResponsibilityChain(integration);

        assertNotNull(first.nextHandler);
        assertNotNull(((BaseOrderCompletionHandler) first.nextHandler).nextHandler);
        assertNull(((BaseOrderCompletionHandler) ((BaseOrderCompletionHandler) first.nextHandler)
                .nextHandler).nextHandler);

        assertEquals(OrderPaymentHandler.class, first.getClass());
        assertEquals(OrderEmailHandler.class, first.nextHandler.getClass());
        assertEquals(OrderDeliveryHandler.class, ((BaseOrderCompletionHandler) first.nextHandler)
                .nextHandler.getClass());
    }
}
