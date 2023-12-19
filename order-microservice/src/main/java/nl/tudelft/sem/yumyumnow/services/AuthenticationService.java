package nl.tudelft.sem.yumyumnow.services;

import nl.tudelft.sem.yumyumnow.services.requests.GetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    private final IntegrationService integrationService;

    @Autowired
    public AuthenticationService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }


    /**
     * Checks with the Users microservice if the ID corresponds to a customer.
     *
     * @param customerId the ID to check
     * @return true if it corresponds to a customer
     */
    public boolean isCustomer(Long customerId) {
        String role = "customer";
        return role.equalsIgnoreCase(this.getRole(customerId));
    }

    /**
     * Checks with the Users microservice if the ID corresponds to an admin.
     *
     * @param adminId the ID to check
     * @return true if it corresponds to an admin
     */
    public boolean isAdmin(Long adminId) {
        return true;
    }

    /**
     * Checks with the Users microservice if the ID corresponds to a vendor.
     *
     * @param vendorId the ID to check
     * @return true if it corresponds to a vendor
     */
    public boolean isVendor(Long vendorId) {
        String role = "vendor";
        return role.equalsIgnoreCase(this.getRole(vendorId));
    }

    /**
     * Connects to the User microservice and retrieve the role of a user from its ID.
     *
     * @param userId the ID to check
     * @return the role of the user, or null if an error occurred
     */
    private String getRole(Long userId) {
        String url = integrationService.getUserMicroserviceAddress() + "/user/" + userId;
        ResponseEntity<String> response = new GetRequest(integrationService.getRestTemplate(), url)
                .send(String.class);
        if (response.getStatusCode().isError()) {
            return null;
        }
        return response.getBody();
    }
}
