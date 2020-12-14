package com.restraunt.shapshuk.listener;

import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ApplicationListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ApplicationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        SecurityContext securityContext = SecurityContext.getInstance();
        securityContext.initialize(sce.getServletContext());
        LOGGER.info("Security context initialized");

        sce.getServletContext().setAttribute(CommonAppConstants.SECURITY_CONTEXT_ATTRIBUTE, securityContext);

        ApplicationContext.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            ApplicationContext.getInstance().destroy();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
