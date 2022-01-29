/*
 * This file is generated by jOOQ.
 */
package io.amtech.projectflow.jooq.tables;


import io.amtech.projectflow.jooq.Pf;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Check;
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
public class Milestone extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>pf.milestone</code>
     */
    public static final Milestone MILESTONE = new Milestone();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>pf.milestone.id</code>.
     */
    public final TableField<Record, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.milestone.project_id</code>.
     */
    public final TableField<Record, UUID> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.milestone.name</code>.
     */
    public final TableField<Record, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>pf.milestone.description</code>.
     */
    public final TableField<Record, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(2048), this, "");

    /**
     * The column <code>pf.milestone.planned_start_date</code>.
     */
    public final TableField<Record, OffsetDateTime> PLANNED_START_DATE = createField(DSL.name("planned_start_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>pf.milestone.planned_finish_date</code>.
     */
    public final TableField<Record, OffsetDateTime> PLANNED_FINISH_DATE = createField(DSL.name("planned_finish_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>pf.milestone.fact_start_date</code>.
     */
    public final TableField<Record, OffsetDateTime> FACT_START_DATE = createField(DSL.name("fact_start_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>pf.milestone.fact_finish_date</code>.
     */
    public final TableField<Record, OffsetDateTime> FACT_FINISH_DATE = createField(DSL.name("fact_finish_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>pf.milestone.progress_percent</code>.
     */
    public final TableField<Record, Short> PROGRESS_PERCENT = createField(DSL.name("progress_percent"), SQLDataType.SMALLINT.nullable(false).defaultValue(DSL.field("0", SQLDataType.SMALLINT)), this, "");

    private Milestone(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Milestone(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>pf.milestone</code> table reference
     */
    public Milestone(String alias) {
        this(DSL.name(alias), MILESTONE);
    }

    /**
     * Create an aliased <code>pf.milestone</code> table reference
     */
    public Milestone(Name alias) {
        this(alias, MILESTONE);
    }

    /**
     * Create a <code>pf.milestone</code> table reference
     */
    public Milestone() {
        this(DSL.name("milestone"), null);
    }

    @Override
    public Schema getSchema() {
        return Pf.PF;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Internal.createUniqueKey(Milestone.MILESTONE, DSL.name("milestone_pkey"), new TableField[] { Milestone.MILESTONE.ID }, true);
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(
              Internal.createUniqueKey(Milestone.MILESTONE, DSL.name("milestone_pkey"), new TableField[] { Milestone.MILESTONE.ID }, true)
        );
    }

    @Override
    public List<Check<Record>> getChecks() {
        return Arrays.<Check<Record>>asList(
              Internal.createCheck(this, DSL.name("milestone_progress_percent_check"), "(((progress_percent >= 0) AND (progress_percent <= 100)))", true)
        );
    }

    @Override
    public Milestone as(String alias) {
        return new Milestone(DSL.name(alias), this);
    }

    @Override
    public Milestone as(Name alias) {
        return new Milestone(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Milestone rename(String name) {
        return new Milestone(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Milestone rename(Name name) {
        return new Milestone(name, null);
    }
}
