package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    void getCustomerTest() {
        Customer customer = new Customer();
        customer.setId((long) 123);
        when(restTemplate.getForEntity("http://localhost:8081/customer/123", Customer.class))
            .thenReturn(new ResponseEntity<>(customer, HttpStatus.OK));

        Customer result = customerService.getCustomer((long) 123);
        assertEquals(customer, result);
        assertEquals(result.getId(), (long) 123);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/123", Customer.class);
    }

    @Test
    void getCustomerTestNoCustomer() {
        when(restTemplate.getForEntity("http://localhost:8081/customer/123", Customer.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Customer result = customerService.getCustomer((long) 123);
        assertNull(result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/123", Customer.class);

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
    void emptyAddressTest() {
        Location location = new Location();
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/124", Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = customerService.getDefaultHomeAddress((long) 124);

        assertEquals(location, result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/124", Location.class);
    }

    @Test
    void noAddressTest() {
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/124", Location.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Location result = customerService.getDefaultHomeAddress((long) 124);

        assertNull(result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/124", Location.class);

    }

    @ParameterizedTest
    @MethodSource("validLocationProvider")
    void setDefaultHomeAddressTest(Location location) {
        when(restTemplate.exchange("http://localhost:8081/customer/homeAddress/0", HttpMethod.PUT, new HttpEntity<>(location), Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));
        assertNotNull(location);

        Location result = customerService.setDefaultHomeAddress((long) 0, location);

        assertEquals(location, result);
        verify(restTemplate, times(1)).exchange("http://localhost:8081/customer/homeAddress/0", HttpMethod.PUT, new HttpEntity<>(location), Location.class);
    }

    public static Stream<Arguments> validLocationProvider() {
        return Stream.of(
            Arguments.of(new Location(1L, 83.56, 145.78)),
            Arguments.of(new Location(1L, 90D, 90D)),
            Arguments.of(new Location(1L, -90D, 90D)),
            Arguments.of(new Location(1L, 90D, -180D)),
            Arguments.of(new Location(1L, -9D, 180D))
        );
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
            .thenReturn(new ResponseEntity<>(location, HttpStatus.INTERNAL_SERVER_ERROR));

        Location result = customerService.setDefaultHomeAddress((long) 123, location);

        assertNull(result);
        verify(restTemplate, times(0)).exchange("http://localhost:8081/customer/homeAddress/123", HttpMethod.PUT, new HttpEntity<>(location), Location.class);
    }

    @ParameterizedTest
    @MethodSource("invalidLocationProvider")
    void setDefaultLocationInvalid(@Nullable Location location, Long customerId) {
        Location result = customerService.setDefaultHomeAddress(customerId, location);
        assertNull(result);
    }

    public static Stream<Arguments> invalidLocationProvider() {
        return Stream.of(
            Arguments.of(null, (long) 123),
            Arguments.of(new Location(), (long) -123),
            Arguments.of(new Location(), (long) -1),
            Arguments.of(new Location(1L, 91D, 91D), (long) 123),
            Arguments.of(new Location(1L, -91D, 91D), (long) 123),
            Arguments.of(new Location(1L, 90D, 181D), (long) 123),
            Arguments.of(new Location(1L, 9D, -181D), (long) 123)
        );
    }
}
