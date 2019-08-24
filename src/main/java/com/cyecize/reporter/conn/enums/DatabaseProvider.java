package com.cyecize.reporter.conn.enums;

import com.cyecize.reporter.conn.utils.MySqlConnectionUtils;
import com.cyecize.reporter.conn.utils.SqlConnectionUtils;
import com.cyecize.reporter.conn.utils.SqlServerConnectionUtils;

public enum DatabaseProvider {
    MY_SQL(new MySqlConnectionUtils()), MS_SQL_SERVER(new SqlServerConnectionUtils());

    private final SqlConnectionUtils connectionUtils;

    DatabaseProvider(SqlConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    public SqlConnectionUtils getConnectionUtils() {
        return this.connectionUtils;
    }
}
