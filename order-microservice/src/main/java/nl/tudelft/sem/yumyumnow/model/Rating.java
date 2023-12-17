package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Rating of a certain dish.
 */

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    @Column(name = "grade")
    private Long grade;

    @Column(name = "comment")
    private String comment;

    /**
     * Empty constructor.
     */
    public Rating() {}

    /**
     * Constructor.
     *
     * @param grade grade of rating
     * @param comment comment of rating
     */
    public Rating(Long grade, String comment) {
        this.grade = grade;
        this.comment = comment;
    }


    public Rating id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id.
     *
     * @return id
     */

    @Schema(name = "id", example = "34", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rating grade(Long grade) {
        this.grade = grade;
        return this;
    }

    /**
     * Get grade.
     *
     * @return grade
     */

    @Schema(name = "grade", example = "6", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("grade")
    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Rating comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Get comment.
     *
     * @return comment
     */

    @Schema(name = "comment", example = "The food was cold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rating rating = (Rating) o;
        return Objects.equals(this.id, rating.id) && Objects.equals(this.grade, rating.grade)
            && Objects.equals(this.comment, rating.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade, comment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Rating {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    grade: ").append(toIndentedString(grade)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

