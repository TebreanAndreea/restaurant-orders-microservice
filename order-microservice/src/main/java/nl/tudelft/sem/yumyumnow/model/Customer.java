package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 * Customer of YumYUmNow.
 */

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
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
         * @param value the value
         * @return the enum
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

    @Column(name = "payment_method")
    private PaymentMethodEnum paymentMethod;

    @Column(name = "favourite_foods")
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Valid
    private List<@Valid Dish> favouriteFoods;

    @Column(name = "favourite_restaurants")
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Valid
    private List<@Valid Vendor> favouriteRestaurants;

    @Column(name = "saved_orders")
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Valid
    private List<@Valid Order> savedOrders;

    @Column(name = "past_orders")
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Valid
    private List<@Valid Order> pastOrders;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "allergens")
    @Valid
    private List<String> allergens;

    /**
     * Empty constructor for mapper.
     */
    public Customer() {
    }

    /**
     * Constructor for Customer.
     *
     * @param name name
     * @param surname surname
     * @param email email
     * @param homeAddress homeAddress
     * @param paymentMethod paymentMethod
     * @param favouriteFoods favouriteFoods
     * @param favouriteRestaurants favouriteRestaurants
     * @param savedOrders savedOrders
     * @param pastOrders pastOrders
     * @param allergens allergens
     */
    public Customer(String name, String surname, String email, Location homeAddress,
        PaymentMethodEnum paymentMethod, List<Dish> favouriteFoods, List<Vendor> favouriteRestaurants,
        List<Order> savedOrders, List<Order> pastOrders, List<String> allergens) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.homeAddress = homeAddress;
        this.paymentMethod = paymentMethod;
        this.favouriteFoods = favouriteFoods;
        this.favouriteRestaurants = favouriteRestaurants;
        this.savedOrders = savedOrders;
        this.pastOrders = pastOrders;
        this.allergens = allergens;
    }

    public Customer id(Long id) {
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

    public Customer name(String name) {
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

    public Customer surname(String surname) {
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

    public Customer email(String email) {
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

    public Customer homeAddress(Location homeAddress) {
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

    public Customer paymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Get paymentMethod.
     *
     * @return paymentMethod
     */

    @Schema(name = "payment_method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("payment_method")
    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Customer favouriteFoods(List<@Valid Dish> favouriteFoods) {
        this.favouriteFoods = favouriteFoods;
        return this;
    }

    /**
     * Add favouriteFoodsItem.
     *
     * @param favouriteFoodsItem favouriteFoodsItem
     * @return this
     */
    public Customer addFavouriteFoodsItem(Dish favouriteFoodsItem) {
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
    @Schema(name = "favourite_foods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favourite_foods")
    public List<@Valid Dish> getFavouriteFoods() {
        return favouriteFoods;
    }

    public void setFavouriteFoods(List<@Valid Dish> favouriteFoods) {
        this.favouriteFoods = favouriteFoods;
    }

    public Customer favouriteRestaurants(List<@Valid Vendor> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
        return this;
    }

    /**
     * Add favouriteRestaurantsItem.
     *
     * @param favouriteRestaurantsItem favouriteRestaurantsItem
     * @return this
     */
    public Customer addFavouriteRestaurantsItem(Vendor favouriteRestaurantsItem) {
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
    @Schema(name = "favourite_restaurants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favourite_restaurants")
    public List<@Valid Vendor> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public void setFavouriteRestaurants(List<@Valid Vendor> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
    }

    public Customer savedOrders(List<@Valid Order> savedOrders) {
        this.savedOrders = savedOrders;
        return this;
    }

    /**
     * Add savedOrdersItem.
     *
     * @param savedOrdersItem savedOrdersItem
     * @return this
     */
    public Customer addSavedOrdersItem(Order savedOrdersItem) {
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
    @Schema(name = "saved_orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("saved_orders")
    public List<@Valid Order> getSavedOrders() {
        return savedOrders;
    }

    public void setSavedOrders(List<@Valid Order> savedOrders) {
        this.savedOrders = savedOrders;
    }

    public Customer pastOrders(List<@Valid Order> pastOrders) {
        this.pastOrders = pastOrders;
        return this;
    }

    /**
     * Add pastOrdersItem.
     *
     * @param pastOrdersItem pastOrdersItem
     * @return this
     */
    public Customer addPastOrdersItem(Order pastOrdersItem) {
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
    @Schema(name = "past_orders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("past_orders")
    public List<@Valid Order> getPastOrders() {
        return pastOrders;
    }

    public void setPastOrders(List<@Valid Order> pastOrders) {
        this.pastOrders = pastOrders;
    }

    public Customer allergens(List<String> allergens) {
        this.allergens = allergens;
        return this;
    }

    /**
     * Add allergensItem.
     *
     * @param allergensItem allergensItem
     * @return this
     */
    public Customer addAllergensItem(String allergensItem) {
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
        Customer customer = (Customer) o;
        return Objects.equals(this.id, customer.id) && Objects.equals(this.name, customer.name)
            && Objects.equals(this.surname, customer.surname) && Objects.equals(this.email, customer.email)
            && Objects.equals(this.homeAddress, customer.homeAddress)
            && Objects.equals(this.paymentMethod, customer.paymentMethod)
            && Objects.equals(this.favouriteFoods, customer.favouriteFoods)
            && Objects.equals(this.favouriteRestaurants, customer.favouriteRestaurants)
            && Objects.equals(this.savedOrders, customer.savedOrders)
            && Objects.equals(this.pastOrders, customer.pastOrders)
            && Objects.equals(this.allergens, customer.allergens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, homeAddress, paymentMethod, favouriteFoods, favouriteRestaurants,
            savedOrders, pastOrders, allergens);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Customer {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

