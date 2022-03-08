package io.amtech.projectflow.app;

import java.util.Map;
import java.util.Optional;

public class SearchCriteria {
    public static final int MAX_LIMIT = 100;

    private final int limit;
    private final int offset;
    private final Map<String, String> filters;
    private final String order;

    public SearchCriteria(final int limit,
                          final int offset,
                          final Map<String, String> filters,
                          final String order) {
        this.limit = limit;
        this.offset = offset;
        this.filters = filters;
        this.order = order;
    }

    public int getLimit() {
        if (limit == 0 || limit > MAX_LIMIT) {
            return MAX_LIMIT;
        }

        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public String getOrder() {
        return order;
    }

    public Optional<String> getFilter(final String filterName) {
        return Optional.ofNullable(filters.get(filterName));
    }

    public Optional<String> getCriteriaValue(final String criteriaName) {
        return Optional.ofNullable(filters.get(criteriaName));
    }
}
