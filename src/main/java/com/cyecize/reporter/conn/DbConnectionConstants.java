package com.cyecize.reporter.conn;

public class DbConnectionConstants {

    public static final String DB_CONNECT_ROUTE = "/database/connect";

    public static final String LOAD_STORED_DB_CREDENTIALS_PARAM_NAME = "loadStoredCredentials";

    public static final String DB_UNIQUE_TABLE_NAME = "reporter_unique_table_metadata";

    public static final String DB_UNIQUE_TABLE_VERSION_COLUMN_NAME = "version";

    public static final int USER_CONNECTION_MAX_INACTIVE_TIME_MILLIS = 7200000; //2h
}
