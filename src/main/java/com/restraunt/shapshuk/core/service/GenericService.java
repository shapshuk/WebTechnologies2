package com.restraunt.shapshuk.core.service;

import com.restraunt.shapshuk.database.connection.ConnectionException;

import java.sql.SQLException;
import java.util.List;

public interface GenericService<T> {

    Long save(T entity) throws SQLException, ConnectionException;

    void update(T entity) throws SQLException, ConnectionException;

    void deleteById(Long id) throws SQLException, ConnectionException;

    void delete(T entity) throws SQLException, ConnectionException;

    T getById(Long id) throws SQLException, ConnectionException;

    List<T> findAll() throws SQLException, ConnectionException;

    Integer getNumberOfRows() throws ConnectionException, SQLException;
}
