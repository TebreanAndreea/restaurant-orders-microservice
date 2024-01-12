package nl.tudelft.sem.yumyumnow.services.completion;

import nl.tudelft.sem.yumyumnow.model.Order;

public class DeliveryStatus {

    public final String status;

    /**
     * Creates a delivery status object with a string value.
     *
     * @param status the string value of the status, in a valid format for the delivery microservice
     */
    public DeliveryStatus(String status) {
        this.status = status == null ? "" : status;
    }


    /**
     * Creates a delivery status object by converting a status that is in a valid format for us.
     *
     * @param status the Enum value of the status, in a valid format for us.
     */
    public DeliveryStatus(Order.StatusEnum status) {
        if (status == null) {
            this.status = "";
            return;
        }
        String temp = status.getValue();
        int length = temp.length();
        for (int i = 0; i < length; i++) {
            String character = temp.substring(i, i + 1);
            if (i == 0 || temp.charAt(i - 1) == ' ' || temp.charAt(i - 1) == '-') {
                temp = temp.substring(0, i)
                        + character.toUpperCase()
                        + temp.substring(i + 1, length);
            }
        }
        this.status = temp.replace(' ', '_').replace('-', '_');
    }

    /**
     * Getter for the delivery status.
     *
     * @return the order status in a valid format for the delivery microservice.
     */
    public String getDeliveryStatus() {
        return this.status;
    }

    /**
     * Converts the delivery status object into a status object valid in our microservice.
     *
     * @return the order status in a valid format for us.
     */
    public Order.StatusEnum getDefaultStatus() {
        if (this.status.isEmpty()) {
            return Order.StatusEnum.REJECTED;
        }
        try {
            boolean isOnTransit = this.status.equalsIgnoreCase("On_Transit");
            return isOnTransit ? Order.StatusEnum.ON_TRANSIT :
                    Order.StatusEnum.fromValue(this.status.toLowerCase().replace('_', ' '));
        } catch (IllegalArgumentException e) {
            return Order.StatusEnum.REJECTED;
        }
    }


}
