package com.restraunt.shapshuk.service;

import com.restraunt.shapshuk.context.ApplicationContext;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Base64;

class DishServiceMysqlDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(DishServiceMysqlDatabaseTest.class.getName());
    private static DishService dishService;

    @BeforeAll
    static void initValidator() {

        ApplicationContext.initialize();
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        dishService = applicationContext.getBean(DishService.class);
    }

    @Test
    void shouldUpdateDishPictureWithoutExceptions1() throws SQLException, IOException, ConnectionException {

        long dishId = 1L;
        String dishName = "Pizza pepperoni";
        File file = new File("D://pizza_pepperoni.jpg");
        String description = "Cоус из протертых томатов, сыр Моцарелла, колбаса пепперони" +
                "\nВес: 780г" +
                "\nКалорийность, кКал: 2130";

        updateDish(dishId, dishName, file, description);
    }

    @Test
    void shouldUpdateDishPictureWithoutExceptions2() throws SQLException, IOException, ConnectionException {

        long dishId = 2L;
        String dishName = "Cheeseburger with bacon";
        File file = new File("D://burger_bacon_cheese.jpg");
        String description = "Бекон, двойная порция говядины на гриле, много сыра - все, как ты любишь, и ничего лишнего!" +
                "\nВес: 341г" +
                "\nКалорийность, кКал: 305";

        updateDish(dishId, dishName, file, description);
    }

    @Test
    void shouldUpdateDishPictureWithoutExceptions3() throws SQLException, IOException, ConnectionException {

        long dishId = 18L;
        String dishName = "Soup kharcho";
        File file = new File("D://soup_kharcho.jpg");
        String description = "Национальный грузинский суп из говядины с рисом, грецкими орехами и тклапи или кислым соусом ткемали. " +
                "Суп очень пряный, острый, с обилием чеснока и зелени (прежде всего, кинзы) и намного гуще, чем прочие супы, " +
                "к которым применяется правило «в супе должна быть половина жидкости»." +
                "\nВес: 400г" +
                "\nКалорийность, кКал: 300";

        updateDish(dishId, dishName, file, description);
    }

    void updateDish(long dishId, String dishName, File file, String description) throws IOException, SQLException, ConnectionException {

        byte[] fileContent = Files.readAllBytes(file.toPath());
        String stringPicture = Base64.getEncoder().encodeToString(fileContent);

        Dish dish = dishService.getById(dishId);
        dish.setPicture(stringPicture);
        dish.setName(dishName);
        dish.setDescription(description);

        LOGGER.info(dish.toString());

        dishService.update(dish);

        LOGGER.info(dishService.getById(dishId));
    }
}
