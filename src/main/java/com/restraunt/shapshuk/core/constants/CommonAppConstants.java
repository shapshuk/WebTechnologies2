package com.restraunt.shapshuk.core.constants;

public final class CommonAppConstants {

  public static final String USER_LOGGED_IN_JSP_PARAM = "userLoggedIn";
  public static final String VIEW_NAME_JSP_PARAM = "viewName";
  public static final String SESSION_ID_JSP_PARAM = "sessionId";
  public static final String SECURITY_CONTEXT_ATTRIBUTE = "securityContext";
  public static final String FEEDBACK_RATING_JSP_PARAM = "feedback.rating";
  public static final String FEEDBACK_TEXT_JSP_PARAM = "feedback.text";
  public static final String DISH_ID_JSP_PARAM = "dish.id";
  public static final String ORDER_ITEM_ID_JSP_PARAM = "orderItem.id";
  public static final String DEFAULT_ORDER_ADDRESS_JSP_PARAM = "default.order.address";
  public static final String ORDER_TIME_OF_DELIVERY_JSP_PARAM = "order.timeOfDelivery";
  public static final String ORDER_ITEM_LIST_JSP_ATTRIBUTE = "itemList";
  public static final String ORDER_DISH_AMOUNT_JSP_PARAM = "order.item.dishAmount";
  public static final String ORDER_ADDRESS_LOCALITY_JSP_ATTRIBUTE = "order.address.locality";
  public static final String ORDER_ADDRESS_STREET_JSP_ATTRIBUTE = "order.address.street";
  public static final String ORDER_ADDRESS_BUILDING_NUMBER_JSP_ATTRIBUTE = "order.address.buildingNumber";
  public static final String ORDER_ADDRESS_FLAT_NUMBER_JSP_ATTRIBUTE = "order.address.flatNumber";
  public static final String ORDER_ADDRESS_PORCH_NUMBER_JSP_ATTRIBUTE = "order.address.porch";
  public static final String ORDER_ADDRESS_FLOOR_NUMBER_JSP_ATTRIBUTE = "order.address.floor";
  public static final String TOTAL_ORDER_COST_JSP_ATTRIBUTE = "totalCost";
  public static final String TIME_LIST_JSP_ATTRIBUTE = "timeList";
  public static final String DEFAULT_USER_NAME_JSP_PARAM = "default.user.name";
  public static final String USER_NAME_JSP_PARAM = "user.name";
  public static final String USER_EMAIL_JSP_PARAM = "user.email";
  public static final String USER_PASSWORD_JSP_PARAM = "user.password";
  public static final String USER_PASSWORD_CONFIRM_JSP_PARAM = "user.password.confirm";
  public static final String USER_ADDRESS_JSP_PARAM = "user.address";
  public static final String DEFAULT_USER_PHONE_NUMBER_JSP_PARAM = "default.user.phoneNumber";
  public static final String USER_PHONE_NUMBER_JSP_PARAM = "user.phoneNumber";
  public static final String DISH_NAME_JSP_PARAM = "dish.name";
  public static final String DISH_COST_JSP_PARAM = "dish.cost";
  public static final String DISH_CATEGORY_NAME_JSP_PARAM = "dish.category";
  public static final String DISH_DESCRIPTION_JSP_PARAM = "dish.description";
  public static final String DISH_PICTURE_JSP_PARAM = "dish.picture";
  public static final String DISH_JSP_ATTRIBUTE = "dish";
  public static final String DISHES_JSP_ATTRIBUTE = "dishes";
  public static final String CATEGORY_NAME_JSP_PARAM = "category.name";
  public static final String CATEGORY_LIST_JSP_ATTRIBUTE = "categoryList";
  public static final String SELECTED_CATEGORIES_JSP_ATTRIBUTE = "selectedCategories";
  public static final String WALLET_CURRENT_MONEY_AMOUNT_JSP_ATTRIBUTE = "walletCurrentMoneyAmount";
  public static final String WALLET_NEW_MONEY_AMOUNT_JSP_PARAM = "wallet.new.money.amount";
  public static final String PAYMENT_FROM_ACCOUNT_JSP_PARAM = "payFromAccount";
  public static final String PAYMENT_ON_DELIVERY_JSP_PARAM = "payOnDelivery";
  public static final String PAYMENT_METHOD_JSP_PARAM = "order.pay.method";
  public static final String ERROR_JSP_ATTRIBUTE = "error";
  public static final String ERRORS_JSP_ATTRIBUTE = "errors";
  public static final String MESSAGE_JSP_ATTRIBUTE = "message";
  public static final String QUERY_PARAM_COMMAND = "_command";
  public static final String QUERY_PARAM_CATEGORY = "_category";
  public static final String QUERY_PARAM_PAGE = "_page";
    public static final String REDIRECT_FORMAT = "%s/%s";
  public static final Integer SUCCESSFULLY = 1;
  public static final Integer UNSUCCESSFULLY = 0;
  public static final String REGISTER_REDIRECT_WITH_PARAMS_FORMAT = "%s/%s?%s=%d&%s=%s";
  public static final String REGISTER_REDIRECT_WITH_ERROR_PARAMS_FORMAT = "%s/%s?%s=%d&%s";
  public static final String QUERY_PARAM_SUCCESS = "success";
  public static final String QUERY_PARAM_ERROR = "error";
  public static final String QUERY_PARAM_MESSAGE = "message";
  public static final String COMMAND_SECURITY_PROPERTY = "command.";
  public static final String NUMBER_OF_PAGES_JSP_ATTRIBUTE = "numberOfPages";
  public static final String CURRENT_PAGE_JSP_ATTRIBUTE = "currentPage";
  public static final Integer FIRST_PAGE = 1;
  public static final Integer RECORDS_PER_PAGE = 6;
  public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
  public static final String EMAIL_PATTERN = "^(.+)@(.+)$";
  public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
  public static final String PHONE_NUMBER_PATTERN = "^\\+\\d{12}$";

  private CommonAppConstants() {

  }


}
