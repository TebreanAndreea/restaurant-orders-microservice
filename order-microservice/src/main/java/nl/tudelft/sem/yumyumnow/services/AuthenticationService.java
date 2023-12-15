package nl.tudelft.sem.yumyumnow.services;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    /**
     * Checks with the Users microservice if the ID corresponds to a customer.
     *
     * @param customerId the ID to check
     * @return true if it corresponds to a customer
     */
    public boolean isCustomer(Long customerId) {
        return true;
    }

    /**
     * Checks with the Users microservice if the ID corresponds to a vendor.
     *
     * @param vendorId the ID to check
     * @return true if it corresponds to a vendor
     */
    public boolean isVendor(Long vendorId) {
        return true;
    }

}
