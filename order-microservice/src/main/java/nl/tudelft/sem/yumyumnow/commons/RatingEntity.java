package nl.tudelft.sem.yumyumnow.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.tudelft.sem.yumyumnow.model.Rating;

@Entity
@Table(name = "RatingTable")
public class RatingEntity extends Rating {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Column(name = "grade")
    public Long getGrade() {
        return super.getGrade();
    }

    @Override
    @Column(name = "comment")
    public String getComment() {
        return super.getComment();
    }
}
