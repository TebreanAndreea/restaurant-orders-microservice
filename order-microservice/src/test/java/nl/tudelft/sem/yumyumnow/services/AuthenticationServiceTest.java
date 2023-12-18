package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private RestTemplate restTemplate;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.restTemplate = Mockito.mock(RestTemplate.class);
        IntegrationService integrationService = new IntegrationService("http://localhost:8080",
                "http://localhost:8081", restTemplate);
        this.authenticationService = new AuthenticationService(integrationService);
    }

    @Test
    public void testIsCustomer() {
        Mockito.when(restTemplate.getForEntity("http://localhost:8081/user/114", String.class))
                .thenReturn(ResponseEntity.ok("customer"));

        assertTrue(this.authenticationService.isCustomer(114L));
        assertFalse(this.authenticationService.isVendor(114L));
    }

    @Test
    public void testIsVendor() {
        Mockito.when(restTemplate.getForEntity("http://localhost:8081/user/45", String.class))
                .thenReturn(ResponseEntity.ok("vendor"));

        assertTrue(this.authenticationService.isVendor(45L));
        assertFalse(this.authenticationService.isCustomer(45L));
    }

    @Test
    public void testIsOther() {
        Mockito.when(restTemplate.getForEntity("http://localhost:8081/user/11111", String.class))
                .thenReturn(ResponseEntity.ok("courier"));

        assertFalse(this.authenticationService.isCustomer(11111L));
        assertFalse(this.authenticationService.isVendor(11111L));
    }

    @Test
    public void testIsNone() {
        Mockito.when(restTemplate.getForEntity("http://localhost:8081/user/20", String.class))
                .thenReturn(ResponseEntity.status(404).build());

        assertFalse(this.authenticationService.isCustomer(20L));
        assertFalse(this.authenticationService.isVendor(20L));
    }
}
