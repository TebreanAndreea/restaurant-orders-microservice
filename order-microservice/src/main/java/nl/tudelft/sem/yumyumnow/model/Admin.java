package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Admin of YumYUmNow.
 */


public class Admin {

    private Long id;

    private String name;

    private String surname;

    private String email;

    public Admin id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Empty constructor.
     */
    public Admin() {
    }

    /**
     * Constructor.
     * @param name name of admin
     * @param surname surname of admin
     * @param email email of admin
     */
    public Admin(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
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

    public Admin name(String name) {
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

    public Admin surname(String surname) {
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

    public Admin email(String email) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Admin admin = (Admin) o;
        return Objects.equals(this.id, admin.id) && Objects.equals(this.name, admin.name)
            && Objects.equals(this.surname, admin.surname) && Objects.equals(this.email, admin.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Admin {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

