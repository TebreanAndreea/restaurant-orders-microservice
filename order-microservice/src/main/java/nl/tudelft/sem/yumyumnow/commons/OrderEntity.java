package nl.tudelft.sem.yumyumnow.commons;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.tudelft.sem.yumyumnow.model.Order;

@Entity
@Table(name = "OrderTable")
public class OrderEntity extends Order {
    @Override
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Column(name = "customer_id")
    public Long getCustomerId() {
        return super.getCustomerId();
    }

    @Override
    @Column(name = "vendor_id")
    public Long getVendorId() {
        return super.getVendorId();
    }

    @Override
    @Column(name = "price")
    public Double getPrice() {
        return super.getPrice();
    }

    @Column(name = "dishes")
    private String dishEntities;

    public void setDishEntities() {
        this.dishEntities = super.getDishes().stream().toString();
    }

    @Override
    @Column(name = "time")
    public OffsetDateTime getTime() {
        return super.getTime();
    }

    @Column(name = "location")
    private String location;

    public void setLocationEntity() {
        this.location = super.getLocation().toString();
    }

    public String getLocationEntity() {
        return location;
    }

    @Column(name = "rating")
    private String rating;

    public void setRatingEntity() {
        this.location = super.getRating().toString();
    }

    public String getRatingEntity() {
        return location;
    }
}
