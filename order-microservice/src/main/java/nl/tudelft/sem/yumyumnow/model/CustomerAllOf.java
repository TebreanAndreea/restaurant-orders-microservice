package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * CustomerAllOf.
 */

@JsonTypeName("Customer_allOf")
public class CustomerAllOf {

    private Location homeAddress;

    /**
     * Gets or Sets paymentMethod.
     */
    public enum PaymentMethodEnum {
        CASH("cash"),

        VISA("Visa"),

        MAESTRO("Maestro"),

        APPLEWALLET("appleWallet");

        private String value;

        PaymentMethodEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        /**
         * Convert to enum from value.
         *
         * @param value value
         * @return enum
         */
        @JsonCreator
        public static PaymentMethodEnum fromValue(String value) {
            for (PaymentMethodEnum b : PaymentMethodEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private PaymentMethodEnum paymentMethod;

    @Valid
    private List<@Valid Dish> favouriteFoods;

    @Valid
    private List<@Valid Vendor> favouriteRestaurants;

    @Valid
    private List<@Valid Order> savedOrders;

    @Valid
    private List<@Valid Order> pastOrders;

    @Valid
    private List<String> allergens;

    public CustomerAllOf homeAddress(Location homeAddress) {
        this.homeAddress = homeAddress;
        return this;
    }

    /**
     * Get homeAddress.
     *
     * @return homeAddress
     */
    @Valid
    @Schema(name = "home_address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("home_address")
    public Location getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Location homeAddress) {
        this.homeAddress = homeAddress;
    }

    public CustomerAllOf paymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Get paymentMethod.
     *
     * @return paymentMethod
     */

    @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("paymentMethod")
    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CustomerAllOf favouriteFoods(List<@Valid Dish> favouriteFoods) {
        this.favouriteFoods = favouriteFoods;
        return this;
    }

    /**
     * Add favouriteFoodsItem.
     *
     * @param favouriteFoodsItem favouriteFoodsItem
     * @return this
     */
    public CustomerAllOf addFavouriteFoodsItem(Dish favouriteFoodsItem) {
        if (this.favouriteFoods == null) {
            this.favouriteFoods = new ArrayList<>();
        }
        this.favouriteFoods.add(favouriteFoodsItem);
        return this;
    }

    /**
     * Get favouriteFoods.
     *
     * @return favouriteFoods
     */
    @Valid
    @Schema(name = "favouriteFoods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favouriteFoods")
    public List<@Valid Dish> getFavouriteFoods() {
        return favouriteFoods;
    }

    public void setFavouriteFoods(List<@Valid Dish> favouriteFoods) {
        this.favouriteFoods = favouriteFoods;
    }

    public CustomerAllOf favouriteRestaurants(List<@Valid Vendor> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
        return this;
    }

    /**
     * Add favouriteRestaurantsItem.
     *
     * @param favouriteRestaurantsItem favouriteRestaurantsItem
     * @return this
     */
    public CustomerAllOf addFavouriteRestaurantsItem(Vendor favouriteRestaurantsItem) {
        if (this.favouriteRestaurants == null) {
            this.favouriteRestaurants = new ArrayList<>();
        }
        this.favouriteRestaurants.add(favouriteRestaurantsItem);
        return this;
    }

    /**
     * Get favouriteRestaurants.
     *
     * @return favouriteRestaurants
     */
    @Valid
    @Schema(name = "favouriteRestaurants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favouriteRestaurants")
    public List<@Valid Vendor> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public void setFavouriteRestaurants(List<@Valid Vendor> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
    }

    public CustomerAllOf savedOrders(List<@Valid Order> savedOrders) {
        this.savedOrders = savedOrders;
        return this;
    }

    /**
     * Add savedOrdersItem.
     *
     * @param savedOrdersItem savedOrdersItem
     * @return this
     */
    public CustomerAllOf addSavedOrdersItem(Order savedOrdersItem) {
        if (this.savedOrders == null) {
            this.savedOrders = new ArrayList<>();
        }
        this.savedOrders.add(savedOrdersItem);
        return this;
    }

    /**
     * Get savedOrders.
     *
     * @return savedOrders
     */
    @Valid
    @Schema(name = "savedOrders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("savedOrders")
    public List<@Valid Order> getSavedOrders() {
        return savedOrders;
    }

    public void setSavedOrders(List<@Valid Order> savedOrders) {
        this.savedOrders = savedOrders;
    }

    public CustomerAllOf pastOrders(List<@Valid Order> pastOrders) {
        this.pastOrders = pastOrders;
        return this;
    }

    /**
     * Add pastOrdersItem.
     *
     * @param pastOrdersItem the past order item
     * @return this
     */
    public CustomerAllOf addPastOrdersItem(Order pastOrdersItem) {
        if (this.pastOrders == null) {
            this.pastOrders = new ArrayList<>();
        }
        this.pastOrders.add(pastOrdersItem);
        return this;
    }

    /**
     * Get pastOrders.
     *
     * @return pastOrders
     */
    @Valid
    @Schema(name = "pastOrders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pastOrders")
    public List<@Valid Order> getPastOrders() {
        return pastOrders;
    }

    public void setPastOrders(List<@Valid Order> pastOrders) {
        this.pastOrders = pastOrders;
    }

    public CustomerAllOf allergens(List<String> allergens) {
        this.allergens = allergens;
        return this;
    }

    /**
     * Add allergensItem.
     *
     * @param allergensItem allergensItem
     * @return this
     */
    public CustomerAllOf addAllergensItem(String allergensItem) {
        if (this.allergens == null) {
            this.allergens = new ArrayList<>();
        }
        this.allergens.add(allergensItem);
        return this;
    }

    /**
     * Get allergens.
     *
     * @return allergens
     */

    @Schema(name = "allergens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("allergens")
    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerAllOf customerAllOf = (CustomerAllOf) o;
        return Objects.equals(this.homeAddress, customerAllOf.homeAddress) && Objects.equals(this.paymentMethod,
            customerAllOf.paymentMethod) && Objects.equals(this.favouriteFoods, customerAllOf.favouriteFoods)
            && Objects.equals(this.favouriteRestaurants, customerAllOf.favouriteRestaurants)
            && Objects.equals(this.savedOrders, customerAllOf.savedOrders)
            && Objects.equals(this.pastOrders, customerAllOf.pastOrders)
            && Objects.equals(this.allergens, customerAllOf.allergens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeAddress, paymentMethod, favouriteFoods, favouriteRestaurants, savedOrders, pastOrders,
            allergens);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CustomerAllOf {\n");
        sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
        sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
        sb.append("    favouriteFoods: ").append(toIndentedString(favouriteFoods)).append("\n");
        sb.append("    favouriteRestaurants: ").append(toIndentedString(favouriteRestaurants)).append("\n");
        sb.append("    savedOrders: ").append(toIndentedString(savedOrders)).append("\n");
        sb.append("    pastOrders: ").append(toIndentedString(pastOrders)).append("\n");
        sb.append("    allergens: ").append(toIndentedString(allergens)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

