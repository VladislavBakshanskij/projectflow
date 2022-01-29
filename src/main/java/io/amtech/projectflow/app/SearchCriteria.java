package io.amtech.projectflow.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class SearchCriteria {
    private Map<String, String> criteria;
    private int limit;
    private int offset;
    private String order;

    public SearchCriteria addCriteria(final String name, final String value) {
        criteria.put(name, value);
        return this;
    }

    public Optional<String> getCriteriaValue(final String name) {
        return Optional.ofNullable(criteria.get(name));
    }
}
