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
public class ProjectComment extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>pf.project_comment</code>
     */
    public static final ProjectComment PROJECT_COMMENT = new ProjectComment();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>pf.project_comment.id</code>.
     */
    public final TableField<Record, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.project_comment.project_id</code>.
     */
    public final TableField<Record, UUID> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>pf.project_comment.message</code>.
     */
    public final TableField<Record, String> MESSAGE = createField(DSL.name("message"), SQLDataType.VARCHAR(5000).nullable(false), this, "");

    /**
     * The column <code>pf.project_comment.create_date</code>.
     */
    public final TableField<Record, OffsetDateTime> CREATE_DATE = createField(DSL.name("create_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>pf.project_comment.login</code>.
     */
    public final TableField<Record, String> LOGIN = createField(DSL.name("login"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    private ProjectComment(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private ProjectComment(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>pf.project_comment</code> table reference
     */
    public ProjectComment(String alias) {
        this(DSL.name(alias), PROJECT_COMMENT);
    }

    /**
     * Create an aliased <code>pf.project_comment</code> table reference
     */
    public ProjectComment(Name alias) {
        this(alias, PROJECT_COMMENT);
    }

    /**
     * Create a <code>pf.project_comment</code> table reference
     */
    public ProjectComment() {
        this(DSL.name("project_comment"), null);
    }

    @Override
    public Schema getSchema() {
        return Pf.PF;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Internal.createUniqueKey(ProjectComment.PROJECT_COMMENT, DSL.name("project_comment_pkey"), new TableField[] { ProjectComment.PROJECT_COMMENT.ID }, true);
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(
              Internal.createUniqueKey(ProjectComment.PROJECT_COMMENT, DSL.name("project_comment_pkey"), new TableField[] { ProjectComment.PROJECT_COMMENT.ID }, true)
        );
    }

    @Override
    public ProjectComment as(String alias) {
        return new ProjectComment(DSL.name(alias), this);
    }

    @Override
    public ProjectComment as(Name alias) {
        return new ProjectComment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProjectComment rename(String name) {
        return new ProjectComment(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ProjectComment rename(Name name) {
        return new ProjectComment(name, null);
    }
}
