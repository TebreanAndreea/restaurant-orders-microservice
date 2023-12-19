package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Vendor of YumYUmNow.
 */

public class Vendor {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String restaurantName;

    private Location location;

    private OpeningTimes openingHours;

    private Integer deliveryRadius;

    @Valid
    private List<@Valid Dish> dishes;

    /**
     * Empty constructor.
     */
    public Vendor() {
    }

    /**
     * Constructor.
     *
     * @param id id of vendor
     * @param name name of vendor
     * @param surname surname of vendor
     * @param email email of vendor
     * @param restaurantName restaurant name of vendor
     * @param location location of vendor
     * @param openingHours opening hours of vendor
     * @param deliveryRadius delivery radius of vendor
     * @param dishes dishes of vendor
     */
    public Vendor(Long id, String name, String surname, String email, String restaurantName,
        Location location, OpeningTimes openingHours, Integer deliveryRadius,
        List<Dish> dishes) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.restaurantName = restaurantName;
        this.location = location;
        this.openingHours = openingHours;
        this.deliveryRadius = deliveryRadius;
        this.dishes = dishes;
    }

    public Vendor id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id.
     *
     * @return id
     */

    @Schema(name = "id", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vendor name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name.
     *
     * @return name
     */

    @Schema(name = "name", example = "Peter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vendor surname(String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * Get surname.
     *
     * @return surname
     */

    @Schema(name = "surname", example = "Peterity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Vendor email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email.
     *
     * @return email
     */

    @Schema(name = "email", example = "peter@gmail.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Vendor restaurantName(String restaurantName) {
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

    public Vendor location(Location location) {
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

    public Vendor openingHours(OpeningTimes openingHours) {
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

    public Vendor deliveryRadius(Integer deliveryRadius) {
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

    public Vendor dishes(List<@Valid Dish> dishes) {
        this.dishes = dishes;
        return this;
    }

    /**
     * Add dishesItem.
     *
     * @param dishesItem dishesItem
     * @return this
     */
    public Vendor addDishesItem(Dish dishesItem) {
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
        Vendor vendor = (Vendor) o;
        return Objects.equals(this.id, vendor.id) && Objects.equals(this.name, vendor.name)
            && Objects.equals(this.surname, vendor.surname) && Objects.equals(this.email, vendor.email)
            && Objects.equals(this.restaurantName, vendor.restaurantName) && Objects.equals(this.location, vendor.location)
            && Objects.equals(this.openingHours, vendor.openingHours)
            && Objects.equals(this.deliveryRadius, vendor.deliveryRadius) && Objects.equals(this.dishes, vendor.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, restaurantName, location, openingHours, deliveryRadius, dishes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Vendor {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

