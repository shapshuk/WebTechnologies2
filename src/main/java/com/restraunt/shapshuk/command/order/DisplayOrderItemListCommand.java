package com.restraunt.shapshuk.command.order;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.order.model.UserOrder;
import com.restraunt.shapshuk.entity.order.service.UserOrderService;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;
import com.restraunt.shapshuk.entity.orderitem.service.OrderItemService;
import com.restraunt.shapshuk.util.CategoryNameUtil;
import com.restraunt.shapshuk.util.JspUtil;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class DisplayOrderItemListCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(DisplayOrderItemListCommand.class.getName());

    private final UserOrderService userOrderService;
    private final OrderItemService orderItemService;

    public DisplayOrderItemListCommand(UserOrderService userOrderService, OrderItemService orderItemService) {
        this.userOrderService = userOrderService;
        this.orderItemService = orderItemService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String sessionId = request.getSession().getId();
        UserOrder currentOrder = userOrderService.getBuildingUpUserOrder(sessionId);

        JspUtil jspUtil = ApplicationContext.getInstance().getBean(JspUtil.class);
        jspUtil.setCategoriesAttribute(request);

        List<String> categoryNames = CategoryNameUtil.getCategoryNamesFromRequest(request);

        if (categoryNames.isEmpty() || categoryNames.get(0).equals(CategoryNameUtil.ALL_CATEGORIES)) {

            List<OrderItem> orderItems = orderItemService.findAllItemsByOrderId(currentOrder.getId());
            request.setAttribute(CommonAppConstants.ORDER_ITEM_LIST_JSP_ATTRIBUTE, orderItems);
            LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ORDER_ITEM_LIST_JSP_ATTRIBUTE, orderItems.toString()));
            LOGGER.info("All found items will be shown");

            return JspName.ORDER_ITEM_LIST_JSP;
        }

        List<OrderItem> filteredOrderItems = new ArrayList<>();
        for (String categoryName : categoryNames) {
            List<OrderItem> orderItems = orderItemService.getFromCurrentOrderByDishCategoryName(categoryName, currentOrder.getId());
            if (orderItems.isEmpty()) {
                continue;
            }
            filteredOrderItems.addAll(orderItems);
        }

        request.setAttribute(CommonAppConstants.ORDER_ITEM_LIST_JSP_ATTRIBUTE, filteredOrderItems);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.ORDER_ITEM_LIST_JSP_ATTRIBUTE, filteredOrderItems.toString()));
        LOGGER.info("Filtered items will be shown");

        return JspName.ORDER_ITEM_LIST_JSP;
    }

}
