package nl.tudelft.sem.yumyumnow.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.tudelft.sem.yumyumnow.model.Dish;

@Entity
@Table(name = "DishTable")
public class DishEntity extends Dish {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_seq")
    @SequenceGenerator(name = "dish_seq", sequenceName = "dish_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Column(name = "name")
    public String getName() {
        return super.getName();
    }

    @Override
    @Column(name = "price")
    public Double getPrice() {
        return super.getPrice();
    }
}
