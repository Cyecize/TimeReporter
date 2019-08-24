package com.cyecize.reporter.conn.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.cyecize.reporter.conn.DbConnectionConstants.*;

/**
 * Entity that will always have 1 row (id = 1, version = current app version).
 * Used to check if a database is compatible with the version of the current application.
 */
@Entity
@Table(name = DB_UNIQUE_TABLE_NAME)
public class ReporterUniqueEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column(name = DB_UNIQUE_TABLE_VERSION_COLUMN_NAME, nullable = false, unique = true, updatable = false)
    private Integer version;

    public ReporterUniqueEntity() {
        this.id = 1;
        this.version = 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
