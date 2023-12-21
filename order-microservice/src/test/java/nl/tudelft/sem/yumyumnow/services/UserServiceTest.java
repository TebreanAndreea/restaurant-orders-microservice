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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class UserServiceTest {
    private RestTemplate restTemplate;
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.restTemplate = mock(RestTemplate.class);
        userService = new UserService(restTemplate);
    }

    @Test
    void getDefaultHomeAddressTest() {
        Location location = new Location();
        location.setLatitude(123.56);
        location.setLongitude(345.78);
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/123", Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = userService.getDefaultHomeAddress((long) 123);

        assertEquals(location, result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/123", Location.class);
    }

    @Test
    void noAddressTest() {
        Location location = new Location();
        when(restTemplate.getForEntity("http://localhost:8081/customer/location/124", Location.class))
            .thenReturn(new ResponseEntity<>(location, HttpStatus.OK));

        Location result = userService.getDefaultHomeAddress((long) 124);

        assertEquals(location, result);
        verify(restTemplate, times(1)).getForEntity("http://localhost:8081/customer/location/124", Location.class);
    }
}
