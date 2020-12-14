package com.restraunt.shapshuk.entity.order.service;

import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.core.service.GenericServiceImpl;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.Transactional;
import com.restraunt.shapshuk.entity.deliveryaddress.model.DeliveryAddress;
import com.restraunt.shapshuk.entity.deliveryaddress.service.DeliveryAddressService;
import com.restraunt.shapshuk.entity.order.constants.OrderStatus;
import com.restraunt.shapshuk.entity.order.dao.UserOrderDao;
import com.restraunt.shapshuk.entity.order.model.UserOrder;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;
import com.restraunt.shapshuk.entity.orderitem.service.OrderItemService;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class UserOrderServiceImpl extends GenericServiceImpl<UserOrder> implements UserOrderService {

    private static final Logger LOGGER = Logger.getLogger(UserOrderServiceImpl.class.getName());
    private static final String SET_TO_USER_ORDER_MESSAGE = "[%s] has been set to User order";

    private final UserOrderDao userOrderDao;
    private final DeliveryAddressService deliveryAddressService;
    private final OrderItemService orderItemService;

    public UserOrderServiceImpl(UserOrderDao userOrderDao, DeliveryAddressService deliveryAddressService, OrderItemService orderItemService) {

        super(userOrderDao);
        this.userOrderDao = userOrderDao;
        this.deliveryAddressService = deliveryAddressService;
        this.orderItemService = orderItemService;
    }

    @Transactional
    @Override
    public UserOrder createNewOrder(User user) throws SQLException, ConnectionException {

        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(user.getId());
        LOGGER.info(format(SET_TO_USER_ORDER_MESSAGE, "User id"));

        userOrder.setTimeOfDelivery(LocalDateTime.now());
        LOGGER.info(format(SET_TO_USER_ORDER_MESSAGE, "Current date-time"));

        userOrder.setOrderStatus(OrderStatus.BUILD_UP);
        String statusMessage = OrderStatus.BUILD_UP + " status";
        LOGGER.info(format(SET_TO_USER_ORDER_MESSAGE, statusMessage));

        DeliveryAddress userDeliveryAddress = new DeliveryAddress();
        userDeliveryAddress.setUserAddress(user.getUserAddress());
        Long deliveryAddressId = deliveryAddressService.save(userDeliveryAddress);
        userDeliveryAddress.setId(deliveryAddressId);
        userOrder.setDeliveryAddress(userDeliveryAddress);
        LOGGER.info(format(SET_TO_USER_ORDER_MESSAGE, "Delivery address"));

        Long id = userOrderDao.save(userOrder);

        userOrder.setId(id);

        return userOrder;
    }

    @Transactional
    @Override
    public UserOrder getBuildingUpUserOrder(String sessionId) throws SQLException, ConnectionException {

        User currentUser = SecurityContext.getInstance().getCurrentUser(sessionId);

        if (isNull(currentUser)) {
            return null;
        }

        UserOrder buildingUpUserOrder = userOrderDao.findBuildingUpUserOrder(currentUser.getId());

        if (isNull(buildingUpUserOrder) || !buildingUpUserOrder.getOrderStatus().equals(OrderStatus.BUILD_UP)) {

            User user = SecurityContext.getInstance().getCurrentUser(sessionId);
            buildingUpUserOrder = createNewOrder(user);

            String message = "Order has been created now";
            LOGGER.info(message);
        } else {

            DeliveryAddress deliveryAddress = deliveryAddressService.getById(buildingUpUserOrder.getDeliveryAddress().getId());
            buildingUpUserOrder.setDeliveryAddress(deliveryAddress);
            LOGGER.info(format(SET_TO_USER_ORDER_MESSAGE, "Delivery address"));
        }

        return buildingUpUserOrder;
    }

    @Transactional
    @Override
    public BigDecimal getOrderCost(UserOrder order) throws ConnectionException, SQLException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        List<OrderItem> orderItems = orderItemService.findAllItemsByOrderId(order.getId());

        BigDecimal orderCost = new BigDecimal(0);
        for (OrderItem orderItem : orderItems) {
            orderCost = orderCost.add(orderItem.getItemCost());
        }
        LOGGER.info(format("Got order cost = [%d]", orderCost.longValue()));

        return orderCost;
    }

    @Transactional
    @Override
    public void update(UserOrder entity) throws SQLException, ConnectionException {

        super.update(entity);
        deliveryAddressService.update(entity.getDeliveryAddress());
    }

    @Override
    public void deleteById(Long id) throws SQLException, ConnectionException {

        orderItemService.deleteByOrderId(id);
        super.deleteById(id);
    }
}
