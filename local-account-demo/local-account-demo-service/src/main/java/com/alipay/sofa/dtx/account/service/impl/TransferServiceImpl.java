package com.alipay.sofa.dtx.account.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.sofa.dtx.account.auto.FirstAutoAction;
import com.alipay.sofa.dtx.account.auto.SecondAutoAction;
import com.alipay.sofa.dtx.account.service.PointService;
import com.alipay.sofa.dtx.account.service.TransferService;
import com.alipay.sofa.dtx.account.tcc.FirstTccAction;
import com.alipay.sofa.dtx.account.tcc.SecondTccAction;
import com.alipay.sofa.dtx.client.aop.annotation.DtxTransaction;
import com.alipay.sofa.dtx.client.context.RuntimeContext;

/**
 * 转账服务
 * 
 * @author zhangsen
 *
 */
public class TransferServiceImpl implements TransferService {

	protected final static Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
	
	private FirstTccAction firstTccAction;
	
	private SecondTccAction secondTccAction;
	
	private FirstAutoAction firstAutoAction;
	
	private SecondAutoAction secondAutoAction;
	
	private PointService pointService;
	
	@Override
	@DtxTransaction(bizType="single-transfer-by-tcc")
	public boolean transferByTcc(String from, String to, double amount) {
		//获取当前事务上下文的事务ID号
		System.out.println("事务开启，txId:" + RuntimeContext.getContext().getTxId());
		try{
			//准备从账户from扣钱
//			boolean ret = firstTccAction.prepare_minus(null, from, amount);
			boolean ret = firstTccAction.prepare_minus_anti_suspend(null, from, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("firstTccAction failed.");
			}
			logger.info("一阶段-准备从账户-{}扣钱{}", from, amount);
			//准备从账户to加钱
			ret = secondTccAction.prepare_add(null, to, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("secondTccAction failed.");
			}
			logger.info("一阶段-准备从账户-{}加钱{}", to, amount);
			System.out.println("事务结束，txId:" + RuntimeContext.getContext().getTxId());

			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	

	@Override
	@DtxTransaction(bizType="single-transfer-by-auto")
	public boolean transferByAuto(String from, String to, double amount) {
		boolean ret = false;
		System.out.println("事务开启，txId:" + RuntimeContext.getContext().getTxId());

		try{
			ret = firstAutoAction.amount_minus(from, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("firstAutoAction failed.");
			}
			ret = secondAutoAction.amount_add(to, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("secondAutoAction failed.");
			}
			System.out.println("事务结束，txId:" + RuntimeContext.getContext().getTxId());
			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	/**
	 * TCC 模式转账，并开启嵌套事务记录积分
	 */
	@Override
	@DtxTransaction(bizType="transfer-nest-by-tcc")
	public boolean transferByTccNest(String from, String to, double amount) {
		try{
			System.out.println("事务开启，txId:" + RuntimeContext.getContext().getTxId());

			boolean ret = firstTccAction.prepare_minus(null, from, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("firstTccAction(TCC) failed.");
			}
			
			ret = secondTccAction.prepare_add(null, to, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("secondTccAction(TCC) failed.");
			}
			
			//开启嵌套事务，调用积分事务
			ret = pointService.addPointByTcc(from, to, 1);
			if(!ret){
				//事务回滚
				throw new RuntimeException("Nest pointService(TCC) failed.");
			}
			System.out.println("事务结束，txId:" + RuntimeContext.getContext().getTxId());

			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}

	/**
	 * FMT模式转账，并开发嵌套事务记录积分
	 */
	@Override
	@DtxTransaction(bizType="transfer-nest-by-auto")
	public boolean transferByAutoNest(String from, String to, double amount) {
		boolean ret = false;
		try{
			System.out.println("事务开启，txId:" + RuntimeContext.getContext().getTxId());

			ret = firstAutoAction.amount_minus(from, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("firstAutoAction (auto) failed.");
			}
			ret = secondAutoAction.amount_add(to, amount);
			if(!ret){
				//事务回滚
				throw new RuntimeException("secondAutoAction (auto) failed.");
			}
			
			//开启嵌套事务，调用积分事务
			ret = pointService.addPointByAuto(from, to, 1);
			if(!ret){
				//事务回滚
				throw new RuntimeException("Nest pointService (auto) failed.");
			}
			System.out.println("事务结束，txId:" + RuntimeContext.getContext().getTxId());

			return ret;
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}


	public void setFirstTccAction(FirstTccAction firstTccAction) {
		this.firstTccAction = firstTccAction;
	}

	public void setSecondTccAction(SecondTccAction secondTccAction) {
		this.secondTccAction = secondTccAction;
	}

	public void setFirstAutoAction(FirstAutoAction firstAutoAction) {
		this.firstAutoAction = firstAutoAction;
	}


	public void setSecondAutoAction(SecondAutoAction secondAutoAction) {
		this.secondAutoAction = secondAutoAction;
	}

	public void setPointService(PointService pointService) {
		this.pointService = pointService;
	}
	
	
}
