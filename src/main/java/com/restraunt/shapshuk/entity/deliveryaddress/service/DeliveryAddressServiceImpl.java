package com.restraunt.shapshuk.entity.deliveryaddress.service;

import com.restraunt.shapshuk.core.service.GenericServiceImpl;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.deliveryaddress.dao.DeliveryAddressDao;
import com.restraunt.shapshuk.entity.deliveryaddress.model.DeliveryAddress;
import com.restraunt.shapshuk.entity.useraddress.dao.UserAddressDao;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.sql.SQLException;

import static java.lang.String.format;

public class DeliveryAddressServiceImpl extends GenericServiceImpl<DeliveryAddress> implements DeliveryAddressService {

    private static final Logger LOGGER = Logger.getLogger(DeliveryAddressServiceImpl.class.getName());

    private final DeliveryAddressDao deliveryAddressDao;
    private final UserAddressDao userAddressDao;

    public DeliveryAddressServiceImpl(DeliveryAddressDao dao, UserAddressDao userAddressDao) {
        super(dao);
        this.deliveryAddressDao = dao;
        this.userAddressDao = userAddressDao;
    }

    @Override
    public DeliveryAddress getById(Long id) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_ID_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, id));

        DeliveryAddress deliveryAddress = deliveryAddressDao.getById(id);
        Long userAddressId = deliveryAddress.getUserAddress().getId();
        deliveryAddress.setUserAddress(userAddressDao.getById(userAddressId));

        LOGGER.info(format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, deliveryAddress.toString()));

        return deliveryAddress;
    }
}
