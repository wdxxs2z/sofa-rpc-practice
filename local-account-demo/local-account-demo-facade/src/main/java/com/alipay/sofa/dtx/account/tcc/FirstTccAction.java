package com.alipay.sofa.dtx.account.tcc;

import com.alipay.dtx.client.core.api.domain.BusinessActionContext;
import com.alipay.dtx.client.core.spi.TwoPhaseBusinessAction;
import com.alipay.dtx.remoting.annotation.InjvmRemoting;



/**
 * 扣钱 参与者
 */
@InjvmRemoting
public interface FirstTccAction {
	
	/**
     * 一阶段准备方法，需要打上dtx注解@TwoPhaseBusinessAction
     * 
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "FirstTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare_minus(BusinessActionContext businessActionContext,String accountNo,
                               double amount);
    
    /**
     * 二阶段提交
     * @param businessActionContext
     * @return
     */
    public boolean commit(BusinessActionContext businessActionContext);

    /**
     * 二阶段回滚
     * @param businessActionContext
     * @return
     */
    public boolean rollback(BusinessActionContext businessActionContext);
    
    /**
     * 一阶段准备方法，需要打上dtx注解@TwoPhaseBusinessAction;
     * 
     * 防悬挂实现
     * 
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "FirstTccAction-AntiSuspend", commitMethod = "commit", rollbackMethod = "rollback", antiSuspend = true)
    public boolean prepare_minus_anti_suspend(BusinessActionContext businessActionContext,String accountNo,
                               double amount);
}
