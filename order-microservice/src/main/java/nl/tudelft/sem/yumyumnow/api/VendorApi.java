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
import javax.validation.constraints.NotNull;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;


@Validated
@Tag(name = "vendor", description = "A user who owns a restaurant")
public interface VendorApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /vendor/{vendorId}/dish/new : Adds a new dish to the catalog of a vendor.
     * Adds a new dish to the catalog of a vendor.
     *
     * @param vendorId ID of vendor that needs to add the new dish (required)
     * @param dish     (optional)
     * @return Vendor not found (status code 404)
     *     or The dish was successfully added (status code 200)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "addDishToVendor", summary = "Adds a new dish to the catalog of a vendor.",
        description = "Adds a new dish to the catalog of a vendor.", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "404", description = "Vendor not found"),
            @ApiResponse(responseCode = "200", description = "The dish was successfully added"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST, value = "/vendor/{vendorId}/dish/new", consumes = {"application/json"})
    default ResponseEntity<Void> addDishToVendor(
            @Parameter(name = "vendorId", description = "ID of vendor that needs to add the new dish", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId,
            @Parameter(name = "Dish", description = "") @Valid @RequestBody(required = false) Dish dish) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /vendor : Get all vendors that matches a filter (optional).
     * Get all vendors from the database that matches a filter (optional).
     *
     * @param filter Filtering criterion, it will be use to match restaurant names by string comparison. (optional)
     * @return All vendors are successfully retrieved (status code 200)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getAllVendors", summary = "Get all vendors that matches a filter (optional)",
        description = "Get all vendors from the database that matches a filter (optional)", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "200", description = "All vendors are successfully retrieved", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema =
                @Schema(implementation = Vendor.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/vendor", produces = {"application/json"})
    default ResponseEntity<List<Vendor>> getAllVendors(
            @Parameter(name = "filter", description = "Filtering criterion, it will be use to match restaurant names by "
                + "string comparison.", in = ParameterIn.QUERY)
            @Valid @RequestParam(value = "filter", required = false) String filter) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ null, null ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /vendor/close : Get all vendors.
     * Get all vendors from the database around the address.
     *
     * @param location The address of the customer (required)
     * @param filter   Filtering criterion, it will be use to match restaurant names by string comparison. (optional)
     * @param radius   The radius (in meters) in which the restaurants should be searched. By default, it is 1000m.(optional)
     * @return All vendors are successfully retrieved (status code 200)
     *     or Invalid address (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getAllVendorsAddress", summary = "Get all vendors", description = "Get all vendors from "
        + "the database around the address", tags = {"vendor"}, responses = {
            @ApiResponse(responseCode = "200", description = "All vendors are successfully retrieved", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = Vendor.class)))}),
            @ApiResponse(responseCode = "404", description = "Invalid address"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/vendor/close", produces = {"application/json"})
    default ResponseEntity<List<Vendor>> getAllVendorsAddress(
            @NotNull @Parameter(name = "location", description = "The address of the customer",
                required = true, in = ParameterIn.HEADER) @RequestHeader(value = "location", required = true)
            Location location,
            @Parameter(name = "filter", description = "Filtering criterion, it will be use to match restaurant names "
                + "by string comparison.", in = ParameterIn.QUERY)
            @Valid @RequestParam(value = "filter", required = false) String filter,
            @Parameter(name = "radius", description = "The radius (in meters) in which the restaurants should be "
                + "searched. By default, it is 1000m.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "radius",
                required = false) Integer radius) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ null, null ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /vendor/{vendorId}/order/{orderId} : Fetches the dishes which need to be prepared.
     * Retrieves a list of dishes from an order which a specific vendor needs to prepare.
     *
     * @param orderId  ID of order which contains the certain dishes (required)
     * @param vendorId ID of vendor that needs to prepare the dishes (required)
     * @return All dishes to prepare were successfully retrieved (status code 200)
     *     or Order or vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getDishesToPrepare", summary = "Fetches the dishes which need to be prepared.",
        description = "Retrieves a list of dishes from an order which a specific vendor needs to prepare.",
        tags = {"vendor"}, responses = {
            @ApiResponse(responseCode = "200", description = "All dishes to prepare were successfully retrieved", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = Dish.class)))}),
            @ApiResponse(responseCode = "404", description = "Order or vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/vendor/{vendorId}/order/{orderId}",
        produces = {"application/json"})
    default ResponseEntity<List<Dish>> getDishesToPrepare(
            @Parameter(name = "orderId", description = "ID of order which contains the certain dishes", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Integer orderId,
            @Parameter(name = "vendorId", description = "ID of vendor that needs to prepare the dishes", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", "
                        + "\"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] }, { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /vendor/{vendorId} : Get a vendor by id.
     * Get a vendor from the database by Id.
     *
     * @param vendorId ID of the vendor that is selected. (required)
     * @return Vendor successfully retrieved (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getVendor", summary = "Get a vendor by id",
        description = "Get a vendor from the database by Id", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "200", description = "Vendor successfully retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Vendor.class))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/vendor/{vendorId}", produces = {"application/json"})
    default ResponseEntity<Vendor> getVendor(
            @Parameter(name = "vendorId", description = "ID of the vendor that is selected.", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "null";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /vendor/{vendorId}/dish/{customerId} : Get all dishes from a vendor, except dishes having any of the
     * customer&#39;s allergens.
     * Get the dishes of a vendor from the database by vendorId except dishes having any of the customer&#39;s allergens.
     *
     * @param vendorId   ID of the vendor that is selected. (required)
     * @param customerId ID of user that is making the query. It allows to filter for the customer&#39;s allergens.(required)
     * @return All dishes are successfully retrieved (status code 200)
     *     or Customer or vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getVendorDishes", summary = "Get all dishes from a vendor, except dishes having any "
        + "of the customer's allergens", description = "Get the dishes of a vendor from the database by vendorId except "
        + "dishes having any of the customer's allergens", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "200", description = "All dishes are successfully retrieved", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Dish.class)))}),
            @ApiResponse(responseCode = "404", description = "Customer or vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/vendor/{vendorId}/dish/{customerId}",
        produces = {"application/json"})
    default ResponseEntity<List<Dish>> getVendorDishes(
            @Parameter(name = "vendorId", description = "ID of the vendor that is selected.", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId,
            @Parameter(name = "customerId", description = "ID of user that is making the query. It allows to filter for "
                + "the customer's allergens.", required = true, in = ParameterIn.PATH)
            @PathVariable("customerId") Long customerId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", "
                        + "\"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] }, { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\","
                        + " \"allergens\" ] } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /vendor/{vendorId}/dish/{dishId} : Modifies a dish from the catalog of a vendor.
     * Modifies a dish from the catalog of a vendor.
     *
     * @param dishId   ID of dish which needs to be updated (required)
     * @param vendorId ID of vendor that needs to update the dish (required)
     * @param dish     Update a dish (optional)
     * @return Dish or vendor not found (status code 404)
     *     or The dish was successfully modified (status code 200)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "modifyDishFromVendor", summary = "Modifies a dish from the catalog of a vendor.",
        description = "Modifies a dish from the catalog of a vendor.", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "404", description = "Dish or vendor not found"),
            @ApiResponse(responseCode = "200", description = "The dish was successfully modified"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/vendor/{vendorId}/dish/{dishId}",
        consumes = {"application/json"})
    default ResponseEntity<Void> modifyDishFromVendor(
            @Parameter(name = "dishId", description = "ID of dish which needs to be updated", required = true,
                in = ParameterIn.PATH) @PathVariable("dishId") Long dishId,
            @Parameter(name = "vendorId", description = "ID of vendor that needs to update the dish", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId,
            @Parameter(name = "Dish", description = "Update a dish") @Valid @RequestBody(required = false) Dish dish) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /vendor/{vendorId}/dish/{dishId} : Removes a dish from the catalog of a vendor.
     * Removes a dish from the catalog of a vendor.
     *
     * @param dishId   ID of dish which needs to be removed (required)
     * @param vendorId ID of vendor that needs to remove the dish (required)
     * @return Dish or vendor not found (status code 404)
     *     or The dish was successfully removed (status code 200)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "removeDishFromVendor", summary = "Removes a dish from the catalog of a vendor.",
        description = "Removes a dish from the catalog of a vendor.", tags = {"vendor"},
        responses = {@ApiResponse(responseCode = "404", description = "Dish or vendor not found"),
            @ApiResponse(responseCode = "200", description = "The dish was successfully removed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/vendor/{vendorId}/dish/{dishId}")
    default ResponseEntity<Void> removeDishFromVendor(
            @Parameter(name = "dishId", description = "ID of dish which needs to be removed",
                required = true, in = ParameterIn.PATH) @PathVariable("dishId") Integer dishId,
            @Parameter(name = "vendorId", description = "ID of vendor that needs to remove the dish",
                required = true, in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
