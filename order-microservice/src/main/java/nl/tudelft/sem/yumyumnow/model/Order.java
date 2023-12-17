package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Order of a customer.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "price")
    private Double price;

    @Column(name = "dishes")
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Valid
    private List<@Valid Dish> dishes;

    @Column(name = "time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime time;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;

    /**
     * Gets or Sets status.
     */
    public enum StatusEnum {
        PENDING("pending"),

        ACCEPTED("accepted"),

        REJECTED("rejected"),

        PREPARING("preparing"),

        GIVEN_TO_COURIER("given to courier"),

        ON_TRANSIT("on-transit"),

        DELIVERED("delivered");

        private String value;

        StatusEnum(String value) {
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
         * Convert the given value to an enum.
         *
         * @param value value
         * @return StatusEnum
         */
        @JsonCreator
        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    @Column(name = "status")
    private StatusEnum status;

    /**
     * Empty constructor for mapper.
     */
    public Order() {
    }

    /**
     * Constructor.
     * @param customerId customerId
     * @param vendorId vendorId
     * @param price price
     * @param dishes dishes
     * @param time time
     * @param location location
     * @param rating rating
     * @param status status
     */
    public Order(Long customerId, Long vendorId, Double price, List<Dish> dishes, OffsetDateTime time,
        Location location, Rating rating, StatusEnum status) {
        this.customerId = customerId;
        this.vendorId = vendorId;
        this.price = price;
        this.dishes = dishes;
        this.time = time;
        this.location = location;
        this.rating = rating;
        this.status = status;
    }

    public Order id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id.
     *
     * @return id
     */

    @Schema(name = "id", example = "202", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    /**
     * Get customerId.
     *
     * @return customerId
     */

    @Schema(name = "customer_id", example = "201", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("customer_id")
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Order vendorId(Long vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    /**
     * Get vendorId.
     *
     * @return vendorId
     */

    @Schema(name = "vendor_id", example = "200", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("vendor_id")
    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Order price(Double price) {
        this.price = price;
        return this;
    }

    /**
     * Get price.
     *
     * @return price
     */

    @Schema(name = "price", example = "20.3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Order dishes(List<@Valid Dish> dishes) {
        this.dishes = dishes;
        return this;
    }

    /**
     * Add dishesItem.
     *
     * @param dishesItem dishesItem
     * @return this
     */
    public Order addDishesItem(Dish dishesItem) {
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

    public Order time(OffsetDateTime time) {
        this.time = time;
        return this;
    }

    /**
     * Get time.
     *
     * @return time
     */
    @Valid
    @Schema(name = "time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("time")
    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public Order location(Location location) {
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

    public Order rating(Rating rating) {
        this.rating = rating;
        return this;
    }

    /**
     * Get rating.
     *
     * @return rating
     */
    @Valid
    @Schema(name = "rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("rating")
    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Order status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Get status.
     *
     * @return status
     */

    @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("status")
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(this.id, order.id) && Objects.equals(this.customerId, order.customerId)
            && Objects.equals(this.vendorId, order.vendorId) && Objects.equals(this.price, order.price)
            && Objects.equals(this.dishes, order.dishes) && Objects.equals(this.time, order.time)
            && Objects.equals(this.location, order.location) && Objects.equals(this.rating, order.rating)
            && Objects.equals(this.status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, vendorId, price, dishes, time, location, rating, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Order {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
        sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    dishes: ").append(toIndentedString(dishes)).append("\n");
        sb.append("    time: ").append(toIndentedString(time)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

