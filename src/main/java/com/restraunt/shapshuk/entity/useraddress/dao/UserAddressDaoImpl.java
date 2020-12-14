package com.restraunt.shapshuk.entity.useraddress.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.useraddress.model.UserAddress;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class UserAddressDaoImpl extends GenericDao<UserAddress> implements UserAddressDao {

    public UserAddressDaoImpl(ConnectionManager connectionManager) {
        super(UserAddressTableConstants.USER_ADDRESS_TABLE_NAME, getUserAddressRowMapper(), connectionManager);
    }

    private static IdentifiedRowMapper<UserAddress> getUserAddressRowMapper() {

        return new IdentifiedRowMapper<UserAddress>() {

            @Override
            public UserAddress map(ResultSet resultSet) throws SQLException {
                UserAddress userAddress = new UserAddress();
                userAddress.setId(resultSet.getLong(UserAddressTableConstants.ID));
                userAddress.setFullAddress(resultSet.getString(UserAddressTableConstants.USER_ADDRESS_STRING));
                return userAddress;
            }

            @Override
            public List<String> getColumnNames() {
                return Collections.singletonList(UserAddressTableConstants.USER_ADDRESS_STRING);
            }

            @Override
            public void populateStatement(PreparedStatement statement, UserAddress entity) throws SQLException {

                statement.setString(1, entity.getFullAddress());
            }
        };
    }
}
