package nl.tudelft.sem.yumyumnow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;


/**
 * Location of a vendor.
 */

public class Location {

    private Double latitude;

    private Double longitude;

    public Location latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Get latitude.
     *
     * @return latitude
     */

    @Schema(name = "latitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Location longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Get longitude.
     *
     * @return longitude
     */

    @Schema(name = "longitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(this.latitude, location.latitude) && Objects.equals(this.longitude, location.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Location {\n");
        sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
        sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
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

