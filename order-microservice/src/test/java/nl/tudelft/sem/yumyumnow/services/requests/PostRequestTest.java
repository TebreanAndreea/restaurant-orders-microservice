package nl.tudelft.sem.yumyumnow.services.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PostRequestTest {

    private RestTemplate template;

    @BeforeEach
    public void setup() {
        this.template = Mockito.mock(RestTemplate.class);
    }


    @Test
    public void testEmptyBody() {
        PostRequest request = new PostRequest(template, "", null);
        assertEquals("", request.getBody());
    }

    @Test
    public void testHttpEntity() {
        PostRequest request = new PostRequest(template, "", "Pizza");
        HttpEntity<Object> entity = request.createRequestEntity("Pizza");
        assertEquals("Pizza", entity.getBody());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
    }

    @Test
    public void testEmptyParametersList() {
        PostRequest request = new PostRequest(template, "http://localhost:80", "Hello");

        Mockito.when(template.postForEntity("http://localhost:80", request.createRequestEntity("Hello"),
                        String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
        ResponseEntity<String> response = request.send(String.class);
        Mockito.verify(template, Mockito.times(1))
                .postForEntity("http://localhost:80", request.createRequestEntity("Hello"), String.class);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
    }
}
