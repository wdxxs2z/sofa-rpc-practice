package com.alipay.sofa.dtx.account.auto;


public interface FirstAutoAction {

	/**
	 * 扣钱
	 * 
	 * @param accountNo
	 * @param amount
	 * @return
	 */
	public boolean amount_minus(String accountNo,  double amount);
	
}
