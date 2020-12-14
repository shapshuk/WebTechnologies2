package com.restraunt.shapshuk.command.order;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.order.model.UserOrder;
import com.restraunt.shapshuk.entity.order.service.UserOrderService;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.entity.wallet.model.Wallet;
import com.restraunt.shapshuk.entity.wallet.service.WalletService;
import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.ValidationResult;
import com.restraunt.shapshuk.entity.order.constants.OrderStatus;
import com.restraunt.shapshuk.validation.CreateMessageUtil;
import com.restraunt.shapshuk.core.constants.JspName;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.restraunt.shapshuk.core.constants.CommonAppConstants.*;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

public class SubmitOrderCheckoutCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitOrderCheckoutCommand.class.getName());
    private static final String USERS_PARAM_TO_CURRENT_ORDER = "User's [%s] has been set to current order";
    private static final String CUSTOM_PARAM_TO_CURRENT_ORDER = "Custom [%s] has been set to current order";

    private final UserOrderService userOrderService;
    private final BeanValidator validator;
    private final WalletService walletService;

    public SubmitOrderCheckoutCommand(UserOrderService userOrderService, BeanValidator validator, WalletService walletService) {
        this.userOrderService = userOrderService;
        this.validator = validator;
        this.walletService = walletService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws SQLException, ConnectionException {

        String sessionId = request.getSession().getId();
        UserOrder currentOrder = userOrderService.getBuildingUpUserOrder(sessionId);
        User user = SecurityContext.getInstance().getCurrentUser(sessionId);

        String paymentMethod = request.getParameter(PAYMENT_METHOD_JSP_PARAM);
        LOGGER.info(format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, PAYMENT_METHOD_JSP_PARAM, paymentMethod));

        if (nonNull(paymentMethod) && paymentMethod.equals(PAYMENT_FROM_ACCOUNT_JSP_PARAM)) {

            if (isUserPossibleToPay(request, currentOrder, user)) {

                payFromAccount(currentOrder, user);
            } else {

                return JspName.ORDER_CHECKOUT_FORM_JSP;
            }
        }

        String customerName = request.getParameter(DEFAULT_USER_NAME_JSP_PARAM);
        LOGGER.info(format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, DEFAULT_USER_NAME_JSP_PARAM, customerName));

        String customerPhoneNumber = request.getParameter(DEFAULT_USER_PHONE_NUMBER_JSP_PARAM);
        LOGGER.info(format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, DEFAULT_USER_PHONE_NUMBER_JSP_PARAM, customerPhoneNumber));

        String customerUsersAddress = request.getParameter(DEFAULT_ORDER_ADDRESS_JSP_PARAM);
        LOGGER.info(format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, DEFAULT_ORDER_ADDRESS_JSP_PARAM, customerUsersAddress));

        if (nonNull(customerName)) {
            currentOrder.setCustomerName(user.getName());
            LOGGER.info(format(USERS_PARAM_TO_CURRENT_ORDER, "name"));
        } else {
            currentOrder.setCustomerName(request.getParameter(USER_NAME_JSP_PARAM));
            LOGGER.info(format(CUSTOM_PARAM_TO_CURRENT_ORDER, "name"));
        }

        if (nonNull(customerPhoneNumber)) {
            currentOrder.setCustomerPhoneNumber(user.getPhoneNumber());
            LOGGER.info(format(USERS_PARAM_TO_CURRENT_ORDER, "phone number"));
        } else {
            currentOrder.setCustomerPhoneNumber(request.getParameter(USER_PHONE_NUMBER_JSP_PARAM));
            LOGGER.info(format(CUSTOM_PARAM_TO_CURRENT_ORDER, "phone number"));
        }

        if (nonNull(customerUsersAddress)) {
            LOGGER.info(format(USERS_PARAM_TO_CURRENT_ORDER, "address"));
        } else {
            setCustomAddress(request, currentOrder);
            LOGGER.info(format(CUSTOM_PARAM_TO_CURRENT_ORDER, "address"));
        }

        setTimeOfDelivery(request, currentOrder);

        currentOrder.setOrderStatus(OrderStatus.SUBMITTED);
        LOGGER.info(String.format("Order status [%s] has been set to current order", OrderStatus.SUBMITTED));

        ValidationResult validationResult = validator.validate(currentOrder);
        List<BrokenField> brokenFields = validationResult.getBrokenFields();

        if (brokenFields.isEmpty()) {

            userOrderService.update(currentOrder);

            String message = "Order accepted";
            request.setAttribute(MESSAGE_JSP_ATTRIBUTE, message);
            LOGGER.info(format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, MESSAGE_JSP_ATTRIBUTE, message));

            return JspName.COMMAND_RESULT_MESSAGE_JSP;
        } else {

            List<String> messages = CreateMessageUtil.createPageMessageList(brokenFields);

            request.setAttribute(ERRORS_JSP_ATTRIBUTE, messages);
            LOGGER.error(messages);

            return JspName.ORDER_CHECKOUT_FORM_JSP;
        }

    }

    private void payFromAccount(UserOrder currentOrder, User user) throws ConnectionException, SQLException {

        BigDecimal orderCost = userOrderService.getOrderCost(currentOrder);
        BigDecimal usersWalletMoneyAmount = user.getWallet().getMoneyAmount();
        BigDecimal newMoneyAmount = usersWalletMoneyAmount.subtract(orderCost);
        user.getWallet().setMoneyAmount(newMoneyAmount);
        walletService.update(user.getWallet());
    }

    private boolean isUserPossibleToPay(HttpServletRequest request, UserOrder currentOrder, User user) throws ConnectionException, SQLException {

        BigDecimal totalOrderCost = userOrderService.getOrderCost(currentOrder);
        Wallet wallet = user.getWallet();

        if (totalOrderCost.compareTo(wallet.getMoneyAmount()) > 0) {

            String message = "Not enough money in account";
            request.setAttribute(ERRORS_JSP_ATTRIBUTE, Collections.singletonList(message));
            LOGGER.error(message);

            return false;
        }

        return true;
    }

    private void setTimeOfDelivery(HttpServletRequest request, UserOrder currentOrder) {

        String timeOfDeliveryMinutes = request.getParameter(ORDER_TIME_OF_DELIVERY_JSP_PARAM);
        LOGGER.info(format(LoggerConstants.PARAM_GOT_FROM_JSP_MESSAGE, ORDER_TIME_OF_DELIVERY_JSP_PARAM, timeOfDeliveryMinutes));

        LocalDateTime date = LocalDateTime.parse(timeOfDeliveryMinutes);
        currentOrder.setTimeOfDelivery(date);

        LOGGER.info(format(LoggerConstants.FROM_JSP_ATTR_SET_TO_CURRENT_ORDER_MESSAGE, ORDER_TIME_OF_DELIVERY_JSP_PARAM, currentOrder.getId()));
    }

    private void setCustomAddress(HttpServletRequest request, UserOrder currentOrder) {

        String locality = request.getParameter(ORDER_ADDRESS_LOCALITY_JSP_ATTRIBUTE);
        String street = request.getParameter(ORDER_ADDRESS_STREET_JSP_ATTRIBUTE);
        String buildingNumber = request.getParameter(ORDER_ADDRESS_BUILDING_NUMBER_JSP_ATTRIBUTE);
        String flatNumber = request.getParameter(ORDER_ADDRESS_FLAT_NUMBER_JSP_ATTRIBUTE);
        String porch = request.getParameter(ORDER_ADDRESS_PORCH_NUMBER_JSP_ATTRIBUTE);
        String floor = request.getParameter(ORDER_ADDRESS_FLOOR_NUMBER_JSP_ATTRIBUTE);

        StringBuilder address = new StringBuilder();
        address.append(locality)
                .append(",")
                .append(street)
                .append(",")
                .append(buildingNumber)
                .append(",")
                .append(flatNumber)
                .append(",")
                .append(porch)
                .append(",")
                .append(floor);

        currentOrder.getDeliveryAddress().setCustomDeliveryAddress(new String(address));

        LOGGER.info(format(LoggerConstants.FROM_JSP_ATTR_SET_TO_CURRENT_ORDER_MESSAGE, "Custom address", currentOrder.getId()));
    }
}
