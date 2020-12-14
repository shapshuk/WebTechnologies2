package com.restraunt.shapshuk.command.order;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.orderitem.service.OrderItemService;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class DeleteOrderItemCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DeleteOrderItemCommand.class.getName());

    private final OrderItemService orderItemService;

    public DeleteOrderItemCommand(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String itemIdString = request.getParameter(CommonAppConstants.ORDER_ITEM_ID_JSP_PARAM);
        LOGGER.info(String.format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, CommonAppConstants.ORDER_ITEM_ID_JSP_PARAM, itemIdString));

        orderItemService.deleteById(parseLong(itemIdString));

        String message = "Item has been deleted from order";
        request.setAttribute(CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.MESSAGE_JSP_ATTRIBUTE, message));

        return JspName.ORDER_ITEM_LIST_JSP;
    }

}
