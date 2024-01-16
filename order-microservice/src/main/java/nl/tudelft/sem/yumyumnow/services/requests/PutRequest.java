package nl.tudelft.sem.yumyumnow.services.requests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PutRequest extends Request {

    private final Object body;

    /**
     * Creates a new PUT Request object.
     *
     * @param restTemplate the REST template object
     * @param url          the url at which the request is made
     * @param body         the body of the request
     */
    public PutRequest(RestTemplate restTemplate, String url, Object body) {
        super(restTemplate, url);
        this.body = body;
    }

    /**
     * Sends a PUT request and waits for the response.
     *
     * @param returnType the type of the object returned
     * @param <T>        the type of the object to return
     * @return a ResponseEntity containing the HTTP status code and the object
     */
    @Override
    public <T> ResponseEntity<T> send(Class<T> returnType) {
        if (this.parameters.isEmpty()) {
            return this.restTemplate.exchange(this.url, HttpMethod.PUT, new HttpEntity<>(this.body), returnType);
        }
        return this.restTemplate.exchange(this.url, HttpMethod.PUT, new HttpEntity<>(this.body),
            returnType, this.parameters);
    }
}
