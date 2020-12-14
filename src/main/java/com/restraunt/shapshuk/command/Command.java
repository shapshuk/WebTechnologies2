package com.restraunt.shapshuk.command;

import com.restraunt.shapshuk.database.connection.ConnectionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Command extends BiFunction<HttpServletRequest, HttpServletResponse, String> {

    String process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ConnectionException;

    @Override
    default String apply(HttpServletRequest request, HttpServletResponse response) {

        try {
            return process(request, response);
        } catch (Exception e) {
            throw new CommandProcessException("Failed to execute command: " + this.getClass().getName(), e);
        }
    }
}
