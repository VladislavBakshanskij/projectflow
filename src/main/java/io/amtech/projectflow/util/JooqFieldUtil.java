package io.amtech.projectflow.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.impl.TableImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JooqFieldUtil {
    public static OrderField<?> findOrderFieldInTableOrDefault(final TableImpl<Record> table,
                                                               final String order,
                                                               final Field<?> defaultField) {
        final boolean desc = order.startsWith("-");
        final String orderFieldName = order.substring(1);
        return table.fieldStream()
                .filter(field -> JooqFieldUtil.getFieldName(field).equals(orderFieldName))
                .findFirst()
                .map(field -> desc ? field.desc() : field)
                .orElse(defaultField);
    }

    public static String getFieldName(Field<?> field) {
        return field.getQualifiedName().getName()[1];
    }
}
