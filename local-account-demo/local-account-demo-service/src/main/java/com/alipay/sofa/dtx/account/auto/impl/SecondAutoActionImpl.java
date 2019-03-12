package com.alipay.sofa.dtx.account.auto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.sofa.dtx.account.auto.SecondAutoAction;
import com.alipay.sofa.dtx.account.dao.AccountDAO;
import com.alipay.sofa.dtx.account.domain.Account;
import com.alipay.sofa.dtx.client.context.RuntimeContext;

public class SecondAutoActionImpl implements SecondAutoAction {
	
    protected final static Logger logger = LoggerFactory.getLogger(SecondAutoActionImpl.class);

	private AccountDAO secondAccountDAOAuto;
	
    private TransactionTemplate   tccSecondActionTransactionTemplateAuto;
	
    /**
     * 账户加钱
     */
	@Override
	public boolean amount_add(final String accountNo, final double amount) {
		try{
			return tccSecondActionTransactionTemplateAuto.execute(new TransactionCallback<Boolean>() {
	            @Override
	            public Boolean doInTransaction(TransactionStatus status) {
	                try {
                    	System.out.println("执行加钱参与者（FMT），txId:" + RuntimeContext.getContext().getTxId());

	                	logger.info("amount_add getAccountForUpdate " + accountNo);
	                    Account account = secondAccountDAOAuto.getAccountForUpdate(accountNo);
	                    if(account == null) {
	                    	throw new RuntimeException("账号不存在");
	                    }
	                    //加钱
	                    double newAmount = account.getAmount() + amount;
	                    account.setAmount(newAmount);
	                    secondAccountDAOAuto.updateAmount(account);
	                    logger.info("amount_add updateAmount " + accountNo);
	                } catch (Exception e) {
	                	System.out.print("amount_add error:"+ e);
	                	status.setRollbackOnly();
	                	return false;
	                }
	                return true;
	            }
	        });
		} catch (Exception e) {
        	System.out.print("amount_add failed:" + e);
        	return false;
        }
	}


	public void setSecondAccountDAOAuto(AccountDAO secondAccountDAOAuto) {
		this.secondAccountDAOAuto = secondAccountDAOAuto;
	}


	public void setTccSecondActionTransactionTemplateAuto(TransactionTemplate tccSecondActionTransactionTemplateAuto) {
		this.tccSecondActionTransactionTemplateAuto = tccSecondActionTransactionTemplateAuto;
	}

}
