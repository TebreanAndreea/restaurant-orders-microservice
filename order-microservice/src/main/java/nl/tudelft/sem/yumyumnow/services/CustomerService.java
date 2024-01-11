package nl.tudelft.sem.yumyumnow.services;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.services.requests.PutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {
    private String url = "http://localhost:8081";

    private final IntegrationService integrationService;

    @Autowired
    public CustomerService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }


    /**
     * Geting a customer based of its id from the user microservice.
     *
     * @param customerId The id of the customer we want to receive.
     * @return The customer with the corresponding id.
     */
    public Customer getCustomer(Long customerId) {
        ResponseEntity<Customer> response = integrationService.getRestTemplate()
            .getForEntity(url + "/customer/"
            + customerId, Customer.class);
        if (response.getStatusCode().isError()) {
            return null;
        }
        return response.getBody();
    }
    /**
     * this method return the default home address of a customer.
     *
     * @param customerId id of the customer
     * @return location
     */

    public Location getDefaultHomeAddress(Long customerId) {
        ResponseEntity<Location> response = integrationService.getRestTemplate().getForEntity(url + "/customer/location/"
            + customerId, Location.class);
        if (response.getStatusCode().isError()) {
            return null;
        }
        return response.getBody();
    }


    /**
     * this method set the default home address of a customer.
     *
     * @param customerId id of the customer
     * @param location location of the customer
     * @return location
     */
    public Location setDefaultHomeAddress(Long customerId, Location location) {
        if (location == null || customerId < 0 || location.getLatitude() < -90 || location.getLatitude() > 90
            || location.getLongitude() < -180 || location.getLongitude() > 180) {
            return null;
        }

        String url = integrationService.getUserMicroserviceAddress() + "/customer/homeAddress/" + customerId;
        ResponseEntity<Location> response = new PutRequest(integrationService.getRestTemplate(),
            url, location).send(Location.class);
        if (response.getStatusCode().isError()) {
            return null;
        }
        return response.getBody();
    }
}
