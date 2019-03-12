package com.alipay.sofa.dtx.account.point.impl;

import java.sql.SQLException;

import com.alipay.dtx.client.core.api.domain.BusinessActionContext;
import com.alipay.sofa.dtx.account.dao.AccountPointDAO;
import com.alipay.sofa.dtx.account.domain.AccountPoint;
import com.alipay.sofa.dtx.account.point.SecondTccPointAction;

public class SecondTccPointActionImpl implements SecondTccPointAction {
	
    private AccountPointDAO  secondAccountPointDAO;

    @Override
	public boolean prepare_add_point(BusinessActionContext businessActionContext, String accountNo, double point) {

		String txId = businessActionContext.getTxId();
    	System.out.println("执行积分参与者-2（TCC）一阶段，txId:" + businessActionContext.getTxId());

		AccountPoint accountPoint= new AccountPoint();
		accountPoint.setTxId(txId);
		accountPoint.setPoint(point);
		accountPoint.setAccountNo(accountNo);
		accountPoint.setStatus(0);
		try {
			secondAccountPointDAO.addAccountPoint(accountPoint);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean commit(BusinessActionContext businessActionContext) {
		String txId = businessActionContext.getTxId();
		try {
	    	System.out.println("执行积分参与者-2（TCC）二阶段（提交），txId:" + businessActionContext.getTxId());

			AccountPoint accountPoint = secondAccountPointDAO.findAccountPoint(txId);
			if(accountPoint == null){
				return false;
			}
			accountPoint.setStatus(1);
			secondAccountPointDAO.updateAccountPointStatus(accountPoint);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean rollback(BusinessActionContext businessActionContext) {
		String txId = businessActionContext.getTxId();
		try {
	    	System.out.println("执行积分参与者-2（TCC）二阶段（回滚），txId:" + businessActionContext.getTxId());

			AccountPoint accountPoint = secondAccountPointDAO.findAccountPoint(txId);
			if(accountPoint == null){
				return false;
			}
			accountPoint.setStatus(2);
			secondAccountPointDAO.updateAccountPointStatus(accountPoint);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	public void setSecondAccountPointDAO(AccountPointDAO secondAccountPointDAO) {
		this.secondAccountPointDAO = secondAccountPointDAO;
	}

}
