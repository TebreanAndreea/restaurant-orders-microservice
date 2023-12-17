package nl.tudelft.sem.yumyumnow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.tudelft.sem.yumyumnow.model.Order;
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
@Tag(name = "admin", description = "An admin of YumYumNow")
public interface AdminApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /admin/order/all/{adminId} : Get all their orders in the system.
     * Get all their orders in the system.
     *
     * @param adminId ID of admin (required)
     * @return All orders are retrieved. (status code 200)
     *     or Admin id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getAllOrders", summary = "Get all their orders in the system",
        description = "Get all their orders in the system", tags = {"admin"}, responses = {
            @ApiResponse(responseCode = "200", description = "All orders are retrieved.", content = {
                @Content(mediaType = "application/json", array =
                    @ArraySchema(schema = @Schema(implementation = Order.class)))}),
            @ApiResponse(responseCode = "404", description = "Admin id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/admin/order/all/{adminId}", produces = {"application/json"})
    default ResponseEntity<List<Order>> getAllOrders(
            @Parameter(name = "adminId", description = "ID of admin", required = true, in = ParameterIn.PATH)
            @PathVariable("adminId") Long adminId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, "
                        + "\"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : "
                        + "\"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], "
                        + "\"location\" : { \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, "
                        + "\"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : "
                        + "\"pending\" }, { \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, "
                        + "\"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\","
                        + " \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : "
                        + "10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], \"location\" : { \"latitude\" : "
                        + "0.8008281904610115, \"longitude\" : 6.027456183070403 }, \"id\" : 202, \"time\" : "
                        + "\"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : \"pending\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /admin/order/vendor/{vendorId}/{customerId}/{adminId} : Get a list of all orders a vendor has had.
     * Get a list of all orders a vendor has had from a specific client.
     *
     * @param vendorId   ID of vendor that needs to be retrieved (required)
     * @param customerId ID of customer that needs to be retrieved (required)
     * @param adminId    ID of admin (required)
     * @return All orders of the vendor successfully retrieved. (status code 200)
     *     or Vendor or customer id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getListOfOrdersForVendorForClient", summary = "Get a list of all orders a vendor has had",
        description = "Get a list of all orders a vendor has had from a specific client", tags = {"admin"},
        responses = {@ApiResponse(responseCode = "200", description = "All orders of the vendor successfully retrieved.",
                content = {@Content(mediaType = "application/json", array =
                        @ArraySchema(schema = @Schema(implementation = Order.class)))}),
            @ApiResponse(responseCode = "404", description = "Vendor or customer id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/admin/order/vendor/{vendorId}/{customerId}/{adminId}",
        produces = {"application/json"})
    default ResponseEntity<List<Order>> getListOfOrdersForVendorForClient(
            @Parameter(name = "vendorId", description = "ID of vendor that needs to be retrieved", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId,
            @Parameter(name = "customerId", description = "ID of customer that needs to be retrieved", required = true,
                in = ParameterIn.PATH) @PathVariable("customerId") Long customerId,
            @Parameter(name = "adminId", description = "ID of admin", required = true, in = ParameterIn.PATH)
            @PathVariable("adminId") Long adminId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, "
                        + "\"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni "
                        + "pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], \"location\" : { "
                        + "\"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, \"id\" : 202, \"time\" : "
                        + "\"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : \"pending\" }, "
                        + "{ \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, \"comment\" : "
                        + "\"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : 0.8008281904610115, "
                        + "\"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] }, "
                        + "{ \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] } ], \"location\" : { \"latitude\" : 0.8008281904610115, "
                        + "\"longitude\" : 6.027456183070403 }, \"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", "
                        + "\"customer_id\" : 201, \"status\" : \"pending\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /admin/order/{orderId}/{adminId} : Get an order by its id.
     * Get an order by its id.
     *
     * @param orderId ID of the Order (required)
     * @param adminId ID of the Admin (required)
     * @return Order is retrieved successfully. (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrderAdmin", summary = "Get an order by its id", description = "Get an order by its id",
        tags = {"admin"}, responses = {@ApiResponse(responseCode = "200", description = "Order is retrieved successfully.",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/admin/order/{orderId}/{adminId}",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Order> getOrderAdmin(
            @Parameter(name = "orderId", description = "ID of the Order", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Long orderId,
            @Parameter(name = "adminId", description = "ID of the Admin", required = true, in = ParameterIn.PATH)
            @PathVariable("adminId") Long adminId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, "
                        + "\"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : "
                        + "\"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], "
                        + "\"location\" : { \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, "
                        + "\"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : "
                        + "\"pending\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
                    String exampleString = "<null> <id>202</id> <customer_id>201</customer_id> <vendor_id>200</vendor_id> "
                        + "<price>20.3</price> <Dish> <id>10</id> <name>pepperoni pizza</name> <allergens>aeiou</allergens> "
                        + "<price>3.149</price> </Dish> <time>2000-01-23T04:56:07.000Z</time> "
                        + "<status>aeiou</status> </null>";
                    ApiUtil.setExampleResponse(request, "application/xml", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /admin/order/{orderId}/{adminId} : Remove an order by its id.
     * Remove an order by its id.
     *
     * @param orderId ID of the Order (required)
     * @param adminId ID of the Admin (required)
     * @return Order deleted successfully (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "removeOrder", summary = "Remove an order by its id",
        description = "Remove an order by its id", tags = {"admin"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/order/{orderId}/{adminId}")
    default ResponseEntity<Void> removeOrder(
            @Parameter(name = "orderId", description = "ID of the Order", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Long orderId,
            @Parameter(name = "adminId", description = "ID of the Admin", required = true, in = ParameterIn.PATH)
            @PathVariable("adminId") Long adminId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /admin/order/{orderId}/{adminId} : Update an order by its id.
     * Update an order by its id.
     *
     * @param orderId ID of the Order (required)
     * @param adminId ID of the Admin (required)
     * @param order   The order to update (optional)
     * @return Order updated successfully (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "updateOrder", summary = "Update an order by its id",
        description = "Update an order by its id", tags = {"admin"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/admin/order/{orderId}/{adminId}",
        consumes = {"application/json", "application/xml"})
    default ResponseEntity<Void> updateOrder(
            @Parameter(name = "orderId", description = "ID of the Order", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Long orderId,
            @Parameter(name = "adminId", description = "ID of the Admin", required = true, in = ParameterIn.PATH)
            @PathVariable("adminId") Long adminId,
            @Parameter(name = "Order", description = "The order to update")
            @Valid @RequestBody(required = false) Order order) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
