package nl.tudelft.sem.yumyumnow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;


@Validated
@Tag(name = "analytics", description = "Statistics about vendors, customers and orders")
public interface AnalyticsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /analytics/vendor/{vendorId}/average-price : Get average-price from a vendor.
     * Get average-price from a vendor&#39;s list.
     *
     * @param vendorId ID of the vendor (required)
     * @return Average price successfully retrieved (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getAveragePrice", summary = "Get average-price from a vendor",
        description = "Get average-price from a vendor's list", tags = {"analytics"},
        responses = {@ApiResponse(responseCode = "200", description = "Average price successfully retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/vendor/{vendorId}/average-price",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getAveragePrice(
            @Parameter(name = "vendorId", description = "ID of the vendor", required = true, in = ParameterIn.PATH)
            @PathVariable("vendorId") Integer vendorId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/rating/vendor/{vendorId} : Get the average rating.
     * Get the average rating of a vendor.
     *
     * @param vendorId ID of the vendor (required)
     * @return Average rating successfully retrieved (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getAverageRating", summary = "Get the average rating",
        description = "Get the average rating of a vendor", tags = {"analytics"},
        responses = {@ApiResponse(responseCode = "200", description = "Average rating successfully retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/rating/vendor/{vendorId}",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getAverageRating(
            @Parameter(name = "vendorId", description = "ID of the vendor", required = true, in = ParameterIn.PATH)
            @PathVariable("vendorId") Long vendorId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/customer/{customerId}/average-price : Get average-price from a customer.
     * Get average-price from a customer.
     *
     * @param customerId ID of the customer (required)
     * @return Average price successfully retrieved (status code 200)
     *     or Customer id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getCustomerAveragePrice", summary = "Get average-price from a customer",
        description = "Get average-price from a customer", tags = {"analytics"}, responses = {
            @ApiResponse(responseCode = "200", description = "Average price successfully retrieved", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Customer id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/customer/{customerId}/average-price",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getCustomerAveragePrice(
            @Parameter(name = "customerId", description = "ID of the customer", required = true, in = ParameterIn.PATH)
            @PathVariable("customerId") Long customerId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/order/{orderId}/rating : Get the order&#39;s rating.
     * Get the order&#39;s rating by the orderId.
     *
     * @param orderId ID of order that will be retrieved (required)
     * @return Order&#39;s rating successfully retrieved (status code 200)
     *     or Order id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrderRating", summary = "Get the order's rating",
        description = "Get the order's rating by the orderId", tags = {"analytics"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order's rating successfully retrieved", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/order/{orderId}/rating",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getOrderRating(
            @Parameter(name = "orderId", description = "ID of order that will be retrieved", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/vendor/{vendorId}/orders-per-day : Get the orders per day.
     * Get the amount of orders per day.
     *
     * @param vendorId ID of vendor (required)
     * @return Order per day successfully retrieved (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrdersPerDay", summary = "Get the orders per day",
        description = "Get the amount of orders per day", tags = {"analytics"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order per day successfully retrieved", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/vendor/{vendorId}/orders-per-day",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getOrdersPerDay(
            @Parameter(name = "vendorId", description = "ID of vendor", required = true, in = ParameterIn.PATH)
            @PathVariable("vendorId") Long vendorId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/customer/{customerId}/orders-per-month : Get the orders per month.
     * Get the amount of orders per month for a customer.
     *
     * @param customerId ID of the customer (required)
     * @return Orders per month successfully retrieved (status code 200)
     *     or Customer id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrdersPerMonth", summary = "Get the orders per month",
        description = "Get the amount of orders per month for a customer", tags = {"analytics"},
        responses = {@ApiResponse(responseCode = "200", description = "Orders per month successfully retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Customer id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/customer/{customerId}/orders-per-month",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getOrdersPerMonth(
            @Parameter(name = "customerId", description = "ID of the customer", required = true, in = ParameterIn.PATH)
            @PathVariable("customerId") Long customerId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /analytics/vendor/{vendorId}/popular-dish : Get popular dish from a vendor.
     * Get popular dish from a vendor&#39;s list of dishes.
     *
     * @param vendorId ID of the vendor (required)
     * @return Popular Dish successfully retrieved (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getPopularDish", summary = "Get popular dish from a vendor",
        description = "Get popular dish from a vendor's list of dishes", tags = {"analytics"},
        responses = {@ApiResponse(responseCode = "200", description = "Popular Dish successfully retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Dish.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = Dish.class))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/analytics/vendor/{vendorId}/popular-dish",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Dish> getPopularDish(
            @Parameter(name = "vendorId", description = "ID of the vendor", required = true, in = ParameterIn.PATH)
            @PathVariable("vendorId") Integer vendorId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : "
                        + "10, \"allergens\" : [ \"allergens\", \"allergens\" ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
                    String exampleString = "<Dish> <id>10</id> <name>pepperoni pizza</name> <allergens>aeiou</allergens> "
                        + "<price>3.149</price> </Dish>";
                    ApiUtil.setExampleResponse(request, "application/xml", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /analytics/order/{orderId}/rating : Sets rating for the order.
     * Set the rating for an order and store it.
     *
     * @param orderId ID of the order to modify (required)
     * @param rating  The rating for the order (optional)
     * @return Rating updated successfully (status code 200)
     *     or Order id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "setOrderRating", summary = "Sets rating for the order",
        description = "Set the rating for an order and store it.", tags = {"analytics"}, responses = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/analytics/order/{orderId}/rating",
        consumes = {"application/json"})
    default ResponseEntity<Void> setOrderRating(
            @Parameter(name = "orderId", description = "ID of the order to modify", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Long orderId,
            @Parameter(name = "Rating", description = "The rating for the order") @Valid @RequestBody(required = false)
            Rating rating) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
