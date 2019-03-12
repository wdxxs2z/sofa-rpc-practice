package com.alipay.sofa.dtx.account.auto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.sofa.dtx.account.auto.FirstAutoAction;
import com.alipay.sofa.dtx.account.dao.AccountDAO;
import com.alipay.sofa.dtx.account.domain.Account;
import com.alipay.sofa.dtx.client.context.RuntimeContext;

public class FirstAutoActionImpl implements FirstAutoAction {
	
    protected final static Logger logger = LoggerFactory.getLogger(FirstAutoActionImpl.class);

	 //账户dao
    private AccountDAO firstAccountDAOAuto;
    
    private TransactionTemplate   tccFirstActionTransactionTemplateAuto;
    
    /**
     * 账户扣钱
     */
	@Override
	public boolean amount_minus(final String accountNo, final double amount) {
		try{
			return tccFirstActionTransactionTemplateAuto.execute(new TransactionCallback<Boolean>() {

	            @Override
	            public Boolean doInTransaction(TransactionStatus status) {
	                try {
                    	System.out.println("执行扣钱参与者（FMT），txId:" + RuntimeContext.getContext().getTxId());

	                	logger.info("amount_minus getAccountForUpdate " + accountNo);
	                	Account account = firstAccountDAOAuto.getAccountForUpdate(accountNo);
	                    if(account == null){
	                    	throw new RuntimeException("账号不存在");
	                    }
	                    //校验账户余额
	                    double newAmount = account.getAmount() - amount;
	                    if (amount < 0) {
	                        throw new RuntimeException("余额不足");
	                    }
	                    //扣钱
	                    account.setAmount(newAmount);
	                    int n = firstAccountDAOAuto.updateAmount(account);
	                	logger.info("amount_minus updateAmount " + accountNo);
	                    if(n == 1){
	                    	return true;
	                    }else{
	                    	status.setRollbackOnly();
	                    	return false;
	                    }
	                } catch (Exception e) {
	                	System.out.println("amount_minus error:  " + e);
	                	status.setRollbackOnly();
	                	return false;
	                }
	            }
			});
		}catch(Exception e){
			logger.error("amount_minus failed", e);
        	return false;
		}
		
	}

	public void setFirstAccountDAOAuto(AccountDAO firstAccountDAOAuto) {
		this.firstAccountDAOAuto = firstAccountDAOAuto;
	}

	public void setTccFirstActionTransactionTemplateAuto(TransactionTemplate tccFirstActionTransactionTemplateAuto) {
		this.tccFirstActionTransactionTemplateAuto = tccFirstActionTransactionTemplateAuto;
	}
	
	
}
