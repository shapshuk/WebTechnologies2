package com.restraunt.shapshuk.dao;

import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.user.dao.UserAccountDao;
import com.restraunt.shapshuk.entity.user.model.User;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAccountDaoMysqlDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(UserAccountDaoMysqlDatabaseTest.class.getName());
    private static UserAccountDao userAccountDao;

    @BeforeAll
    static void initContext() {

        ApplicationContext.initialize();
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        userAccountDao = applicationContext.getBean(UserAccountDao.class);
    }

    @Test
    void shouldFindAllUsers() throws ConnectionException, SQLException {

        List<User> all = userAccountDao.findAll();
        for (User user : all) {
            LOGGER.info(user.toString());
        }
        assertTrue(all.size() > 0);
    }

    @Test
    void shouldGetUserByName() throws ConnectionException, SQLException {

        String name = "qwerty";
        User user = userAccountDao.getByName(name);
        String message = "account got by name = " + name + ", account: " + user.toString();
        LOGGER.info(message);
        assertNotNull(user);
    }
}
