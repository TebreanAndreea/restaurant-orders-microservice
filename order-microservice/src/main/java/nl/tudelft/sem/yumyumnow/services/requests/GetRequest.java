package nl.tudelft.sem.yumyumnow.services.requests;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetRequest extends Request {

    /**
     * Creates a new GET Request object.
     *
     * @param restTemplate the REST template object
     * @param url          the url at which the request is made
     */
    public GetRequest(RestTemplate restTemplate, String url) {
        super(restTemplate, url);
    }

    /**
     * Sends a GET request and waits for the response.
     *
     * @param returnType the type of the object returned
     * @param <T>        the type of the object to return
     * @return a ResponseEntity containing the HTTP status code and the object
     */
    @Override
    public <T> ResponseEntity<T> send(Class<T> returnType) {
        if (this.parameters.isEmpty()) {
            return this.restTemplate.getForEntity(this.url, returnType);
        }
        return this.restTemplate.getForEntity(this.url, returnType, this.parameters);
    }
}
