package com.restraunt.shapshuk.context;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.command.CommandFactory;
import com.restraunt.shapshuk.command.CommandFactoryImpl;
import com.restraunt.shapshuk.command.category.DisplayDishCategoryDeletingFormCommand;
import com.restraunt.shapshuk.command.category.SubmitDishCategoryCreatingFormCommand;
import com.restraunt.shapshuk.command.category.SubmitDishCategoryDeletingFormCommand;
import com.restraunt.shapshuk.command.dish.DeleteDishCommand;
import com.restraunt.shapshuk.command.dish.DisplayDishCreatingFormCommand;
import com.restraunt.shapshuk.command.dish.DisplayDishMenuCommand;
import com.restraunt.shapshuk.command.dish.DisplayDishUpdatingFormCommand;
import com.restraunt.shapshuk.command.dish.SubmitDishCreatingFormCommand;
import com.restraunt.shapshuk.command.dish.SubmitDishUpdatingFormCommand;
import com.restraunt.shapshuk.command.feedback.DisplayDishFeedbackCreatingFormCommand;
import com.restraunt.shapshuk.command.feedback.SubmitDishFeedbackCreatingFormCommand;
import com.restraunt.shapshuk.command.order.AddOrderItemCommand;
import com.restraunt.shapshuk.command.order.DeleteOrderItemCommand;
import com.restraunt.shapshuk.command.order.DisplayOrderCheckoutCommand;
import com.restraunt.shapshuk.command.order.DisplayOrderItemListCommand;
import com.restraunt.shapshuk.command.order.SubmitOrderCheckoutCommand;
import com.restraunt.shapshuk.command.user.SubmitUserLoginCommand;
import com.restraunt.shapshuk.command.user.SubmitUserRegisterCommand;
import com.restraunt.shapshuk.command.wallet.DisplayFillingUpWalletFormCommand;
import com.restraunt.shapshuk.command.wallet.SubmitFillingUpWalletFormCommand;
import com.restraunt.shapshuk.core.constants.CommandReturnValues;
import com.restraunt.shapshuk.entity.category.dao.DishCategoryDao;
import com.restraunt.shapshuk.entity.category.dao.DishCategoryDaoImpl;
import com.restraunt.shapshuk.entity.category.service.DishCategoryService;
import com.restraunt.shapshuk.entity.category.service.DishCategoryServiceImpl;
import com.restraunt.shapshuk.entity.deliveryaddress.dao.DeliveryAddressDao;
import com.restraunt.shapshuk.entity.deliveryaddress.dao.DeliveryAddressDaoImpl;
import com.restraunt.shapshuk.entity.deliveryaddress.service.DeliveryAddressService;
import com.restraunt.shapshuk.entity.deliveryaddress.service.DeliveryAddressServiceImpl;
import com.restraunt.shapshuk.entity.dish.dao.DishDao;
import com.restraunt.shapshuk.entity.dish.dao.DishDaoImpl;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.entity.dish.service.DishServiceImpl;
import com.restraunt.shapshuk.entity.dishfeedback.dao.DishFeedbackDao;
import com.restraunt.shapshuk.entity.dishfeedback.dao.DishFeedbackDaoImpl;
import com.restraunt.shapshuk.entity.dishfeedback.service.DishFeedbackService;
import com.restraunt.shapshuk.entity.dishfeedback.service.DishFeedbackServiceImpl;
import com.restraunt.shapshuk.entity.loyalty.dao.LoyaltyDao;
import com.restraunt.shapshuk.entity.loyalty.dao.LoyaltyDaoImpl;
import com.restraunt.shapshuk.entity.order.dao.UserOrderDao;
import com.restraunt.shapshuk.entity.order.dao.UserOrderDaoImpl;
import com.restraunt.shapshuk.entity.order.service.UserOrderService;
import com.restraunt.shapshuk.entity.order.service.UserOrderServiceImpl;
import com.restraunt.shapshuk.entity.orderitem.dao.OrderItemDao;
import com.restraunt.shapshuk.entity.orderitem.dao.OrderItemDaoImpl;
import com.restraunt.shapshuk.entity.orderitem.service.OrderItemService;
import com.restraunt.shapshuk.entity.orderitem.service.OrderItemServiceImpl;
import com.restraunt.shapshuk.entity.role.dao.UserRoleDao;
import com.restraunt.shapshuk.entity.role.dao.UserRoleDaoImpl;
import com.restraunt.shapshuk.entity.user.dao.UserAccountDao;
import com.restraunt.shapshuk.entity.user.dao.UserAccountDaoImpl;
import com.restraunt.shapshuk.entity.user.service.UserService;
import com.restraunt.shapshuk.entity.user.service.UserServiceImpl;
import com.restraunt.shapshuk.entity.useraddress.dao.UserAddressDao;
import com.restraunt.shapshuk.entity.useraddress.dao.UserAddressDaoImpl;
import com.restraunt.shapshuk.entity.wallet.dao.WalletDao;
import com.restraunt.shapshuk.entity.wallet.dao.WalletDaoImpl;
import com.restraunt.shapshuk.entity.wallet.service.WalletService;
import com.restraunt.shapshuk.entity.wallet.service.WalletServiceImpl;
import com.restraunt.shapshuk.util.JspUtil;
import com.restraunt.shapshuk.validation.cost.Digits;
import com.restraunt.shapshuk.validation.cost.MinCost;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.database.connection.ConnectionManagerImpl;
import com.restraunt.shapshuk.database.connection.ConnectionPool;
import com.restraunt.shapshuk.database.connection.ConnectionPoolImpl;
import com.restraunt.shapshuk.database.connection.TransactionManager;
import com.restraunt.shapshuk.database.connection.TransactionManagerImpl;
import com.restraunt.shapshuk.database.connection.Transactional;
import com.restraunt.shapshuk.validation.BeanValidator;
import com.restraunt.shapshuk.validation.Email;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.MaxLength;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.NotEmpty;
import com.restraunt.shapshuk.validation.Password;
import com.restraunt.shapshuk.validation.PhoneNumber;
import com.restraunt.shapshuk.validation.impl.AnnotationBasedBeanValidator;
import com.restraunt.shapshuk.validation.impl.DigitsFieldValidator;
import com.restraunt.shapshuk.validation.impl.EmailFieldValidator;
import com.restraunt.shapshuk.validation.impl.MaxLengthFieldValidator;
import com.restraunt.shapshuk.validation.impl.MinCostFieldValidator;
import com.restraunt.shapshuk.validation.impl.MinLengthFieldValidator;
import com.restraunt.shapshuk.validation.impl.NotEmptyFieldValidator;
import com.restraunt.shapshuk.validation.impl.PasswordFieldValidator;
import com.restraunt.shapshuk.validation.impl.PhoneNumberFieldValidator;
import com.restraunt.shapshuk.command.CommandType;
import com.restraunt.shapshuk.core.constants.JspName;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.restraunt.shapshuk.core.constants.ServletName.*;

