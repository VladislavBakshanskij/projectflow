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
import org.jooq.JSONB;
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
public class ProjectJournal extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>pf.project_journal</code>
     */
    public static final ProjectJournal PROJECT_JOURNAL = new ProjectJournal();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>pf.project_journal.id</code>.
     */
    public final TableField<Record, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.project_journal.project_id</code>.
     */
    public final TableField<Record, UUID> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.project_journal.login</code>.
     */
    public final TableField<Record, String> LOGIN = createField(DSL.name("login"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>pf.project_journal.update_date</code>.
     */
    public final TableField<Record, OffsetDateTime> UPDATE_DATE = createField(DSL.name("update_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>pf.project_journal.current_state</code>.
     */
    public final TableField<Record, JSONB> CURRENT_STATE = createField(DSL.name("current_state"), SQLDataType.JSONB.nullable(false), this, "");

    private ProjectJournal(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private ProjectJournal(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>pf.project_journal</code> table reference
     */
    public ProjectJournal(String alias) {
        this(DSL.name(alias), PROJECT_JOURNAL);
    }

    /**
     * Create an aliased <code>pf.project_journal</code> table reference
     */
    public ProjectJournal(Name alias) {
        this(alias, PROJECT_JOURNAL);
    }

    /**
     * Create a <code>pf.project_journal</code> table reference
     */
    public ProjectJournal() {
        this(DSL.name("project_journal"), null);
    }

    @Override
    public Schema getSchema() {
        return Pf.PF;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Internal.createUniqueKey(ProjectJournal.PROJECT_JOURNAL, DSL.name("project_journal_pkey"), new TableField[] { ProjectJournal.PROJECT_JOURNAL.ID }, true);
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(
              Internal.createUniqueKey(ProjectJournal.PROJECT_JOURNAL, DSL.name("project_journal_pkey"), new TableField[] { ProjectJournal.PROJECT_JOURNAL.ID }, true)
        );
    }

    @Override
    public ProjectJournal as(String alias) {
        return new ProjectJournal(DSL.name(alias), this);
    }

    @Override
    public ProjectJournal as(Name alias) {
        return new ProjectJournal(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProjectJournal rename(String name) {
        return new ProjectJournal(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ProjectJournal rename(Name name) {
        return new ProjectJournal(name, null);
    }
}
