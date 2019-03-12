package com.alipay.sofa.dtx.account.point.impl;

import java.sql.SQLException;

import com.alipay.sofa.dtx.account.dao.AccountPointDAO;
import com.alipay.sofa.dtx.account.domain.AccountPoint;
import com.alipay.sofa.dtx.account.point.SecondAutoPointAction;
import com.alipay.sofa.dtx.client.context.RuntimeContext;

public class SecondAutoPointActionImpl implements SecondAutoPointAction {

    private AccountPointDAO secondAccountPointDAOAuto;
	
	@Override
	public boolean point_add(String txId, String accountNo, double addPoint) {
    	System.out.println("执行积分参与者-2（FMT），txId:" + RuntimeContext.getContext().getTxId());

		AccountPoint accountPoint = new AccountPoint();
		accountPoint.setTxId(txId);
		accountPoint.setPoint(addPoint);
		accountPoint.setAccountNo(accountNo);
		accountPoint.setStatus(1);
		try {
			secondAccountPointDAOAuto.addAccountPoint(accountPoint);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setSecondAccountPointDAOAuto( AccountPointDAO secondAccountPointDAOAuto) {
		this.secondAccountPointDAOAuto = secondAccountPointDAOAuto;
	}

}
