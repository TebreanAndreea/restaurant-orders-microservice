package nl.tudelft.sem.yumyumnow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;


@Validated
@Tag(name = "order", description = "A user can make an order")
public interface OrderApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /order/{orderId}/dish/{userId} : Adds a dish to the order
     * Adds a dish to the order dish list and store it.
     *
     * @param orderId ID of the order to which the dish is added (required)
     * @param userId  ID of user who made the order (required)
     * @param dish    The dish to add (optional)
     * @return Dish added successfully to order (status code 200)
     *     or Order or dish id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "addDishToOrder", summary = "Adds a dish to the order",
        description = "Adds a dish to the order dish list and store it.", tags = {"order"},
        responses = {@ApiResponse(responseCode = "200", description = "Dish added successfully to order"),
            @ApiResponse(responseCode = "404", description = "Order or dish id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST, value = "/order/{orderId}/dish/{userId}",
        consumes = {"application/json"})
    default ResponseEntity<Void> addDishToOrder(
            @Parameter(name = "orderId", description = "ID of the order to which the dish is added", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "Dish", description = "The dish to add") @Valid @RequestBody(required = false) Dish dish) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /order/{orderId}/{userId} : Complete order status.
     * Mark an order as complete and change its status.
     *
     * @param orderId ID of the order that is completed (required)
     * @param userId  ID of user who made the order (required)
     * @return Order successfully updated (status code 200)
     *     or Unauthorized (status code 401)
     *     or Order id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "completeOrder", summary = "Complete order status", description = "Mark an order as "
        + "complete and change its status", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order successfully updated", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST, value = "/order/{orderId}/{userId}", produces = {"application/json",
        "application/xml"})
    default ResponseEntity<String> completeOrder(
            @Parameter(name = "orderId", description = "ID of the order that is completed", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /order/new/{customerId} : Creates a new order for the given customer.
     * Creates a new order object for the given customer.
     *
     * @param customerId ID of customer creating the order (required)
     * @return Order successfully created (status code 200)
     *     or Customer id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "createOrder", summary = "Creates a new order for the given customer",
        description = "Creates a new order object for the given customer", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order successfully created", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = Order.class))),
                @Content(mediaType = "application/xml", array = @ArraySchema(schema =
                    @Schema(implementation = Order.class)))}),
            @ApiResponse(responseCode = "404", description = "Customer or Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.POST, value = "/order/new/{customerId}/{vendorId}",
            produces = {"application/json", "application/xml"})
    default ResponseEntity<Order> createOrder(
            @Parameter(name = "customerId", description = "ID of customer creating the order", required = true,
                in = ParameterIn.PATH) @PathVariable("customerId") Long customerId,
            @Parameter(name = "vendorId", description = "ID of vendor for which the customer is creating the order",
                    required = true, in = ParameterIn.PATH) @PathVariable("customerId") Long vendorId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"price\" : 20.3, \"vendor_id\" : 200, "
                            + "\"rating\" : { \"grade\" : 6, \"comment\" : \"The food was cold\", \"id\" : 34 }, "
                            + "\"dishes\" : [ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", "
                            + "\"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] }, "
                            + "{ \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, "
                            + "\"allergens\" : [ \"allergens\", \"allergens\" ] } ], \"location\" : "
                            + "{ \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, "
                            + "\"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, "
                            + "\"status\" : \"pending\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
                    String exampleString = "<Order> <id>202</id> <customer_id>201</customer_id> "
                            + "<vendor_id>200</vendor_id> <price>20.3</price> <Dish> <id>10</id> "
                            + "<name>pepperoni pizza</name> <allergens>aeiou</allergens> <price>3.149</price> "
                            + "</Dish> <time>2000-01-23T04:56:07.000Z</time> <null> <latitude>3.149</latitude> "
                            + "<longitude>3.149</longitude> </null> <null> <id>34</id> <grade>6</grade> "
                            + "<comment>The food was cold</comment> </null> <status>aeiou</status> </Order>";
                    ApiUtil.setExampleResponse(request, "application/xml", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /order/{orderId}/{userId} : Delete an order.
     * Delete an order by its id
     *
     * @param orderId ID of the order that needs to be deleted (required)
     * @param userId  ID of user who made the order (required)
     * @return Order successfully deleted (status code 200)
     *     or Invalid order id (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "deleteOrder", summary = "Delete an order", description = "Delete an order by its id",
        tags = {"order"}, responses = {@ApiResponse(responseCode = "200", description = "Order successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Invalid order id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/order/{orderId}/{userId}")
    default ResponseEntity<Void> deleteOrder(
            @Parameter(name = "orderId", description = "ID of the order that needs to be deleted", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /order/customer/{customerId} : Get a list of all orders a customer has ordered.
     * Get a list of all orders a customer has ordered
     *
     * @param customerId ID of user that needs to be retrieved (required)
     * @return All orders of the customer successfully retrieved. (status code 200)
     *     or Customer id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getListOfOrdersForCustomers", summary = "Get a list of all orders a customer has ordered",
        description = "Get a list of all orders a customer has ordered", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "All orders of the customer successfully retrieved.",
                content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                        @Schema(implementation = Order.class)))}),
            @ApiResponse(responseCode = "404", description = "Customer id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/order/customer/{customerId}", produces = {"application/json"})
    default ResponseEntity<List<Order>> getListOfOrdersForCustomers(
            @Parameter(name = "customerId", description = "ID of user that needs to be retrieved", required = true,
                in = ParameterIn.PATH) @PathVariable("customerId") Long customerId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : "
                        + "{ \"grade\" : 6, \"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : "
                        + "[ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : "
                        + "\"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], "
                        + "\"location\" : { \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, "
                        + "\"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, "
                        + "\"status\" : \"pending\" }, { \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : "
                        + "{ \"grade\" : 6, \"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : "
                        + "[ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : "
                        + "[ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : "
                        + "\"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], "
                        + "\"location\" : { \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, "
                        + "\"id\" : 202, \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : "
                        + "\"pending\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /order/vendor/{vendorId} : Get a list of all orders a vendor has had.
     * Get a list of all orders a vendor has had
     *
     * @param vendorId ID of vendor that needs to be retrieved (required)
     * @return All orders of the vendor successfully retrieved. (status code 200)
     *     or Vendor id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getListOfOrdersForVendor", summary = "Get a list of all orders a vendor has had",
        description = "Get a list of all orders a vendor has had", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "All orders of the vendor successfully retrieved.", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = Dish.class)))}),
            @ApiResponse(responseCode = "404", description = "Vendor id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/order/vendor/{vendorId}", produces = {"application/json"})
    default ResponseEntity<List<Dish>> getListOfOrdersForVendor(
            @Parameter(name = "vendorId", description = "ID of vendor that needs to be retrieved", required = true,
                in = ParameterIn.PATH) @PathVariable("vendorId") Long vendorId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10,"
                        + " \"allergens\" : [ \"allergens\", \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\""
                        + " : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /order/{orderId}/{userId} : Get an Order.
     * Get an Order from the database by id
     *
     * @param orderId ID of the Order (required)
     * @param userId  ID of user who made the order (required)
     * @return Order successfully retrieved (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrder", summary = "Get an Order", description = "Get an Order from the database by id",
        tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Order successfully retrieved", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)),
                @Content(mediaType = "application/xml", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "Order id not found"), @ApiResponse(responseCode = "401",
                description = "Unauthorized"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/order/{orderId}/{userId}", produces = {"application/json",
        "application/xml"})
    default ResponseEntity<Order> getOrder(
            @Parameter(name = "orderId", description = "ID of the Order", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Integer orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"price\" : 20.3, \"vendor_id\" : 200, \"rating\" : { \"grade\" : 6, "
                        + "\"comment\" : \"The food was cold\", \"id\" : 34 }, \"dishes\" : [ { \"price\" : "
                        + "0.8008281904610115, \"name\" : \"pepperoni pizza\", \"id\" : 10, \"allergens\" : [ \"allergens\","
                        + " \"allergens\" ] }, { \"price\" : 0.8008281904610115, \"name\" : \"pepperoni pizza\", "
                        + "\"id\" : 10, \"allergens\" : [ \"allergens\", \"allergens\" ] } ], \"location\" : "
                        + "{ \"latitude\" : 0.8008281904610115, \"longitude\" : 6.027456183070403 }, \"id\" : 202, "
                        + "\"time\" : \"2000-01-23T04:56:07.000+00:00\", \"customer_id\" : 201, \"status\" : \"pending\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/xml"))) {
                    String exampleString = "<Order> <id>202</id> <customer_id>201</customer_id> <vendor_id>200</vendor_id>"
                        + " <price>20.3</price> <Dish> <id>10</id> <name>pepperoni pizza</name> <allergens>aeiou</allergens>"
                        + " <price>3.149</price> </Dish> <time>2000-01-23T04:56:07.000Z</time> <null> "
                        + "<latitude>3.149</latitude> <longitude>3.149</longitude> </null> <null> <id>34</id> "
                        + "<grade>6</grade> <comment>The food was cold</comment> </null> <status>aeiou</status> </Order>";
                    ApiUtil.setExampleResponse(request, "application/xml", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /order/{orderId}/status/{userId} : Get an Order Status.
     * Get an Order Status from the database by id
     *
     * @param orderId ID of the Order (required)
     * @param userId  ID of user who made the order (required)
     * @return Order Status successfully retrieved (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getOrderStatus", summary = "Get an Order Status", description = "Get an Order Status from "
        + "the database by id", tags = {"order"}, responses = {@ApiResponse(responseCode = "200", description = "Order "
                + "Status successfully retrieved", content = {@Content(mediaType = "application/json", schema =
                        @Schema(implementation = Integer.class)), @Content(mediaType = "application/xml", schema =
                        @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/order/{orderId}/status/{userId}",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Integer> getOrderStatus(
            @Parameter(name = "orderId", description = "ID of the Order", required = true, in = ParameterIn.PATH)
            @PathVariable("orderId") Integer orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /order/{orderId}/{userId} : Update an existing order.
     * Update an existing order by the orderId
     *
     * @param orderId  ID of order that needs to be updated (required)
     * @param userId   ID of user that wants to update (required)
     * @param dishes   The list of dishes that needs to be updated (required)
     * @param location The location that needs to be updated (required)
     * @param status   The status of the order that needs to be updated (required)
     * @param time     The time of the order that needs to be updated (required)
     * @return Dish successfully updated (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "modifyOrder", summary = "Update an existing order", description = "Update an "
        + "existing order by the orderId", tags = {"order"}, responses = {
            @ApiResponse(responseCode = "200", description = "Dish successfully updated"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/order/{orderId}/{userId}")
    default ResponseEntity<Void> modifyOrder(
            @Parameter(name = "orderId", description = "ID of order that needs to be updated", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user that wants to update", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @NotNull @Parameter(name = "dishes", description = "The list of dishes that needs to be updated",
                required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "dishes", required = true)
            List<@Valid Dish> dishes,
            @NotNull @Parameter(name = "location", description = "The location that needs to be updated",
                required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "location",
                required = true) String location,
            @NotNull @Parameter(name = "status", description = "The status of the order that needs to be updated",
                required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "status",
                required = true) String status,
            @NotNull @Parameter(name = "time", description = "The time of the order that needs to be updated",
                required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "time", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime time) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /order/{orderId}/dish/{userId} : Removes a dish from an order
     * Removes a dish from an order dish list and store it.
     *
     * @param orderId ID of the order from which the dish is removed (required)
     * @param userId  ID of user who made the order (required)
     * @param dish    The dish to remove (optional)
     * @return Dish removed successfully from order (status code 200)
     *     or Internal server error (status code 500)
     *     or Unauthorized (status code 401)
     *     or Dish or order not found (status code 404)
     */
    @Operation(operationId = "removeDishFromOrder", summary = "Removes a dish from an order",
        description = "Removes a dish from an order dish list and store it.", tags = {"order"},
        responses = {@ApiResponse(responseCode = "200", description = "Dish removed successfully from order"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404",
                description = "Dish or order not found")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/order/{orderId}/dish/{userId}",
        consumes = {"application/json"})
    default ResponseEntity<Void> removeDishFromOrder(
            @Parameter(name = "orderId", description = "ID of the order from which the dish is removed",
                required = true, in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "Dish", description = "The dish to remove") @Valid @RequestBody(required = false) Dish dish) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /order/{orderId}/requirements/{userId} : Sets special requirements for the order
     * Set the special requirements for an order and store it.
     *
     * @param orderId ID of the order to modify (required)
     * @param userId  ID of user who made the order (required)
     * @param body    The requirements for the order (optional)
     * @return Requirements updated successfully (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "setOrderRequirements", summary = "Sets special requirements for the order",
        description = "Set the special requirements for an order and store it.", tags = {"order"},
        responses = {@ApiResponse(responseCode = "200", description = "Requirements updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/order/{orderId}/requirements/{userId}",
        consumes = {"application/json"})
    default ResponseEntity<Void> setOrderRequirements(
            @Parameter(name = "orderId", description = "ID of the order to modify", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Long orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "body", description = "The requirements for the order")
            @Valid @RequestBody(required = false) String body) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /order/{orderId}/status/{userId} : Update the status for an order.
     * Update the status for an order using its id
     *
     * @param orderId ID of order that needs to be handled (required)
     * @param userId  ID of user who made the order (required)
     * @param status  new status of order that needs to be handled (optional)
     * @param body    Update an order&#39;s status (optional)
     * @return Order&#39;s status successfully updated (status code 200)
     *     or Order id not found (status code 404)
     *     or Unauthorized (status code 401)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "setOrderStatus", summary = "Update the status for an order",
        description = "Update the status for an order using its id", tags = {"order"},
        responses = {@ApiResponse(responseCode = "200", description = "Order's status successfully updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Order id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/order/{orderId}/status/{userId}",
        produces = {"application/json", "application/xml"}, consumes = {"application/json"})
    default ResponseEntity<String> setOrderStatus(
            @Parameter(name = "orderId", description = "ID of order that needs to be handled", required = true,
                in = ParameterIn.PATH) @PathVariable("orderId") Integer orderId,
            @Parameter(name = "userId", description = "ID of user who made the order", required = true,
                in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "status", description = "new status of order that needs to be handled",
                in = ParameterIn.QUERY) @Valid @RequestParam(value = "status", required = false) String status,
            @Parameter(name = "body", description = "Update an order's status")
            @Valid @RequestBody(required = false) String body) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
