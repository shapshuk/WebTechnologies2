package com.restraunt.shapshuk.entity.wallet.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.wallet.model.Wallet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class WalletDaoImpl extends GenericDao<Wallet> implements WalletDao {

    public WalletDaoImpl(ConnectionManager connectionManager) {
        super(WalletTableConstants.WALLET_TABLE_NAME, getWalletRowMapper(), connectionManager);
    }

    private static IdentifiedRowMapper<Wallet> getWalletRowMapper() {

        return new IdentifiedRowMapper<Wallet>() {

            @Override
            public Wallet map(ResultSet resultSet) throws SQLException {
                Wallet wallet = new Wallet();
                wallet.setId(resultSet.getLong(WalletTableConstants.ID));
                wallet.setMoneyAmount(resultSet.getBigDecimal(WalletTableConstants.MONEY_AMOUNT));
                return wallet;
            }

            @Override
            public List<String> getColumnNames() {
                return Collections.singletonList(WalletTableConstants.MONEY_AMOUNT);
            }

            @Override
            public void populateStatement(PreparedStatement statement, Wallet entity) throws SQLException {

                statement.setBigDecimal(1, entity.getMoneyAmount());
            }
        };
    }
}
