package nl.tudelft.sem.yumyumnow.services.requests;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Getter
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
        HttpEntity<Object> httpEntity = createRequestEntity(this.body);
        if (this.parameters.isEmpty()) {
            return this.restTemplate.postForEntity(this.url, httpEntity, returnType);
        }
        return this.restTemplate.postForEntity(this.url, httpEntity, returnType, this.parameters);
    }

    /**
     * Creates a Request entity with a body in the JSON format.
     *
     * @param body the request body
     * @return the http entity containing the body
     */
    public HttpEntity<Object> createRequestEntity(Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }
}
