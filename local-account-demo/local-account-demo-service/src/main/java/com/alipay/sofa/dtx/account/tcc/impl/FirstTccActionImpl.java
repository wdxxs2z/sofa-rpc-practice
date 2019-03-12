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
import com.alipay.sofa.dtx.account.tcc.FirstTccAction;
import com.alipay.sofa.dtx.rm.tcc.TccTransactionController;

public class FirstTccActionImpl implements FirstTccAction {
	
	protected final static Logger logger = LoggerFactory.getLogger(FirstTccActionImpl.class);

    //账户dao
    private AccountDAO            firstAccountDAO;
    //账户流水dao
    private AccountTransactionDAO firstAccountTransactionDAO;
    //A银行事务模版
    private TransactionTemplate   tccFirstActionTransactionTemplate;

    /**
     * 一阶段准备
     */
    @Override
    public boolean prepare_minus(final BusinessActionContext businessActionContext,
                                 final String accountNo, final double amount) {
        try {
        	return tccFirstActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {
                @Override
                public Boolean doInTransaction(TransactionStatus status) {
                    try {
                    	System.out.println("执行扣钱参与者(TCC)一阶段，txId:" + businessActionContext.getTxId());
                        
                    	//1. 校验账户余额
                        Account account = firstAccountDAO.getAccountForUpdate(accountNo);
                        if (account.getAmount() - account.getFreezedAmount() - amount < 0) {
                            throw new RuntimeException("余额不足");
                        }
                        logger.info("一阶段-校验账户号{}余额{}", account.getAccountNo(), amount);
                        
                        //2. 记录账户操作流水,确保后续对账
                        AccountTransaction accountTransaction = new AccountTransaction();
                        accountTransaction.setTxId(businessActionContext.getTxId());
                        accountTransaction.setAccountNo(accountNo);
                        accountTransaction.setAmount(amount);
                        accountTransaction.setType("minus");
                        firstAccountTransactionDAO.addTransaction(accountTransaction);
                        
                        //3. 冻结资金
                        double freezedAmount = account.getFreezedAmount() + amount;
                        account.setFreezedAmount(freezedAmount);
                        firstAccountDAO.updateFreezedAmount(account);
                        logger.info("一阶段-冻结账户号{}金额{}", account.getAccountNo(), freezedAmount);
                        
                    } catch (Exception e) {
                    	logger.error("一阶段操作失败", e);
                        throw new RuntimeException("一阶段操作失败", e);
                    }
                    return true;
                }
            });
        } catch (Exception e) {
        	System.out.print("first prepare:" + e);
            return false;
        }
    }
    
    public boolean prepare_minus_anti_suspend(final BusinessActionContext businessActionContext, 
    		final String accountNo, final double amount){
    	try {
         	return tccFirstActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {
                 @Override
                 public Boolean doInTransaction(TransactionStatus status) {
                     try {
                    	 //模拟一阶段 网络超时
                    	 Thread.sleep(45*1000);
                    	 
                    	 //防悬挂
                    	 TccTransactionController.doAntiSuspendControl();
                    	 
                         //校验账户余额
                         Account account = firstAccountDAO.getAccountForUpdate(accountNo);
                         if (account.getAmount()  - account.getFreezedAmount() - amount < 0) {
                             throw new RuntimeException("余额不足");
                         }
                         //记录账户操作流水
                         AccountTransaction accountTransaction = new AccountTransaction();
                         accountTransaction.setTxId(businessActionContext.getTxId());
                         accountTransaction.setAccountNo(accountNo);
                         accountTransaction.setAmount(amount);
                         accountTransaction.setType("minus");
                         firstAccountTransactionDAO.addTransaction(accountTransaction);
                         //冻结资金
                         double freezedAmount = account.getFreezedAmount() + amount;
                         account.setFreezedAmount(freezedAmount);
                         firstAccountDAO.updateFreezedAmount(account);
                         
                     } catch (Exception e) {
                    	 System.out.print("一阶段操作失败:" + e);
                         throw new RuntimeException("一阶段操作失败", e);
                     }
                     return true;
                 }
             });
         } catch (Exception e) {
         	logger.error("first prepare", e);
             return false;
         }
    }

    /**
     * 二阶段提交
     */
    @Override
    public boolean commit(final BusinessActionContext businessActionContext) {

    	return tccFirstActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                	System.out.println("执行扣钱参与者(TCC)二阶段（提交），txId:" + businessActionContext.getTxId());

                    //校验账户
                    AccountTransaction accountTransaction = firstAccountTransactionDAO
                        .findTransaction(businessActionContext.getTxId());
                    if(accountTransaction == null){
                    	return true;
                    }
                    Account account = firstAccountDAO.getAccountForUpdate(accountTransaction.getAccountNo());
                    logger.info("二阶段-校验账户号{}在扣钱事务{}内", account.getAccountNo(), businessActionContext.getTxId());
                    //扣钱
                    double amount = account.getAmount() - accountTransaction.getAmount();
                    if (amount < 0) {
                        throw new RuntimeException("余额不足");
                    }
                    account.setAmount(amount);
                    firstAccountDAO.updateAmount(account);
                    logger.info("二阶段-扣除账户号{}后的余额为：{}", account.getAccountNo(), amount);
                    //释放冻结金额
                    account.setFreezedAmount(account.getFreezedAmount()  - accountTransaction.getAmount());
                    firstAccountDAO.updateFreezedAmount(account);
                    logger.info("二阶段-释放扣除账户号{}后的冻结额为：{}", account.getAccountNo(), account.getFreezedAmount());
                    return true;
                } catch (Exception e) {
                	System.out.print("first commit:" + e);
                	status.setRollbackOnly();
                	return false;
                }
            }
        });
    }

    /**
     * 二阶段回滚
     */
    @Override
    public boolean rollback(final BusinessActionContext businessActionContext) {
    	return tccFirstActionTransactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                	System.out.println("执行扣钱参与者(TCC)二阶段（回滚），txId:" + businessActionContext.getTxId());
                    //
                    AccountTransaction accountTransaction = firstAccountTransactionDAO
                        .findTransaction(businessActionContext.getTxId());
                    if (accountTransaction == null) {
                    	return true;
                    }
                    //释放冻结金额
                    Account account = firstAccountDAO.getAccountForUpdate(accountTransaction.getAccountNo());
                    account.setFreezedAmount(account.getFreezedAmount()
                                             - accountTransaction.getAmount());
                    firstAccountDAO.updateFreezedAmount(account);
                    //删除流水
                    firstAccountTransactionDAO.deleteTransaction(businessActionContext.getTxId());
                    return true;
                } catch (Exception e) {
                	System.out.print("first rollback:" + e);
                	status.setRollbackOnly();
                	return false;
                }
            }
        });
    }
    
	public void setFirstAccountDAO(AccountDAO firstAccountDAO) {
		this.firstAccountDAO = firstAccountDAO;
	}

	public void setFirstAccountTransactionDAO(
			AccountTransactionDAO firstAccountTransactionDAO) {
		this.firstAccountTransactionDAO = firstAccountTransactionDAO;
	}

	public void setTccFirstActionTransactionTemplate(
			TransactionTemplate tccFirstActionTransactionTemplate) {
		this.tccFirstActionTransactionTemplate = tccFirstActionTransactionTemplate;
	}
    
}
