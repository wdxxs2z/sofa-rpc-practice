/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.sofa.dtx.account.domain;

/**
 * 
 * @author 冰魂
 * @version $Id: Account.java, v 0.1 2014年9月22日 下午7:36:28 Exp $
 */
public class Account {

    /**
     * 账户
     */
    private String accountNo;
    /**
     * 余额
     */
    private double amount;
    /**
     * 冻结金额
     */
    private double freezedAmount;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFreezedAmount() {
        return freezedAmount;
    }

    public void setFreezedAmount(double freezedAmount) {
        this.freezedAmount = freezedAmount;
    }


}
