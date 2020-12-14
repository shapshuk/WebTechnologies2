package com.restraunt.shapshuk.core.service;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

public class GenericServiceImpl<T extends IdentifiedRow> implements GenericService<T> {

    private static final Logger LOGGER = Logger.getLogger(GenericServiceImpl.class.getName());

    private CrudDao<T> crudDao;

    public GenericServiceImpl(CrudDao<T> crudDao) {

        this.crudDao = crudDao;
    }

    @Override
    public Long save(T entity) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, entity.toString()));

        return crudDao.save(entity);
    }

    @Override
    public void update(T entity) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, entity.toString()));

        crudDao.update(entity);
    }

    public void deleteById(Long id) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_ID_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, id));

        crudDao.deleteById(id);
    }

    @Override
    public void delete(T entity) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, entity.toString()));

        crudDao.delete(entity);
    }

    @Override
    public T getById(Long id) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_ID_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, id));

        return crudDao.getById(id);
    }

    @Override
    public List<T> findAll() throws SQLException, ConnectionException {
        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String
            .format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        return crudDao.findAll();
    }

    public Integer getNumberOfRows() throws ConnectionException, SQLException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        LOGGER.info(String
            .format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        return crudDao.getNumberOfRows();
    }
}
