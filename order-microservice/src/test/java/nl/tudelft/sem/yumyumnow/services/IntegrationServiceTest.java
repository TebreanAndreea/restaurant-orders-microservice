package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class IntegrationServiceTest {


    @Test
    public void testDefaultService() {
        IntegrationService service = new IntegrationService();
        String deliveryMicroservice = "http://localhost:8080";
        assertEquals(deliveryMicroservice, service.getDeliveryMicroserviceAddress());
        String userMicroservice = "http://localhost:8081";
        assertEquals(userMicroservice, service.getUserMicroserviceAddress());
    }

    @Test
    public void testCustomService() {
        IntegrationService service = new IntegrationService("https://www.google.com:1111",
                "https://www.minecraft.net:448",
                new RestTemplate());
        String deliveryMicroservice = "https://www.google.com:1111";
        assertEquals(deliveryMicroservice, service.getDeliveryMicroserviceAddress());
        String userMicroservice = "https://www.minecraft.net:448";
        assertEquals(userMicroservice, service.getUserMicroserviceAddress());
    }
}
