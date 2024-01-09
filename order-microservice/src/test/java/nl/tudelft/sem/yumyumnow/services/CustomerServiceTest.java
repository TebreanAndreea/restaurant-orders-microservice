package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.yumyumnow.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class CustomerServiceTest {
    private RestTemplate restTemplate;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        this.restTemplate = mock(RestTemplate.class);
        IntegrationService integrationService = new IntegrationService("http://localhost:8080",
            "http://localhost:8081", restTemplate);
        customerService = new CustomerService(integrationService);
    }

    @Test
    void getDefaultHomeAddressTest() {
        Location location = new Location();
        location.setLatitude(83.56);
        location.setLongitude(145.78);
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/123", Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.getDefaultHomeAddress((long) 123);

        assertEquals(location, result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/123", Location.class);
    }

    @Test
    void noAddressTest() {
        Location location = new Location();
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/124", Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.getDefaultHomeAddress((long) 124);

        assertEquals(location, result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/124", Location.class);
    }

    @Test
    void setDefaultHomeAddressTest() {
        Location location = new Location();
        location.setLatitude(83.56);
        location.setLongitude(145.78);
        when(restTemplate.exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.setDefaultHomeAddress((long) 123, location);

        assertEquals(location, result);
        verify(restTemplate, times(1)).exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class);
    }

    @Test
    void setDefaultHomeAddressNullTest() {
        Location location = new Location();
        location.setLatitude(83.56);
        location.setLongitude(145.78);
        when(restTemplate.exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.setDefaultHomeAddress((long) 123, null);

        assertNull(result);
        verify(restTemplate, times(0)).exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class);
    }

    @Test
    void setDefaultHomeAddressInvalidLocation() {
        Location location = new Location();
        location.setLatitude(83.56);
        location.setLongitude(345.78);
        when(restTemplate.exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.setDefaultHomeAddress((long) 123, null);

        assertNull(result);
        verify(restTemplate, times(0)).exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class);
    }
}
