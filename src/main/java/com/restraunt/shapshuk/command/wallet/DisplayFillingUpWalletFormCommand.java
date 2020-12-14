package com.restraunt.shapshuk.command.wallet;

import com.restraunt.shapshuk.command.Command;
import com.restraunt.shapshuk.core.constants.JspName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplayFillingUpWalletFormCommand implements Command {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        WalletCommandUtil.setCurrentWalletMoneyAmountToRequest(request);

        return JspName.FILL_UP_WALLET_FORM_JSP;
    }

}
