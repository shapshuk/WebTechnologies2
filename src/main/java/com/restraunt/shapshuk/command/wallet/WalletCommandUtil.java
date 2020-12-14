package com.restraunt.shapshuk.command.wallet;

import com.restraunt.shapshuk.context.SecurityContext;
import com.restraunt.shapshuk.entity.user.model.User;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import static java.lang.String.format;

public final class WalletCommandUtil {

    private static final Logger LOGGER = Logger.getLogger(WalletCommandUtil.class.getName());

    private WalletCommandUtil() {

    }

    public static void setCurrentWalletMoneyAmountToRequest(HttpServletRequest request) {

        String sessionId = request.getSession().getId();
        User user = SecurityContext.getInstance().getCurrentUser(sessionId);

        BigDecimal moneyAmount = user.getWallet().getMoneyAmount();
        request.setAttribute(CommonAppConstants.WALLET_CURRENT_MONEY_AMOUNT_JSP_ATTRIBUTE, moneyAmount);
        LOGGER.info(String.format(LoggerConstants.ATTRIBUTE_SET_TO_JSP_MESSAGE, CommonAppConstants.WALLET_CURRENT_MONEY_AMOUNT_JSP_ATTRIBUTE, moneyAmount));
    }
}
