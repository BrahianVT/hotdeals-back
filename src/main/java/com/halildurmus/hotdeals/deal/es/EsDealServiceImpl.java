package com.halildurmus.hotdeals.deal.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halildurmus.hotdeals.deal.DealRepository;
import com.halildurmus.hotdeals.deal.DealSearchParams;
import com.halildurmus.hotdeals.deal.PriceRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EsDealServiceImpl implements EsDealService {

  private static final int MAX_SUGGESTION = 5;
  private static final String DEAL_INDEX = "deal";

  @Autowired private DealRepository dealRepository;
  @Autowired private EsDealRepository repository;
  @Autowired private ElasticsearchClient esClient;
  @Autowired private ObjectMapper objectMapper;

  @Override
  public Page<EsDeal> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  private MultiMatchQuery createAutocompleteQuery(String query) {
    return MultiMatchQuery.of(m -> m
            .query(query)
            .fields("title", "title._2gram", "title._3gram")
            .type(TextQueryType.BoolPrefix)
    );
  }

  @Override
  public JsonNode getSuggestions(String query) {
    try {
      SearchRequest request = SearchRequest.of(r -> r
              .index(DEAL_INDEX)
              .size(MAX_SUGGESTION)
              .query(q -> q.multiMatch(createAutocompleteQuery(query)))
              .source(s -> s.filter(f -> f.includes("title")))
      );

      SearchResponse<Object> response = esClient.search(request, Object.class);
      return objectMapper.valueToTree(response.hits().hits());
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private NestedQuery createStringFacetFilter(String facetName, String facetValue) {
    String facetGroup = "stringFacets";
    return NestedQuery.of(n -> n
            .path(facetGroup)
            .scoreMode(ChildScoreMode.Avg)
            .query(q -> q
                    .bool(b -> b
                            .must(List.of(
                                    Query.of(q1 -> q1.term(t -> t.field(facetGroup + ".facetName").value(facetName))),
                                    Query.of(q2 -> q2.term(t -> t.field(facetGroup + ".facetValue").value(facetValue)))
                            ))
                    )
            )
    );
  }

  private NestedQuery createNumberFacetFilter(String facetName, Double from, Double to) {
    String facetGroup = "numberFacets";

    List<Query> queries = new ArrayList<>();
    queries.add(Query.of(q -> q.term(t -> t.field(facetGroup + ".facetName").value(facetName))));

    RangeQuery.Builder rangeBuilder = new RangeQuery.Builder()
            .field(facetGroup + ".facetValue")
            .gte(JsonData.of(from));

    if (to != null) {
      rangeBuilder.lt(JsonData.of(to));
    }

    queries.add(Query.of(q -> q.range(rangeBuilder.build())));

    return NestedQuery.of(n -> n
            .path(facetGroup)
            .scoreMode(ChildScoreMode.Avg)
            .query(q -> q
                    .bool(b -> b
                            .must(queries)
                    )
            )
    );
  }

  private co.elastic.clients.elasticsearch._types.SortOptions createCreatedAtSort(SortOrder sortOrder) {
    return co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
            .field(f -> f
                    .field("createdAt")
                    .order(sortOrder)
            )
    );
  }

  private co.elastic.clients.elasticsearch._types.SortOptions createPriceSort(SortOrder sortOrder) {
    String nestedPath = "numberFacets";
    String fieldName = "numberFacets.facetValue";

    return co.elastic.clients.elasticsearch._types.SortOptions.of(s -> s
            .field(f -> f
                    .field(fieldName)
                    .order(sortOrder)
                    .nested(n -> n
                            .path(nestedPath)
                            .filter(fi -> fi
                                    .term(t -> t
                                            .field(nestedPath + ".facetName")
                                            .value("price")
                                    )
                            )
                    )
            )
    );
  }

  private List<Query> createCategoryFilters(List<String> categories) {
    List<Query> queries = new ArrayList<>();

    if (categories.size() > 1) {
      List<Query> shouldQueries = new ArrayList<>();
      for (String category : categories) {
        shouldQueries.add(Query.of(q -> q.nested(createStringFacetFilter("category", category))));
      }

      queries.add(Query.of(q -> q.bool(b -> b.should(shouldQueries))));
    } else {
      queries.add(Query.of(q -> q.nested(createStringFacetFilter("category", categories.get(0)))));
    }

    return queries;
  }

  private List<Query> createPriceFilters(List<PriceRange> priceRanges) {
    List<Query> queries = new ArrayList<>();

    List<Query> shouldQueries = new ArrayList<>();
    for (PriceRange pr : priceRanges) {
      shouldQueries.add(Query.of(q -> q.nested(createNumberFacetFilter("price", pr.getFrom(), pr.getTo()))));
    }

    queries.add(Query.of(q -> q.bool(b -> b.should(shouldQueries))));
    return queries;
  }

  private List<Query> createStoreFilters(List<String> stores) {
    List<Query> queries = new ArrayList<>();

    if (stores.size() > 1) {
      List<Query> shouldQueries = new ArrayList<>();
      for (String store : stores) {
        shouldQueries.add(Query.of(q -> q.nested(createStringFacetFilter("store", store))));
      }

      queries.add(Query.of(q -> q.bool(b -> b.should(shouldQueries))));
    } else {
      queries.add(Query.of(q -> q.nested(createStringFacetFilter("store", stores.get(0)))));
    }

    return queries;
  }

  private MultiMatchQuery createMultiMatchQuery(String query) {
    return MultiMatchQuery.of(m -> m
            .query(query)
            .fields("title", "description")
    );
  }

  private TermQuery createTermQuery() {
    return TermQuery.of(t -> t
            .field("status")
            .value("ACTIVE")
    );
  }

  private BoolQuery createBoolQuery(Boolean hideExpired, String query) {
    BoolQuery.Builder boolQuery = new BoolQuery.Builder();

    if (hideExpired) {
      boolQuery.filter(Query.of(q -> q.term(createTermQuery())));
    }

    boolQuery.must(Query.of(q -> q.multiMatch(createMultiMatchQuery(query))));

    return boolQuery.build();
  }

  private BoolQuery createFilters(DealSearchParams searchParams, String filterToBeExcluded) {
    BoolQuery.Builder boolQuery = new BoolQuery.Builder();

    if (searchParams.getCategories() == null
            && searchParams.getPrices() == null
            && searchParams.getStores() == null) {
      return boolQuery.build();
    }

    List<Query> filters = new ArrayList<>();

    if (searchParams.getCategories() != null && !Objects.equals(filterToBeExcluded, "category")) {
      filters.addAll(createCategoryFilters(searchParams.getCategories()));
    }

    if (searchParams.getPrices() != null && !Objects.equals(filterToBeExcluded, "price")) {
      filters.addAll(createPriceFilters(searchParams.getPrices()));
    }

    if (searchParams.getStores() != null && !Objects.equals(filterToBeExcluded, "store")) {
      filters.addAll(createStoreFilters(searchParams.getStores()));
    }

    for (Query filter : filters) {
      boolQuery.filter(filter);
    }

    return boolQuery.build();
  }

  private Aggregation createAllFiltersSubAgg(String facetGroup) {
    return Aggregation.of(a -> a
            .nested(n -> n.path(facetGroup))
            .aggregations(new HashMap<String, Aggregation>() {{ // Explicitly create map for aggregations
              put("names", Aggregation.of(namesAgg -> namesAgg
                      .terms(t -> t.field(facetGroup + ".facetName"))
                      .aggregations(new HashMap<String, Aggregation>() {{
                        put("values", Aggregation.of(valuesAgg -> valuesAgg
                                .terms(t -> t.field(facetGroup + ".facetValue"))));
                      }})));
            }}));
  }

  private Aggregation createAllFiltersAgg(DealSearchParams searchParams) {
    return Aggregation.of(a -> a
            .filter(f -> f.bool(createFilters(searchParams, null)))
            .aggregations(new HashMap<String, Aggregation>() {{
              put("stringFacets", createAllFiltersSubAgg("stringFacets"));
              put("numberFacets", createAllFiltersSubAgg("numberFacets"));
            }}));
  }

  private Aggregation createFilterAgg(String fieldName, String facetName) {
    return Aggregation.of(a -> a
            .filter(f -> f.match(m -> m.field(fieldName).query(facetName))));
  }

  private Aggregation createNestedSubAgg(String facetGroup, String facetName) {
    return Aggregation.of(a -> a
            .nested(n -> n.path(facetGroup))
            .aggregations(new HashMap<String, Aggregation>() {{
              put("aggSpecial", Aggregation.of(specialAgg -> specialAgg
                      .filter(f -> f.term(t -> t.field(facetGroup + ".facetName").value(facetName)))
                      .aggregations(new HashMap<String, Aggregation>() {{
                        put("names", Aggregation.of(namesAgg -> namesAgg
                                .terms(t -> t.field(facetGroup + ".facetName"))
                                .aggregations(new HashMap<String, Aggregation>() {{
                                  put("values", Aggregation.of(valuesAgg -> valuesAgg
                                          .terms(t -> t.field(facetGroup + ".facetValue"))));
                                }})));
                      }})));
            }}));
  }

  private Aggregation createCategoryAgg(DealSearchParams searchParams) {
    return Aggregation.of(a -> a
            .filter(f -> f.bool(createFilters(searchParams, "category")))
            .aggregations(new HashMap<String, Aggregation>() {{
              put("stringFacets", createNestedSubAgg("stringFacets", "category"));
            }}));
  }

  private Aggregation createStoreAgg(DealSearchParams searchParams) {
    return Aggregation.of(a -> a
            .filter(f -> f.bool(createFilters(searchParams, "store")))
            .aggregations(new HashMap<String, Aggregation>() {{
              put("stringFacets", createNestedSubAgg("stringFacets", "store"));
            }}));
  }

  private Aggregation createPriceAgg(DealSearchParams searchParams) {
    // Create the ranges using AggregationRange
    List<AggregationRange> ranges = new ArrayList<>();
    ranges.add(AggregationRange.of(r -> r.from(0.0).to(1.0)));
    ranges.add(AggregationRange.of(r -> r.from(1.0).to(5.0)));
    ranges.add(AggregationRange.of(r -> r.from(5.0).to(10.0)));
    ranges.add(AggregationRange.of(r -> r.from(10.0).to(20.0)));
    ranges.add(AggregationRange.of(r -> r.from(20.0).to(50.0)));
    ranges.add(AggregationRange.of(r -> r.from(50.0).to(100.0)));
    ranges.add(AggregationRange.of(r -> r.from(100.0).to(250.0)));
    ranges.add(AggregationRange.of(r -> r.from(250.0).to(500.0)));
    ranges.add(AggregationRange.of(r -> r.from(500.0).to(1000.0)));
    ranges.add(AggregationRange.of(r -> r.from(1000.0).to(1500.0)));
    ranges.add(AggregationRange.of(r -> r.from(1500.0).to(2000.0)));
    ranges.add(AggregationRange.of(r -> r.from(2000.0)));

    return Aggregation.of(a -> a
            .filter(f -> f.bool(createFilters(searchParams, "price")))
            .aggregations(new HashMap<String, Aggregation>() {{
              put("numberFacets", Aggregation.of(numberFacetsAgg -> numberFacetsAgg
                      .nested(n -> n.path("numberFacets"))
                      .aggregations(new HashMap<String, Aggregation>() {{
                        put("aggSpecial", Aggregation.of(specialAgg -> specialAgg
                                .filter(f -> f.term(t -> t.field("numberFacets.facetName").value("price")))
                                .aggregations(new HashMap<String, Aggregation>() {{
                                  put("names", Aggregation.of(namesAgg -> namesAgg
                                          .terms(t -> t.field("numberFacets.facetName"))
                                          .aggregations(new HashMap<String, Aggregation>() {{
                                            put("values", Aggregation.of(valuesAgg -> valuesAgg
                                                    .range(r -> r
                                                            .field("numberFacets.facetValue")
                                                            .ranges(ranges))));
                                          }})));
                                }})));
                      }})));
            }}));
  }

  private List<Aggregation> createAggregations(DealSearchParams searchParams) {
    List<Aggregation> aggregations = new ArrayList<>();
    aggregations.add(createAllFiltersAgg(searchParams));
    aggregations.add(createCategoryAgg(searchParams));
    aggregations.add(createPriceAgg(searchParams));
    aggregations.add(createStoreAgg(searchParams));
    return aggregations;
  }

  @Override
  public JsonNode searchDeals(DealSearchParams searchParams, Pageable pageable) {
    try {
      // Build the search request using the new API
      SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
              .index(DEAL_INDEX)
              .from(pageable.getPageNumber())
              .size(pageable.getPageSize());

      // Add sort if specified
      if (searchParams.getSortBy() != null) {
        SortOrder order = searchParams.getOrder().equals("asc") ? SortOrder.Asc : SortOrder.Desc;
        if (searchParams.getSortBy().equals("createdAt")) {
          requestBuilder.sort(createCreatedAtSort(order));
        } else {
          requestBuilder.sort(createPriceSort(order));
        }
      }

      // Add query if specified
      if (!ObjectUtils.isEmpty(searchParams.getQuery())) {
        requestBuilder.query(q -> q.bool(createBoolQuery(searchParams.getHideExpired(), searchParams.getQuery())));
      }

      // Add aggregations
      Map<String, Aggregation> aggsMap = new HashMap<>();
      int i = 0;
      for (Aggregation agg : createAggregations(searchParams)) {
        // Use meaningful names for the aggregations
        String name;
        switch (i) {
          case 0:
            name = "aggAllFilters";
            break;
          case 1:
            name = "aggCategory";
            break;
          case 2:
            name = "aggPrice";
            break;
          case 3:
            name = "aggStore";
            break;
          default:
            name = "agg" + i;
        }
        aggsMap.put(name, agg);
        i++;
      }
      requestBuilder.aggregations(aggsMap);

      // Add post filter
      requestBuilder.postFilter(pf -> pf.bool(createFilters(searchParams, null)));

      SearchResponse<Object> response = esClient.search(requestBuilder.build(), Object.class);
      return objectMapper.valueToTree(response);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }


  @Override
  public EsDeal save(EsDeal esDeal) {
    return repository.save(esDeal);
  }
}