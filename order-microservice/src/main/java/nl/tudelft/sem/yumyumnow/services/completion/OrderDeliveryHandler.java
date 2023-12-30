package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import nl.tudelft.sem.yumyumnow.services.requests.GetRequest;
import nl.tudelft.sem.yumyumnow.services.requests.PostRequest;
import org.springframework.http.ResponseEntity;

public class OrderDeliveryHandler extends OrderCompletionHandler {

    private final IntegrationService integrationService;

    /**
     * Initializes an instance of the Order Delivery Handler.
     *
     * @param integrationService the IntegrationService needed to communicate with the other microservices.
     * @param nextHandler the next Order completion handler.
     */
    public OrderDeliveryHandler(IntegrationService integrationService, OrderCompletionHandler nextHandler) {
        super(nextHandler);
        this.integrationService = integrationService;
    }

    /**
     * Sends the order to the delivery microservice.
     *
     * @param order the current order to process.
     * @return the status of the order after processing.
     */
    @Override
    public Order.StatusEnum handleOrderCompletion(Order order) {
        if (order.getStatus() == Order.StatusEnum.REJECTED) {
            return order.getStatus();
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder(order);
        String url = this.integrationService.getDeliveryMicroserviceAddress() + "/delivery";
        PostRequest deliveryRequest = new PostRequest(this.integrationService.getRestTemplate(), url,
                deliveryOrder);
        deliveryRequest.addParameter("authorizationId", order.getCustomerId().toString());

        ResponseEntity<String> response = deliveryRequest.send(String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return Order.StatusEnum.REJECTED;
        }

        String url2 = this.integrationService.getDeliveryMicroserviceAddress() + "/delivery/order/"
                + order.getOrderId() + "/status";
        GetRequest getStatusRequest = new GetRequest(this.integrationService.getRestTemplate(), url2);
        getStatusRequest.addParameter("authorizationId", order.getCustomerId().toString());

        ResponseEntity<String> statusResponse = getStatusRequest.send(String.class);
        if (!statusResponse.getStatusCode().is2xxSuccessful()) {
            return order.getStatus();
        }
        Order.StatusEnum newStatus = new DeliveryStatus(statusResponse.getBody()).getDefaultStatus();
        order.setStatus(newStatus);
        if (this.nextHandler != null) {
            return this.nextHandler.handleOrderCompletion(order);
        }
        return newStatus;
    }
}
