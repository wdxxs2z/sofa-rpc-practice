package com.alipay.sofa.dtx.account.tcc;

import com.alipay.dtx.client.core.api.domain.BusinessActionContext;
import com.alipay.dtx.client.core.spi.TwoPhaseBusinessAction;
import com.alipay.dtx.remoting.annotation.InjvmRemoting;

/**
 * 加钱 参与者
 * 
 * @author zhangsen
 *
 */
@InjvmRemoting
public interface SecondTccAction {

	 /**
     * 一阶段准备方法
     * 
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "SecondTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare_add(BusinessActionContext businessActionContext,String accountNo, double amount);

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
    
}
