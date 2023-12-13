package nl.tudelft.sem.yumyumnow.commons;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }
}
