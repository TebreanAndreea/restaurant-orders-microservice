package nl.tudelft.sem.yumyumnow.services.requests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PostRequest extends Request {

    private final Object body;

    /**
     * Creates a new GET Request object.
     *
     * @param restTemplate the REST template object
     * @param url          the url at which the request is made
     * @param body the object that will be the body of the request, in JSON format.
     */
    public PostRequest(RestTemplate restTemplate, String url, Object body) {
        super(restTemplate, url);
        this.body = body != null ? body : "";
    }

    /**
     * Sends a POST request and waits for the response.
     *
     * @param returnType the type of the object returned
     * @param <T>        the type of the object to return
     * @return a ResponseEntity containing the HTTP status code and the object
     */
    @Override
    public <T> ResponseEntity<T> send(Class<T> returnType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(this.body, httpHeaders);
        if (this.parameters.isEmpty()) {
            return this.restTemplate.postForEntity(this.url, httpEntity, returnType);
        }
        return this.restTemplate.postForEntity(this.url, httpEntity, returnType, this.parameters);
    }
}
