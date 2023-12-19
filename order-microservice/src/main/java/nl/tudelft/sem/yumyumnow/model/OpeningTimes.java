package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.Valid;


/**
 * OpeningTimes of a vendor.
 */

public class OpeningTimes {

    private Day monday;

    private Day tuesday;

    private Day wednesday;

    private Day thursday;

    private Day friday;

    private Day saturday;

    private Day sunday;

    public OpeningTimes monday(Day monday) {
        this.monday = monday;
        return this;
    }

    /**
     * Get monday.
     *
     * @return monday
     */
    @Valid
    @Schema(name = "monday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("monday")
    public Day getMonday() {
        return monday;
    }

    public void setMonday(Day monday) {
        this.monday = monday;
    }

    public OpeningTimes tuesday(Day tuesday) {
        this.tuesday = tuesday;
        return this;
    }

    /**
     * Get tuesday.
     *
     * @return tuesday
     */
    @Valid
    @Schema(name = "tuesday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("tuesday")
    public Day getTuesday() {
        return tuesday;
    }

    public void setTuesday(Day tuesday) {
        this.tuesday = tuesday;
    }

    public OpeningTimes wednesday(Day wednesday) {
        this.wednesday = wednesday;
        return this;
    }

    /**
     * Get wednesday.
     *
     * @return wednesday
     */
    @Valid
    @Schema(name = "wednesday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("wednesday")
    public Day getWednesday() {
        return wednesday;
    }

    public void setWednesday(Day wednesday) {
        this.wednesday = wednesday;
    }

    public OpeningTimes thursday(Day thursday) {
        this.thursday = thursday;
        return this;
    }

    /**
     * Get thursday.
     *
     * @return thursday
     */
    @Valid
    @Schema(name = "thursday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("thursday")
    public Day getThursday() {
        return thursday;
    }

    public void setThursday(Day thursday) {
        this.thursday = thursday;
    }

    public OpeningTimes friday(Day friday) {
        this.friday = friday;
        return this;
    }

    /**
     * Get friday.
     *
     * @return friday
     */
    @Valid
    @Schema(name = "friday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("friday")
    public Day getFriday() {
        return friday;
    }

    public void setFriday(Day friday) {
        this.friday = friday;
    }

    public OpeningTimes saturday(Day saturday) {
        this.saturday = saturday;
        return this;
    }

    /**
     * Get saturday.
     *
     * @return saturday
     */
    @Valid
    @Schema(name = "saturday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("saturday")
    public Day getSaturday() {
        return saturday;
    }

    public void setSaturday(Day saturday) {
        this.saturday = saturday;
    }

    public OpeningTimes sunday(Day sunday) {
        this.sunday = sunday;
        return this;
    }

    /**
     * Get sunday.
     *
     * @return sunday
     */
    @Valid
    @Schema(name = "sunday", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("sunday")
    public Day getSunday() {
        return sunday;
    }

    public void setSunday(Day sunday) {
        this.sunday = sunday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpeningTimes openingTimes = (OpeningTimes) o;
        return Objects.equals(this.monday, openingTimes.monday) && Objects.equals(this.tuesday, openingTimes.tuesday)
            && Objects.equals(this.wednesday, openingTimes.wednesday) && Objects.equals(this.thursday, openingTimes.thursday)
            && Objects.equals(this.friday, openingTimes.friday) && Objects.equals(this.saturday, openingTimes.saturday)
            && Objects.equals(this.sunday, openingTimes.sunday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpeningTimes {\n");
        sb.append("    monday: ").append(toIndentedString(monday)).append("\n");
        sb.append("    tuesday: ").append(toIndentedString(tuesday)).append("\n");
        sb.append("    wednesday: ").append(toIndentedString(wednesday)).append("\n");
        sb.append("    thursday: ").append(toIndentedString(thursday)).append("\n");
        sb.append("    friday: ").append(toIndentedString(friday)).append("\n");
        sb.append("    saturday: ").append(toIndentedString(saturday)).append("\n");
        sb.append("    sunday: ").append(toIndentedString(sunday)).append("\n");
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