public class ApplicationContext {

    private static final Logger LOGGER = Logger.getLogger(ApplicationContext.class.getName());

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final Lock INITIALIZE_LOCK = new ReentrantLock();
    private static ApplicationContext instance;
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private ConnectionPool connectionPool;

    private ApplicationContext() {

    }

    public static void initialize() {

        INITIALIZE_LOCK.lock();
        try {
            if (instance != null && INITIALIZED.get()) {

                String message = "Context has been already initialized";
                LOGGER.error(message);
                throw new IllegalStateException(message);

            } else {

                ApplicationContext context = new ApplicationContext();
                context.init();
                instance = context;
                INITIALIZED.set(true);
                LOGGER.info("Application context initialized");
            }

        } finally {
            INITIALIZE_LOCK.unlock();
        }
    }

    public static ApplicationContext getInstance() {

        if (instance != null && INITIALIZED.get()) {

            return instance;
        } else {

            String message = "Context wasn't initialized";
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }
    }

    private static <T> InvocationHandler createTransactionalInvocationHandler(TransactionManager transactionManager, T service) {
        return (proxy, method, args) -> {

            Method declaredMethod = service.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (method.isAnnotationPresent(Transactional.class)
                    || declaredMethod.isAnnotationPresent(Transactional.class)) {
                transactionManager.begin();
                LOGGER.info("Transaction has begun");
                try {
                    Object result = method.invoke(service, args);
                    transactionManager.commit();
                    LOGGER.info("Transaction has been committed");
                    return result;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    transactionManager.rollback();
                    throw e;
                }
            } else {
                LOGGER.info("Invoke service method without Transaction manager");
                return method.invoke(service, args);
            }
        };
    }

