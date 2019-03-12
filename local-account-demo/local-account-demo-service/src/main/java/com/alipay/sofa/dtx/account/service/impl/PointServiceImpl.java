package com.alipay.sofa.dtx.account.service.impl;

import com.alipay.sofa.dtx.account.point.FirstAutoPointAction;
import com.alipay.sofa.dtx.account.point.FirstTccPointAction;
import com.alipay.sofa.dtx.account.point.SecondAutoPointAction;
import com.alipay.sofa.dtx.account.point.SecondTccPointAction;
import com.alipay.sofa.dtx.account.service.PointService;
import com.alipay.sofa.dtx.client.aop.annotation.DtxTransaction;
import com.alipay.sofa.dtx.client.context.RuntimeContext;

/**
 * 积分服务
 * 
 * @author zhangsen
 *
 */
public class PointServiceImpl implements PointService {
	
	private FirstAutoPointAction firstAutoPointAction;
	
	private SecondAutoPointAction secondAutoPointAction;
	
	private FirstTccPointAction firstTccPointAction;
	
	private SecondTccPointAction secondTccPointAction;
	
	@Override
	@DtxTransaction(bizType="pay-add-point-tcc")
	public boolean addPointByTcc(String accountNo1, String accountNo2, int point) {
		try{
			System.out.println("嵌套事务开启，txId:" + RuntimeContext.getContext().getTxId());

			boolean ret = firstTccPointAction.prepare_add_point(null, accountNo1, point);
			if(!ret){
				//事务回滚
				throw new RuntimeException("First add point(TCC) failed.");
			}
			ret = secondTccPointAction.prepare_add_point(null, accountNo2, point);
			if(!ret){
				//事务回滚
				throw new RuntimeException("Second add point(TCC) failed.");
			}
			System.out.println("嵌套事务结束，txId:" + RuntimeContext.getContext().getTxId());
			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}

	@Override
	@DtxTransaction(bizType="pay-add-point-auto")
	public boolean addPointByAuto(String accountNo1, String accountNo2, int point) {
		boolean ret = false;
		try{
			System.out.println("嵌套事务开启，txId:" + RuntimeContext.getContext().getTxId());

			String txId = RuntimeContext.getContext().getTxId();
			ret = firstAutoPointAction.point_add(txId, accountNo1, point);
			if(!ret){
				//事务回滚
				throw new RuntimeException("First add point (auto) failed.");
			}
			ret = secondAutoPointAction.point_add(txId, accountNo2, point);
			if(!ret){
				//事务回滚
				throw new RuntimeException("Second add point (auto) failed.");
			}
			System.out.println("嵌套事务结束，txId:" + RuntimeContext.getContext().getTxId());

			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}

	public void setFirstAutoPointAction(FirstAutoPointAction firstAutoPointAction) {
		this.firstAutoPointAction = firstAutoPointAction;
	}

	public void setSecondAutoPointAction(SecondAutoPointAction secondAutoPointAction) {
		this.secondAutoPointAction = secondAutoPointAction;
	}

	public void setFirstTccPointAction(FirstTccPointAction firstTccPointAction) {
		this.firstTccPointAction = firstTccPointAction;
	}

	public void setSecondTccPointAction(SecondTccPointAction secondTccPointAction) {
		this.secondTccPointAction = secondTccPointAction;
	}
	

}
