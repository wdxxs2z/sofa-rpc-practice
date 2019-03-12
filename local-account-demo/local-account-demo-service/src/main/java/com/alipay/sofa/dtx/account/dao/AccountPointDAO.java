/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.sofa.dtx.account.dao;

import java.sql.SQLException;

import com.alipay.sofa.dtx.account.domain.AccountPoint;


public interface AccountPointDAO {

    void addAccountPoint(AccountPoint accountPoint) throws SQLException;
    
    AccountPoint findAccountPoint(String account) throws SQLException;
    
    void updateAccountPointStatus(AccountPoint accountPoint) throws SQLException;
 
    void deleteAccountPoint(String account) throws SQLException;
    
    void deleteAllAccountPoint() throws SQLException;
    

}
