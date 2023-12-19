package nl.tudelft.sem.yumyumnow.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Getter
@Service
public class IntegrationService {

    private final String deliveryMicroserviceAddress;

    private final String userMicroserviceAddress;

    private final RestTemplate restTemplate;


    /**
     * Creates a default IntegrationService with the addresses provided by the other teams.
     */
    public IntegrationService() {
        this.restTemplate = new RestTemplate();
        this.deliveryMicroserviceAddress = "http://localhost:8080";
        this.userMicroserviceAddress = "http://localhost:8081";
    }

    /**
     * Creates a custom IntegrationService with the given microservices addresses.
     *
     * @param deliveryMicroserviceAddress the server address of the Delivery microservice
     * @param userMicroserviceAddress the server address of the User microservice
     * @param restTemplate the REST template that can be used to make REST requests to the other microservices.
     */
    public IntegrationService(String deliveryMicroserviceAddress, String userMicroserviceAddress,
                              RestTemplate restTemplate) {
        this.deliveryMicroserviceAddress = deliveryMicroserviceAddress;
        this.userMicroserviceAddress = userMicroserviceAddress;
        this.restTemplate = restTemplate;
    }
}
