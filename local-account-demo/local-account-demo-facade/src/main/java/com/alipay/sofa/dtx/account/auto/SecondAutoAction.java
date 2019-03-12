package com.alipay.sofa.dtx.account.auto;


public interface SecondAutoAction {

	/**
	 * 账号加钱
	 * 
	 * @param accountNo
	 * @param amount
	 * @return
	 */
    public boolean amount_add(String accountNo, double amount);

}
