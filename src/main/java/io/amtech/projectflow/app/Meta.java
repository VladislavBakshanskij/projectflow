package io.amtech.projectflow.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Meta {
    private long limit;
    private long offset;
    private long totalPages;
}
