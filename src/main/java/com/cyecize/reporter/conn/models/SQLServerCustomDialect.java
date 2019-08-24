package com.cyecize.reporter.conn.models;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class SQLServerCustomDialect extends SQLServerDialect {

    public SQLServerCustomDialect() {
        super();
        this.registerColumnType(Types.CHAR, "nchar(1)");
        this.registerColumnType(Types.LONGVARCHAR, "nvarchar(max)");
        this.registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
        this.registerColumnType(Types.VARCHAR, "nvarchar(max)");
        this.registerColumnType(Types.CLOB, "nvarchar(max)");

        this.registerColumnType(Types.NCHAR, "nchar(1)");
        this.registerColumnType(Types.LONGNVARCHAR, "nvarchar(max)");
        this.registerColumnType(Types.NVARCHAR, 4000, "nvarchar($l)");
        this.registerColumnType(Types.NVARCHAR, "nvarchar(max)");
        this.registerColumnType(Types.NCLOB, "nvarchar(max)");

        this.registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        this.registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        this.registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        this.registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName());
    }

}
