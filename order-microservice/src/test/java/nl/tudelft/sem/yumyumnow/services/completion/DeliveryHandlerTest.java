package nl.tudelft.sem.yumyumnow.services.completion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class DeliveryHandlerTest {

    private OrderDeliveryHandler deliveryHandler;
    private RestTemplate restTemplate;
    private IntegrationService integrationService;
    private final BaseOrderCompletionHandler stubHandler = new BaseOrderCompletionHandler() {
        @Override
        public Order.StatusEnum handleOrderCompletion(Order order) {
            return order.getStatus() == Order.StatusEnum.ON_TRANSIT ? Order.StatusEnum.DELIVERED :
                    Order.StatusEnum.REJECTED;
        }
    };

    /**
     * Setup of the mock objects.
     */
    @BeforeEach
    public void setup() {
        this.restTemplate = Mockito.mock(RestTemplate.class);
        this.integrationService = new IntegrationService("http://localhost:8080",
                "http://localhost:8081", restTemplate);
        this.deliveryHandler = new OrderDeliveryHandler(integrationService, null);
    }

    @Test
    public void testInvalidStatus() {
        Order order = new Order().status(Order.StatusEnum.REJECTED);
        assertEquals(Order.StatusEnum.REJECTED, this.deliveryHandler.handleOrderCompletion(order));
    }

    @Test
    @SuppressWarnings("all")
    public void testPostRequestFails() {
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(10L).price(125.0D).orderId(1L).vendorId(12L);
        Mockito.when(this.restTemplate.postForEntity(Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.notFound().build());
        assertEquals(Order.StatusEnum.REJECTED, this.deliveryHandler.handleOrderCompletion(order));
    }

    @Test
    @SuppressWarnings("all")
    public void testGetRequestFails() {
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(10L).price(125.0D).orderId(1L).vendorId(12L);
        Mockito.when(this.restTemplate.postForEntity(Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(this.restTemplate.getForEntity(Mockito.any(), Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.status(401).build());
        assertEquals(Order.StatusEnum.PENDING, this.deliveryHandler.handleOrderCompletion(order));
    }

    @Test
    @SuppressWarnings("all")
    public void testGetRequestPasses() {
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(10L).price(125.0D).orderId(1L).vendorId(12L);
        Mockito.when(this.restTemplate.postForEntity(Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(this.restTemplate.getForEntity(Mockito.any(), Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.ok("On_Transit"));
        assertEquals(Order.StatusEnum.ON_TRANSIT, this.deliveryHandler.handleOrderCompletion(order));
    }

    @Test
    @SuppressWarnings("all")
    public void testCallsNextHandler() {
        this.deliveryHandler = new OrderDeliveryHandler(integrationService, stubHandler);
        Order order = new Order().status(Order.StatusEnum.PENDING).customerId(10L).price(125.0D).orderId(1L).vendorId(12L);
        Mockito.when(this.restTemplate.postForEntity(Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(this.restTemplate.getForEntity(Mockito.any(), Mockito.any(), Mockito.any(MultiValueMap.class)))
                .thenReturn(ResponseEntity.ok("On_Transit"));
        assertEquals(Order.StatusEnum.DELIVERED, this.deliveryHandler.handleOrderCompletion(order));
    }


}
