package com.alipay.sofa.dtx.account.service;

/**
 * 积分服务
 * @author zhangsen
 *
 */
public interface PointService {
	
	/**
	 * 添加积分 TCC
	 * 
	 * @param accountNo1
	 * @param accountNo2
	 * @param point
	 * @return
	 */
	public boolean addPointByTcc(String accountNo1, String accountNo2, int point);

	/**
	 * 添加积分 FMT
	 * 
	 * @param accountNo1
	 * @param accountNo2
	 * @param point
	 * @return
	 */
	public boolean addPointByAuto(String accountNo1, String accountNo2, int point);
	

}
