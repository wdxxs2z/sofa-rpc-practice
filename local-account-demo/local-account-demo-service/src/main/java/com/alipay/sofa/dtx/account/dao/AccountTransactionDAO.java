/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import com.alipay.sofa.dtx.account.domain.AccountTransaction;


public interface AccountTransactionDAO {

    void addTransaction(AccountTransaction accountTransaction) throws SQLException;
    
    AccountTransaction findTransaction(String txId) throws SQLException;
 
    void deleteTransaction(String txId) throws SQLException;
    
    void deleteAllTransaction() throws SQLException;
}
