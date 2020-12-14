package com.restraunt.shapshuk.context;

import com.restraunt.shapshuk.command.CommandType;
import com.restraunt.shapshuk.entity.role.model.UserRole;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

public class SecurityContext {

    private static final Logger LOGGER = Logger.getLogger(SecurityContext.class.getName());

    private static final SecurityContext SECURITY_CONTEXT = new SecurityContext();
    private final Map<String, User> userMap = new ConcurrentHashMap<>(1000);
    private Properties properties = new Properties();

    public static SecurityContext getInstance() {
        return SECURITY_CONTEXT;
    }

    public void initialize(ServletContext servletContext) {

        try (InputStream propertyStream = SecurityContext.class.getResourceAsStream("/security.properties")) {

            properties.load(propertyStream);
            LOGGER.info("Security properties have been loaded successfully");
        } catch (IOException e) {

            LOGGER.error("Failed to read security properties" + e.getMessage());
            throw new IllegalStateException("Failed to read security properties", e);
        }
    }

    public void login(User user, String sessionId) {

        userMap.put(sessionId, user);
    }

    void logout(String sessionId) {

        userMap.remove(sessionId);
    }

    public User getCurrentUser(String sessionId) {

        if (nonNull(sessionId)) {
            return userMap.get(sessionId);
        } else {
            LOGGER.error("session Id is NULL, haven't got any user");
            return null;
        }

    }

    public boolean canExecute(CommandType commandType, String sessionId) {

        User currentUser = getCurrentUser(sessionId);
        return canExecute(currentUser, commandType);
    }

    private boolean canExecute(User user, CommandType commandType) {

        String commandToRoles = properties.getProperty(CommonAppConstants.COMMAND_SECURITY_PROPERTY + commandType.name());
        List<String> roles = Optional.ofNullable(commandToRoles)
                .map(s -> Arrays.asList(s.split(",")))
                .orElseGet(ArrayList::new);
        return (user != null && rolesMatch(roles, user)) || roles.isEmpty();
    }

    private boolean rolesMatch(List<String> roles, User user) {

        for (String role : roles) {
            for (UserRole userRole : user.getRoles()) {
                if (role.equalsIgnoreCase(userRole.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isUserLoggedIn(HttpServletRequest req) {

        return getCurrentUser(req.getSession().getId()) != null;
    }

    public void setSecurityAttributes(HttpServletRequest req) {

        req.setAttribute(CommonAppConstants.SESSION_ID_JSP_PARAM, req.getSession().getId());
        req.setAttribute(CommonAppConstants.USER_LOGGED_IN_JSP_PARAM, isUserLoggedIn(req));
        LOGGER.info("Security attributes have been set");
    }
}
