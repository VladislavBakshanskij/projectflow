/*
 * This file is generated by jOOQ.
 */
package io.amtech.projectflow.jooq.tables;


import io.amtech.projectflow.jooq.Pf;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NotificationHistory extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>pf.notification_history</code>
     */
    public static final NotificationHistory NOTIFICATION_HISTORY = new NotificationHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>pf.notification_history.id</code>.
     */
    public final TableField<Record, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.notification_history.notification_id</code>.
     */
    public final TableField<Record, UUID> NOTIFICATION_ID = createField(DSL.name("notification_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.notification_history.error</code>.
     */
    public final TableField<Record, String> ERROR = createField(DSL.name("error"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>pf.notification_history.create_date</code>.
     */
    public final TableField<Record, OffsetDateTime> CREATE_DATE = createField(DSL.name("create_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private NotificationHistory(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private NotificationHistory(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>pf.notification_history</code> table reference
     */
    public NotificationHistory(String alias) {
        this(DSL.name(alias), NOTIFICATION_HISTORY);
    }

    /**
     * Create an aliased <code>pf.notification_history</code> table reference
     */
    public NotificationHistory(Name alias) {
        this(alias, NOTIFICATION_HISTORY);
    }

    /**
     * Create a <code>pf.notification_history</code> table reference
     */
    public NotificationHistory() {
        this(DSL.name("notification_history"), null);
    }

    @Override
    public Schema getSchema() {
        return Pf.PF;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Internal.createUniqueKey(NotificationHistory.NOTIFICATION_HISTORY, DSL.name("notification_history_pkey"), new TableField[] { NotificationHistory.NOTIFICATION_HISTORY.ID }, true);
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(
              Internal.createUniqueKey(NotificationHistory.NOTIFICATION_HISTORY, DSL.name("notification_history_pkey"), new TableField[] { NotificationHistory.NOTIFICATION_HISTORY.ID }, true)
        );
    }

    @Override
    public NotificationHistory as(String alias) {
        return new NotificationHistory(DSL.name(alias), this);
    }

    @Override
    public NotificationHistory as(Name alias) {
        return new NotificationHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public NotificationHistory rename(String name) {
        return new NotificationHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NotificationHistory rename(Name name) {
        return new NotificationHistory(name, null);
    }
}
