package nl.tudelft.sem.yumyumnow.services.requests;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public abstract class Request {


    protected final RestTemplate restTemplate;
    protected final MultiValueMap<String, String> parameters;
    protected final String url;

    /**
     * Creates a new Request object.
     *
     * @param restTemplate the REST template object
     * @param url          the url at which the request is made
     */
    public Request(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.parameters = new LinkedMultiValueMap<>();
        this.url = url;
    }

    /**
     * Adds a parameter to the request.
     *
     * @param name      the name of the parameter
     * @param parameter the value of the parameter
     */
    public void addParameter(String name, String parameter) {
        this.parameters.add(name, parameter);
    }

    /**
     * Sends a request and waits for the response. Subclasses will specify what type of request should be sent.
     *
     * @param returnType the type of the object returned
     * @param <T>        the type of the object to return
     * @return a ResponseEntity containing the HTTP status code and the object
     */
    public abstract <T> ResponseEntity<T> send(Class<T> returnType);
}
