package nl.tudelft.sem.yumyumnow.services.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PutRequestTest {

    private RestTemplate template;

    @BeforeEach
    public void setup() {
        this.template = Mockito.mock(RestTemplate.class);
    }

    @Test
    public void testWithParametersList() {
        PutRequest request = new PutRequest(template, "http://localhost:80", "Burger");
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("Pizza", "Margarita");

        Mockito.when(template.exchange("http://localhost:80",
                        HttpMethod.PUT, new HttpEntity<>("Burger"),
                        String.class, parameters))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
        request.addParameter("Pizza", "Margarita");
        ResponseEntity<String> response = request.send(String.class);
        Mockito.verify(template, Mockito.times(1))
                .exchange("http://localhost:80",
                        HttpMethod.PUT, new HttpEntity<>("Burger"), String.class, parameters);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
    }
}
