package nl.tudelft.sem.template.commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.tudelft.sem.template.model.Rating;

@Entity
@Table(name = "RatingTable")
public class RatingEntity extends Rating{
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }
}
