package io.amtech.projectflow.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.impl.TableImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JooqFieldUtil {
    public static OrderField<?> findOrderFieldInTableOrDefault(final TableImpl<Record> table,
                                                               final String order,
                                                               final Field<?> defaultField) {
        final String orderField = StringUtils.defaultIfBlank(order, StringUtils.EMPTY);
        final String orderFieldName = orderField.equals(StringUtils.EMPTY) ? orderField : orderField.substring(1);
        final boolean desc = orderField.startsWith("-");
        return table.fieldStream()
                .filter(field -> getFieldName(field).equals(orderFieldName))
                .findFirst()
                .map(field -> desc ? field.desc() : field)
                .orElse(defaultField);
    }

    private static String getFieldName(Field<?> field) {
        return field.getQualifiedName().getName()[1];
    }
}
