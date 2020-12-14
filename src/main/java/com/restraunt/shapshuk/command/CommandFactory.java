package com.restraunt.shapshuk.command;

public interface CommandFactory {

    Command getCommand(String commandName);

    void registerCommand(CommandType commandName, Command command);
}