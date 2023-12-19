package nl.tudelft.sem.yumyumnow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;


@Validated
@Tag(name = "dish", description = "An order consists of multiple dishes")
public interface DishApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /dish/{dishId} : Get a dish.
     * Get a dish from the database by id.
     *
     * @param dishId ID of the Dish (required)
     * @return Dish successfully retrieved (status code 200)
     *     or Invalid id (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "getDish", summary = "Get a dish", description = "Get a dish from the database by id",
        tags = {"dish"}, responses = {@ApiResponse(responseCode = "200", description = "Dish successfully retrieved",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Dish.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = Dish.class))}),
            @ApiResponse(responseCode = "404", description = "Invalid id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.GET, value = "/dish/{dishId}",
        produces = {"application/json", "application/xml"})
    default ResponseEntity<Dish> getDish(
            @Parameter(name = "dishId", description = "ID of the Dish", required = true, in = ParameterIn.PATH)
            @PathVariable("dishId") Integer dishId) {
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
     * PUT /dish/{dishId} : Update a dish.
     * Update a dish from the database by id.
     *
     * @param dishId ID of the Dish (required)
     * @return Dish successfully updated (status code 200)
     *     or Dish id not found (status code 404)
     *     or Internal server error (status code 500)
     */
    @Operation(operationId = "updateDish", summary = "Update a dish", description = "Update a dish from the database by id",
        tags = {"dish"}, responses = {@ApiResponse(responseCode = "200", description = "Dish successfully updated",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Dish.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = Dish.class))}),
            @ApiResponse(responseCode = "404", description = "Dish id not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(method = RequestMethod.PUT, value = "/dish/{dishId}", produces = {"application/json",
        "application/xml"})
    default ResponseEntity<Dish> updateDish(
            @Parameter(name = "dishId", description = "ID of the Dish", required = true, in = ParameterIn.PATH)
            @PathVariable("dishId") Integer dishId) {
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

}
