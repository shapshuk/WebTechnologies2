package com.restraunt.shapshuk.command;

import com.restraunt.shapshuk.core.constants.JspName;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class CommandFactoryImpl implements CommandFactory {

    private static final Logger LOGGER = Logger.getLogger(CommandFactoryImpl.class.getName());

    private static final Map<CommandType, Command> commands = new ConcurrentHashMap<>(CommandType.values().length);

    @Override
    public void registerCommand(CommandType commandType, Command command) {

        commands.put(commandType, command);
        LOGGER.info(format("Command [%s] has been registered", commandType));
    }

    @Override
    public Command getCommand(String commandParam) {

        LOGGER.info(format("Get command from param: [%s]", commandParam));
        return CommandType.of(commandParam)
                .map(commands::get)
                .orElse(((request, response) -> JspName.DEFAULT_JSP));
    }
}
