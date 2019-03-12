package com.alipay.sofa.dtx.account.service;

/**
 * 转账服务
 * @author zhangsen
 *
 */
public interface TransferService {

	/**
	 * TCC 转账
	 * 
	 * @param from
	 * @param to
	 * @param amount
	 * @param businessId
	 */
	public boolean transferByTcc(final String from, final String to, final double amount);
	
	/**
	 * FMT转账
	 * 
	 * @param from
	 * @param to
	 * @param amount
	 * @param businessId
	 */
	public boolean transferByAuto(final String from, final String to, final double amount);
	
	
	/**
	 * TCC 转账，并且开启嵌套事务记录积分
	 * @param from
	 * @param to
	 * @param amount
	 * @return
	 */
	public boolean transferByTccNest(final String from, final String to, final double amount);

	/**
	 * FMT转账，并且开启嵌套事务记录积分
	 * @param from
	 * @param to
	 * @param amount
	 * @return
	 */
	public boolean transferByAutoNest(final String from, final String to, final double amount);

	

}
