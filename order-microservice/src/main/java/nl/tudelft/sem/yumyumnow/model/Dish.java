package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 * Dish of a vendor.
 */

@Entity
@Table(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "allergens")
    @Valid
    private List<String> allergens;

    @Column(name = "price")
    private Double price;

    /**
     * Empty constructor.
     */
    public Dish() {}

    /**
     * Constructor.
     *
     * @param name name of dish
     * @param allergens allergens of dish
     * @param price price of dish
     */
    public Dish(String name, List<String> allergens, Double price) {
        this.name = name;
        this.allergens = allergens;
        this.price = price;
    }

    public Dish id(Long id) {
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

    public Dish name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name.
     *
     * @return name
     */

    @Schema(name = "name", example = "pepperoni pizza", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dish allergens(List<String> allergens) {
        this.allergens = allergens;
        return this;
    }

    /**
     * Add allergensItem.
     *
     * @param allergensItem allergensItem
     * @return Dish
     */
    public Dish addAllergensItem(String allergensItem) {
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

    public Dish price(Double price) {
        this.price = price;
        return this;
    }

    /**
     * Get price.
     *
     * @return price
     */

    @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dish dish = (Dish) o;
        return Objects.equals(this.id, dish.id) && Objects.equals(this.name, dish.name)
            && Objects.equals(this.allergens, dish.allergens) && Objects.equals(this.price, dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, allergens, price);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Dish {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    allergens: ").append(toIndentedString(allergens)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

