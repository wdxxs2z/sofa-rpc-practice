/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import com.alipay.sofa.dtx.account.domain.Account;


public interface AccountDAO {

    void addAccount(Account account) throws SQLException;
    
    int updateAmount(Account account) throws SQLException;
    
    int updateFreezedAmount(Account account) throws SQLException;
    
    Account getAccount(String accountNo) throws SQLException;
    
    Account getAccountForUpdate(String accountNo) throws SQLException;
    
    void deleteAllAccount() throws SQLException;

}
