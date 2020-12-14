package com.restraunt.shapshuk.entity.wallet.service;

import com.restraunt.shapshuk.core.service.GenericServiceImpl;
import com.restraunt.shapshuk.entity.wallet.dao.WalletDao;
import com.restraunt.shapshuk.entity.wallet.model.Wallet;

public class WalletServiceImpl extends GenericServiceImpl<Wallet> implements WalletService {

    public WalletServiceImpl(WalletDao dao) {
        super(dao);
    }
}
