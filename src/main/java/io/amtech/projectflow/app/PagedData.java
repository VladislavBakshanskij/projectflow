package io.amtech.projectflow.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class PagedData<T> {
    private Meta meta;
    private List<T> data;

    public <R> PagedData<R> map(Function<T, R> mapper) {
        return new PagedData<R>()
                .setMeta(meta)
                .setData(data.stream().map(mapper).collect(Collectors.toList()));
    }
}
