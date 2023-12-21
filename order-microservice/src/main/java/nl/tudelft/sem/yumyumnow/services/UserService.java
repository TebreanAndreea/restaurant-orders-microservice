package nl.tudelft.sem.yumyumnow.services;

import nl.tudelft.sem.yumyumnow.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private RestTemplate restTemplate;
    private String url = "http://localhost:8081";

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * this method return the default home address of a customer.
     *
     * @param customerId id of the customer
     * @return location
     */
    public Location getDefaultHomeAddress(Long customerId) {
        Location location = restTemplate.getForEntity(url + "/customer/location/" + customerId, Location.class).getBody();
        if (location == null) {
            return new Location();
        }
        return location;
    }
}
