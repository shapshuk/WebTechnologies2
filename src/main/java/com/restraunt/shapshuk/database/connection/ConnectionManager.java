package com.restraunt.shapshuk.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {

    Connection getConnection() throws SQLException, ConnectionException;
}
