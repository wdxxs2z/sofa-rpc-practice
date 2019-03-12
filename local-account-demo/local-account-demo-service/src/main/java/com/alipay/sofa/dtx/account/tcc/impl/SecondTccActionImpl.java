/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.alipay.sofa.dtx.account.tcc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.dtx.client.core.api.domain.BusinessActionContext;
import com.alipay.sofa.dtx.account.dao.AccountDAO;
import com.alipay.sofa.dtx.account.dao.AccountTransactionDAO;
import com.alipay.sofa.dtx.account.domain.Account;
import com.alipay.sofa.dtx.account.domain.AccountTransaction;
import com.alipay.sofa.dtx.account.tcc.SecondTccAction;


public class SecondTccActionImpl implements SecondTccAction {
	
	protected final static Logger logger = LoggerFactory.getLogger(FirstTccActionImpl.class);

    private AccountDAO            secondAccountDAO;

    private AccountTransactionDAO secondAccountTransactionDAO;

    private TransactionTemplate   tccSecondActionTransactionTemplate;

    /**
     * 一阶段准备
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean prepare_add(final BusinessActionContext businessActionContext,
                               final String accountNo, final double amount) {
        try {
        	tccSecondActionTransactionTemplate.execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    try {
                    	System.out.println("执行加钱参与者(TCC)一阶段，txId:" + businessActionContext.getTxId());

                        //先记一笔账户操作流水
                        AccountTransaction accountTransaction = new AccountTransaction();
                        accountTransaction.setTxId(businessActionContext.getTxId());
                        accountTransaction.setAccountNo(accountNo);
                        accountTransaction.setAmount(amount);
                        accountTransaction.setType("add");
                        secondAccountTransactionDAO.addTransaction(accountTransaction);
                        //再递增冻结金额，表示这部分钱已经被冻结，不能使用
                        Account account = secondAccountDAO.getAccountForUpdate(accountNo);
                        double freezedAmount = account.getFreezedAmount() + amount;
                        account.setFreezedAmount(freezedAmount);
                        secondAccountDAO.updateFreezedAmount(account);
                        logger.info("一阶段-准备从账户-{}增加冻结金额：{}", accountNo, freezedAmount);
                    } catch (Exception e) {
                    	System.out.print("second prepare:"+ e);
                        throw new RuntimeException("", e);
                    }

                    return true;
                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 二阶段提交
     */
    @Override
    public boolean commit(final BusinessActionContext businessActionContext) {

    	return tccSecondActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                	System.out.println("执行加钱参与者(TCC)二阶段（提交），txId:" + businessActionContext.getTxId());

                    //找到账户操作流水
                    AccountTransaction accountTransaction = secondAccountTransactionDAO
                        .findTransaction(businessActionContext.getTxId());
                    if(accountTransaction == null){
                    	return true;
                    }
                    Account account = secondAccountDAO.getAccountForUpdate(accountTransaction.getAccountNo());
                    //加钱
                    double amount = account.getAmount() + accountTransaction.getAmount();
                    account.setAmount(amount);
                    secondAccountDAO.updateAmount(account);
                    logger.info("二阶段-提交从账户号{}加钱后的余额：{}", account.getAccountNo(), amount);
                    //冻结金额相应减少
                    account.setFreezedAmount(account.getFreezedAmount()
                                             - accountTransaction.getAmount());
                    secondAccountDAO.updateFreezedAmount(account);
                    logger.info("二阶段-减少账户号{}冻结金额后的冻结额为：{}", account.getAccountNo(), account.getFreezedAmount());
                } catch (Exception e) {
                	System.out.print("second commit:" + e);
                	status.setRollbackOnly();
                	return false;
                }
                return true;
            }
        });

    }

    /**
     * 二阶段回滚
     */
    @Override
    public boolean rollback(final BusinessActionContext businessActionContext) {
    	return tccSecondActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                	System.out.println("执行加钱参与者(TCC)二阶段（回滚），txId:" + businessActionContext.getTxId());

                    AccountTransaction accountTransaction = secondAccountTransactionDAO
                        .findTransaction(businessActionContext.getTxId());
                    if(accountTransaction == null){
                    	return true;
                    }
                    //释放冻结金额
                    Account account = secondAccountDAO.getAccountForUpdate(accountTransaction.getAccountNo());
                    account.setFreezedAmount(account.getFreezedAmount()
                                             - accountTransaction.getAmount());
                    secondAccountDAO.updateFreezedAmount(account);

                    secondAccountTransactionDAO.deleteTransaction(businessActionContext.getTxId());
                    return true;
                } catch (Exception e) {
                	System.out.print("second rollback:" + e);
                	status.setRollbackOnly();
                	return false;
                }
               
            }
        });
    }
    
    
	public void setSecondAccountDAO(AccountDAO secondAccountDAO) {
		this.secondAccountDAO = secondAccountDAO;
	}

	public void setSecondAccountTransactionDAO(AccountTransactionDAO secondAccountTransactionDAO) {
		this.secondAccountTransactionDAO = secondAccountTransactionDAO;
	}

	public void setTccSecondActionTransactionTemplate(TransactionTemplate tccSecondActionTransactionTemplate) {
		this.tccSecondActionTransactionTemplate = tccSecondActionTransactionTemplate;
	}
    
    
    

}
