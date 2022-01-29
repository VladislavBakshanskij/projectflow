package io.amtech.projectflow.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class PagedData<T> {
    private long limit;
    private long offset;
    private List<T> data;

    public <R> PagedData<R> map(Function<T, R> mapper) {
        return new PagedData<R>()
                .setLimit(limit)
                .setOffset(offset)
                .setData(data.stream().map(mapper).collect(Collectors.toList()));
    }
}
