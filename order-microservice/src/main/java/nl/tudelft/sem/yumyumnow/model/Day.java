package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;


/**
 * Day of the week.
 */

public class Day {

    private String open;

    private String close;

    /**
     * Empty constructor.
     */
    public Day() {
    }

    /**
     * Constructor.
     * @param open open time
     * @param close close time
     */
    public Day(String open, String close) {
        this.open = open;
        this.close = close;
    }

    public Day open(String open) {
        this.open = open;
        return this;
    }

    /**
     * Get open.
     *
     * @return open
     */

    @Schema(name = "open", example = "09:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("open")
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Day close(String close) {
        this.close = close;
        return this;
    }

    /**
     * Get close.
     *
     * @return close
     */

    @Schema(name = "close", example = "1320", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("close")
    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day = (Day) o;
        return Objects.equals(this.open, day.open) && Objects.equals(this.close, day.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(open, close);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Day {\n");
        sb.append("    open: ").append(toIndentedString(open)).append("\n");
        sb.append("    close: ").append(toIndentedString(close)).append("\n");
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