    private static <T> T createProxy(ClassLoader classLoader, InvocationHandler handler, Class<T>... toBeProxied) {
        LOGGER.info(String.format("Create a proxy of %s", toBeProxied));
        return (T) Proxy.newProxyInstance(classLoader, toBeProxied, handler);
    }

    private void init() {

        // ConnectionManager
        connectionPool = ConnectionPoolImpl.getInstance();
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManagerImpl(connectionPool, transactionManager);
        LOGGER.info("ConnectionManager initialized");

        //dao
        DishCategoryDao dishCategoryDao = new DishCategoryDaoImpl(connectionManager);
        DeliveryAddressDao deliveryAddressDao = new DeliveryAddressDaoImpl(connectionManager);
        DishDao dishDao = new DishDaoImpl(connectionManager);
        DishFeedbackDao dishFeedbackDao = new DishFeedbackDaoImpl(connectionManager);
        LoyaltyDao loyaltyDao = new LoyaltyDaoImpl(connectionManager);
        UserOrderDao userOrderDao = new UserOrderDaoImpl(connectionManager);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(connectionManager);
        UserRoleDao userRoleDao = new UserRoleDaoImpl(connectionManager);
        UserAccountDao userAccountDao = new UserAccountDaoImpl(connectionManager);
        UserAddressDao userAddressDao = new UserAddressDaoImpl(connectionManager);
        WalletDao walletDao = new WalletDaoImpl(connectionManager);
        LOGGER.info("Dao initialized");

        //service
        DishService dishService = new DishServiceImpl(dishDao, dishCategoryDao);
        OrderItemService orderItemService = new OrderItemServiceImpl(orderItemDao, dishService);
        DeliveryAddressService deliveryAddressService = new DeliveryAddressServiceImpl(deliveryAddressDao, userAddressDao);
        UserOrderService userOrderService = new UserOrderServiceImpl(userOrderDao, deliveryAddressService, orderItemService);
        UserService userService = new UserServiceImpl(userAccountDao, userAddressDao, loyaltyDao, walletDao, userRoleDao);
        DishCategoryService dishCategoryService = new DishCategoryServiceImpl(dishCategoryDao);
        DishFeedbackService dishFeedbackService = new DishFeedbackServiceImpl(dishFeedbackDao);
        WalletService walletService = new WalletServiceImpl(walletDao);
        LOGGER.info("Services initialized");

        //proxy of services
        InvocationHandler dishServiceHandler = createTransactionalInvocationHandler
                (transactionManager, dishService);
        DishService dishProxyService = createProxy(getClass().getClassLoader(),
                dishServiceHandler, DishService.class);

        InvocationHandler userOrderServiceHandler = createTransactionalInvocationHandler
                (transactionManager, userOrderService);
        UserOrderService userOrderProxyService = createProxy
                (getClass().getClassLoader(), userOrderServiceHandler, UserOrderService.class);

        InvocationHandler orderItemServiceHandler = createTransactionalInvocationHandler
                (transactionManager, orderItemService);
        OrderItemService orderItemProxyService = createProxy
                (getClass().getClassLoader(), orderItemServiceHandler, OrderItemService.class);

        InvocationHandler userServiceHandler = createTransactionalInvocationHandler
                (transactionManager, userService);
        UserService userProxyService = createProxy
                (getClass().getClassLoader(), userServiceHandler, UserService.class);

        InvocationHandler deliveryAddressServiceHandler = createTransactionalInvocationHandler
                (transactionManager, deliveryAddressService);
        DeliveryAddressService deliveryAddressProxyService = createProxy
                (getClass().getClassLoader(), deliveryAddressServiceHandler, DeliveryAddressService.class);

        LOGGER.info("Proxy of services initialized");

        //init bean validator
        Map<Class<? extends Annotation>, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(MaxLength.class, new MaxLengthFieldValidator());
        validatorMap.put(MinLength.class, new MinLengthFieldValidator());
        validatorMap.put(NotEmpty.class, new NotEmptyFieldValidator());
        validatorMap.put(Email.class, new EmailFieldValidator());
        validatorMap.put(Password.class, new PasswordFieldValidator());
        validatorMap.put(PhoneNumber.class, new PhoneNumberFieldValidator());
        validatorMap.put(MinCost.class, new MinCostFieldValidator());
        validatorMap.put(Digits.class, new DigitsFieldValidator());
        BeanValidator beanValidator = new AnnotationBasedBeanValidator(validatorMap);

        LOGGER.info("Bean validator initialized");

        //commands
        Command deleteOrderItemCommand = new DeleteOrderItemCommand(orderItemProxyService);
        Command displayOrderItemListCommand = new DisplayOrderItemListCommand(userOrderProxyService, orderItemProxyService);
        Command addOrderItemCommand = new AddOrderItemCommand(orderItemProxyService, dishProxyService, userOrderProxyService);
        Command submitUserLoginCommand = new SubmitUserLoginCommand(userProxyService);
        Command submitUserRegisterCommand = new SubmitUserRegisterCommand(userProxyService, beanValidator);
        Command displayDishMenuCommand = new DisplayDishMenuCommand(dishProxyService);
        Command displayOrderCheckoutCommand = new DisplayOrderCheckoutCommand(orderItemProxyService, userOrderProxyService);
        Command submitOrderCheckoutCommand = new SubmitOrderCheckoutCommand(userOrderProxyService, beanValidator, walletService);
        Command submitDishFeedbackCreatingFormCommand = new SubmitDishFeedbackCreatingFormCommand(dishFeedbackService);
        Command displayDishFeedbackCreatingFormCommand = new DisplayDishFeedbackCreatingFormCommand();
        Command displayDishCreatingFormCommand = new DisplayDishCreatingFormCommand();
        Command submitDishCreatingFormCommand = new SubmitDishCreatingFormCommand(dishProxyService, beanValidator);
        Command displayDishUpdatingFormCommand = new DisplayDishUpdatingFormCommand();
        Command submitDishUpdatingFormCommand = new SubmitDishUpdatingFormCommand(dishProxyService, dishCategoryService, beanValidator);
        Command deleteDishCommand = new DeleteDishCommand(dishProxyService);
        Command submitDishCategoryCreatingFormCommand = new SubmitDishCategoryCreatingFormCommand(dishCategoryService, beanValidator);
        Command submitDishCategoryDeletingFormCommand = new SubmitDishCategoryDeletingFormCommand(dishCategoryService);
        Command displayDishCategoryDeletingFormCommand = new DisplayDishCategoryDeletingFormCommand(dishCategoryService);
        Command displayFillingUpWalletFormCommand = new DisplayFillingUpWalletFormCommand();
        Command submitFillingUpWalletFormCommand = new SubmitFillingUpWalletFormCommand(walletService);
        LOGGER.info("Commands initialized");

        //utils
        JspUtil jspUtil = new JspUtil(dishProxyService, dishCategoryService);

        //commandFactory
        CommandFactory commandFactory = new CommandFactoryImpl();

        commandFactory.registerCommand(CommandType.ORDER_BASKET_SERVLET_SWITCH, (request, response) -> ORDER_BASKET_SERVLET);
        commandFactory.registerCommand(CommandType.MENU_SERVLET_SWITCH, (request, response) -> MENU_SERVLET);
        commandFactory.registerCommand(CommandType.REGISTER_SERVLET_SWITCH, (request, response) -> USER_REGISTER_SERVLET);
        commandFactory.registerCommand(CommandType.LOGIN_SERVLET_SWITCH, (request, response) -> LOGIN_SERVLET);
        commandFactory.registerCommand(CommandType.ORDER_CHECKOUT_SERVLET_SWITCH, (request, response) -> ORDER_CHECKOUT_SERVLET);
        commandFactory.registerCommand(CommandType.INDEX, (request, response) -> INDEX_SERVLET);

        commandFactory.registerCommand(CommandType.DELETE_ORDER_ITEM_COMMAND, deleteOrderItemCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_ORDER_ITEM_LIST_COMMAND, displayOrderItemListCommand);
        commandFactory.registerCommand(CommandType.ADD_ORDER_ITEM_COMMAND, addOrderItemCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_ORDER_CHECKOUT_COMMAND, displayOrderCheckoutCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_ORDER_CHECKOUT_COMMAND, submitOrderCheckoutCommand);

        commandFactory.registerCommand(CommandType.DISPLAY_DISH_FEEDBACK_CREATING_FORM_COMMAND, displayDishFeedbackCreatingFormCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_DISH_FEEDBACK_CREATING_FORM_COMMAND, submitDishFeedbackCreatingFormCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_DISH_MENU_COMMAND, displayDishMenuCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_DISH_CREATING_FORM_COMMAND, displayDishCreatingFormCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_DISH_CREATING_FORM_COMMAND, submitDishCreatingFormCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_DISH_UPDATING_FORM_COMMAND, displayDishUpdatingFormCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_DISH_UPDATING_FORM_COMMAND, submitDishUpdatingFormCommand);
        commandFactory.registerCommand(CommandType.DELETE_DISH_COMMAND, deleteDishCommand);

        commandFactory.registerCommand(CommandType.DISPLAY_DISH_CATEGORY_CREATING_FORM_COMMAND, ((request, response) -> JspName.CREATE_CATEGORY_FORM_JSP));
        commandFactory.registerCommand(CommandType.SUBMIT_DISH_CATEGORY_CREATING_FORM_COMMAND, submitDishCategoryCreatingFormCommand);
        commandFactory.registerCommand(CommandType.DISPLAY_DISH_CATEGORY_DELETING_FORM_COMMAND, displayDishCategoryDeletingFormCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_DISH_CATEGORY_DELETING_FORM_COMMAND, submitDishCategoryDeletingFormCommand);

        commandFactory.registerCommand(CommandType.DISPLAY_FILLING_UP_WALLET_FORM_COMMAND, displayFillingUpWalletFormCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_FILLING_UP_WALLET_FORM_COMMAND, submitFillingUpWalletFormCommand);

        commandFactory.registerCommand(CommandType.SUBMIT_USER_REGISTER_COMMAND, submitUserRegisterCommand);
        commandFactory.registerCommand(CommandType.SUBMIT_USER_LOGIN_COMMAND, submitUserLoginCommand);
        commandFactory.registerCommand(CommandType.LOGOUT, (request, response) -> {
            request.getSession().invalidate();
            SecurityContext.getInstance().logout(request.getSession().getId());
            return CommandReturnValues.LOGOUT_RESULT;
        });

        LOGGER.info("Command factory initialized");

        //bean command provider
        beans.put(CommandFactory.class, commandFactory);
        beans.put(DishService.class, dishProxyService);
        beans.put(OrderItemService.class, orderItemProxyService);
        beans.put(DeliveryAddressService.class, deliveryAddressProxyService);
        beans.put(UserOrderService.class, userOrderProxyService);
        beans.put(UserService.class, userProxyService);
        beans.put(DishCategoryService.class, dishCategoryService);
        beans.put(DishFeedbackService.class, dishFeedbackService);

        beans.put(UserAccountDao.class, userAccountDao);
        beans.put(BeanValidator.class, beanValidator);

        beans.put(ConnectionPool.class, connectionPool);
        beans.put(TransactionManager.class, transactionManager);
        beans.put(ConnectionManager.class, connectionManager);

        beans.put(JspUtil.class, jspUtil);

        LOGGER.info("Bean command provider initialized");

    }

    public void destroy() throws SQLException {

        connectionPool.shutdown();
        beans.clear();
        LOGGER.info("Application context destroyed");
    }

    public <T> T getBean(Class<T> clazz) {

        return (T) this.beans.get(clazz);
    }
}
