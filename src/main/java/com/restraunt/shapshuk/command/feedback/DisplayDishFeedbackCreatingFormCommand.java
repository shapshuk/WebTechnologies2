package com.restraunt.shapshuk.command.feedback;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.util.JspUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static com.restraunt.shapshuk.core.constants.JspName.CREATE_DISH_FEEDBACK_FORM_JSP;

public class DisplayDishFeedbackCreatingFormCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DisplayDishFeedbackCreatingFormCommand.class.getName());

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        JspUtil jspUtil = ApplicationContext.getInstance().getBean(JspUtil.class);
        jspUtil.setDishAttributeByDishIdParam(request);
        LOGGER.info("Command have been processed");

        return CREATE_DISH_FEEDBACK_FORM_JSP;
    }

}
