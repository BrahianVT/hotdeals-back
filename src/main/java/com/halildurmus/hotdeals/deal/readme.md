## com.halildurmus.hotdeals.deal.Deal
1. Deal Model (com.halildurmus.hotdeals.deal.Deal)

* This class represents the **"Deal"** entity stored in the MongoDB database.
* It includes fields like id, postedBy, store, dealScore, upvoters, downvoters, category, title, description, originalPrice, price, coverPhoto, dealUrl, status, photos, and views.
* It extends DateAudit, which likely provides fields for creation and update timestamps.
* It uses annotations like @Document, @TypeAlias, @Indexed, @JsonProperty, @JsonSerialize, @NotNull, @NotBlank, @Size, @Min, and @URL for MongoDB mapping, JSON serialization/deserialization, validation, and schema documentation (Swagger).
 

2. Deal DTOs (com.halildurmus.hotdeals.deal.dto.*)

* These classes are Data Transfer Objects (DTOs) used for transferring data between the client and the server.
* DealGetDTO: Used for retrieving deal information. It includes fields relevant for displaying deal details to the client.
* DealPatchDTO: Used for patching (partially updating) a deal, specifically for updating the status of a deal.
* DealPostDTO: Used for creating a new deal. It includes fields required for creating a deal.

3. Deal Repository (com.halildurmus.hotdeals.deal.DealRepository)

* This is a Spring Data MongoDB repository interface that provides methods for interacting with the "deals" collection in MongoDB.
* It extends MongoRepository, which provides basic CRUD operations.
* It includes custom methods for finding deals by various criteria (category, store, status, posted by) and counting deals.
* It uses Spring Cache annotations (@Cacheable, @CachePut, @CacheEvict, @Caching) to cache query results and improve performance.


4. Deal Service (com.halildurmus.hotdeals.deal.DealService and com.halildurmus.hotdeals.deal.DealServiceImpl)

* This is the service layer responsible for implementing the business logic related to deals.
* DealService is the interface defining the available operations.
* DealServiceImpl is the implementation of the DealService interface.
* It uses the DealRepository to interact with the database.
* It includes methods for:
  * Finding all deals, counting deals by user or store.
  * Finding deals by ID, category, or store.
  * Getting the latest active deals and most liked active deals.
  * Creating, updating, patching, and deleting deals.
  * Voting on deals (upvote, downvote, unvote).
* It uses MongoTemplate for more complex MongoDB operations like incrementing the views counter and updating deals using aggregation updates.
* It interacts with the EsDealService for Elasticsearch integration.
* It uses SecurityService to get the current authenticated user and enforce access control (e.g., allowing users to only update or delete their own deals).
* It handles exceptions like DealNotFoundException and ResponseStatusException to provide appropriate error responses to the client.


5. Elasticsearch Integration (com.halildurmus.hotdeals.deal.es.*)

* These classes provide integration with Elasticsearch for searching deals.
* EsDeal: Represents the Deal entity in Elasticsearch.
* EsDealRepository: Spring Data Elasticsearch repository interface for EsDeal.
* EsDealService and EsDealServiceImpl: Service layer for Elasticsearch operations, including searching deals with various filters and aggregations, and providing search suggestions.
* NumberFacet and StringFacet: Helper classes used for defining facets (for filtering and aggregation) in Elasticsearch.

6. Deal Controller (com.halildurmus.hotdeals.deal.DealController)

* This is the REST controller that exposes the Deal service endpoints.
* It handles HTTP requests and delegates the business logic to the DealService.
* It uses Spring Web annotations like @RestController, @RequestMapping, @GetMapping, @PostMapping, @PatchMapping, @PutMapping, @DeleteMapping, @PathVariable, @RequestParam, @RequestBody, @ResponseStatus, and @Validated.
* It uses Swagger annotations (@Tag, @Operation, @ApiResponses, @ApiResponse, @Parameter, @SecurityRequirement, @Schema, @Content, @ExampleObject, @ArraySchema) to document the API endpoints.
* It includes methods for:
  * Getting all deals, count of deals by user or store.
  * Searching deals by category, store, or using Elasticsearch with various filters and sorting options.
  * Getting search suggestions.
  * Getting a deal by ID.
  * Creating, patching, updating, and deleting a deal.
  * Getting comments for a deal, getting comment count for a deal.
  * Posting a comment to a deal.
  * Reporting a comment or a deal.
  * Voting on a deal.
