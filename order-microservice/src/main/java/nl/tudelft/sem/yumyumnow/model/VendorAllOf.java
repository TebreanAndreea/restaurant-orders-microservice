package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


/**
 * VendorAllOf.
 */

@JsonTypeName("Vendor_allOf")
public class VendorAllOf {

    private String restaurantName;

    private Location location;

    private OpeningTimes openingHours;

    private Integer deliveryRadius;

    @Valid
    private List<@Valid Dish> dishes;

    public VendorAllOf restaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
        return this;
    }

    /**
     * Get restaurantName.
     *
     * @return restaurantName
     */

    @Schema(name = "restaurantName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("restaurantName")
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public VendorAllOf location(Location location) {
        this.location = location;
        return this;
    }

    /**
     * Get location.
     *
     * @return location
     */
    @Valid
    @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VendorAllOf openingHours(OpeningTimes openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    /**
     * Get openingHours.
     *
     * @return openingHours
     */
    @Valid
    @Schema(name = "openingHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("openingHours")
    public OpeningTimes getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningTimes openingHours) {
        this.openingHours = openingHours;
    }

    public VendorAllOf deliveryRadius(Integer deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
        return this;
    }

    /**
     * Get deliveryRadius.
     *
     * @return deliveryRadius
     */

    @Schema(name = "deliveryRadius", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("deliveryRadius")
    public Integer getDeliveryRadius() {
        return deliveryRadius;
    }

    public void setDeliveryRadius(Integer deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }

    public VendorAllOf dishes(List<@Valid Dish> dishes) {
        this.dishes = dishes;
        return this;
    }

    /**
     * Add a dish to the list of dishes.
     *
     * @param dishesItem the dish to add
     * @return this
     */
    public VendorAllOf addDishesItem(Dish dishesItem) {
        if (this.dishes == null) {
            this.dishes = new ArrayList<>();
        }
        this.dishes.add(dishesItem);
        return this;
    }

    /**
     * Get dishes.
     *
     * @return dishes
     */
    @Valid
    @Schema(name = "dishes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("dishes")
    public List<@Valid Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<@Valid Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VendorAllOf vendorAllOf = (VendorAllOf) o;
        return Objects.equals(this.restaurantName, vendorAllOf.restaurantName)
            && Objects.equals(this.location, vendorAllOf.location)
            && Objects.equals(this.openingHours, vendorAllOf.openingHours)
            && Objects.equals(this.deliveryRadius, vendorAllOf.deliveryRadius)
            && Objects.equals(this.dishes, vendorAllOf.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName, location, openingHours, deliveryRadius, dishes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class VendorAllOf {\n");
        sb.append("    restaurantName: ").append(toIndentedString(restaurantName)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    openingHours: ").append(toIndentedString(openingHours)).append("\n");
        sb.append("    deliveryRadius: ").append(toIndentedString(deliveryRadius)).append("\n");
        sb.append("    dishes: ").append(toIndentedString(dishes)).append("\n");
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

