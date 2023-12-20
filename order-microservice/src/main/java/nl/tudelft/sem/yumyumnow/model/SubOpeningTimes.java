package nl.tudelft.sem.yumyumnow.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "opening_times")
public class SubOpeningTimes extends OpeningTimes {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    /**
     * Empty constructor for mapper.
     */
    public SubOpeningTimes() {
    }

    /**
     * Constructor.
     *
     * @param monday opening / closing times on mondays
     * @param tuesday opening / closing times on tuesdays
     * @param wednesday opening / closing times on wednesdays
     * @param thursday opening / closing times on thursdays
     * @param friday opening / closing times on fridays
     * @param saturday opening / closing times on saturdays
     * @param sunday opening / closing times on sundays
     */
    public SubOpeningTimes(Day monday, Day tuesday, Day wednesday, Day thursday, Day friday, Day saturday, Day sunday) {
        super(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubOpeningTimes)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SubOpeningTimes that = (SubOpeningTimes) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

}