* It uses MapStructMapper to map between DTOs and the Deal entity.
* It includes custom validation annotations like @IsObjectId to validate input parameters.

7. Other Helper Classes (com.halildurmus.hotdeals.deal.*)

* DealStatus: Enum representing the status of a deal (ACTIVE or EXPIRED).
* DealVote: Class representing a vote on a deal, including the vote type.
* DealVoteType: Enum representing the type of vote (UP, DOWN, UNVOTE).
* PriceRange: Class representing a price range used for filtering deals.
* SearchSuggestion: Class representing a search suggestion.
* DealSearchParams: Class used to hold search parameters for Elasticsearch queries.

**Overall Functionality:**
This service provides a comprehensive set of features for managing deals in the application. It leverages MongoDB for storing deal data, Elasticsearch for efficient searching, and Spring Boot for building the RESTful API. The use of DTOs, repositories, services, and controllers follows best practices for building a layered and maintainable application. The inclusion of caching and Elasticsearch integration highlights the focus on performance and search capabilities. The security annotations indicate that some endpoints require authentication and authorization.


The GET **/deals** endpoint in DealController.java is designed to return all deals. Here's a breakdown of the flow:

* Request Reception: The getDeals method in DealController.java is triggered when a GET request is made to /deals.
* Authorization: The @IsSuper annotation indicates that this endpoint is restricted to users with the "SUPER" role. The SecurityService is likely used to check the user's role.
* Pagination: The method accepts a Pageable object as a parameter, which allows for pagination of the results. This means the client can specify the page number and the number of results per page.
* Service Layer Call: The controller then calls the findAll method of the DealService.
* Repository Layer Call: The DealService (likely DealServiceImpl.java) calls the findAll method of the DealRepository.
* Data Retrieval: The DealRepository interacts with the database (MongoDB, based on the @Document annotation on the Deal class) to fetch all deals.
* Pagination Applied: The results are paginated based on the Pageable object passed from the controller.
* Return Results: The paginated list of Deal objects is returned from the repository to the service, and then from the service to the controller.
* Response: The controller returns the list of Deal objects as the response to the client. The @ApiResponse annotation indicates that a successful response (200) will contain a JSON array of Deal objects.

In essence, the flow is: **Controller** -> **Service** -> **Repository** -> **Database** ----> code again  **Repository** -> **Service** -> **Controller** -> Response.

The **Deal** class defines the structure of a deal object, including fields like id, postedBy, store, dealScore, category, title, description, prices, URLs, status, and views. The **DealEntityCallbacks** class seems to handle setting the postedBy field before saving a deal. The **DealRepository** provides methods for accessing and manipulating deal data in the database, with caching enabled for some operations. The **DealService** acts as an intermediary between the controller and the repository, providing business logic and calling the appropriate repository methods.

The GET **/deals** endpoint returns a list of Deal objects. Looking at the Deal class definition [1], there is a field called photos which is a List<String>. This field is described as "Deal photo URLs".

Therefore, the response for the GET /deals endpoint includes an array of image URLs (represented as strings) within each Deal object. It does not return actual images, but rather the URLs pointing to where the images are stored.


Based on the **@SecurityRequirement(name = "bearerAuth")** annotation on the getDeals method in **DealController.java** and the @IsSuper annotation, this endpoint is not public in general.

Here's why:
* @SecurityRequirement(name = "bearerAuth"): This annotation indicates that the endpoint requires authentication using a bearer token. This means that a client needs to provide a valid JWT or other form of bearer token in the Authorization header of the request. Public endpoints typically do not require any form of authentication.
* @IsSuper: This is a custom annotation that, based on its name and common usage in Spring Security, likely restricts access to users with a specific role or authority, in this case, likely a "SUPER" role. This further limits access beyond just being authenticated.
Therefore, this method is personal in the sense that it's not accessible to anyone without proper authentication and authorization. Only users who are authenticated with a valid bearer token and possess the "SUPER" role can access this endpoint to retrieve all deals.