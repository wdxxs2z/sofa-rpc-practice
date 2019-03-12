package com.alipay.sofa.dtx.account.point;

import com.alipay.dtx.client.core.api.domain.BusinessActionContext;
import com.alipay.dtx.client.core.spi.TwoPhaseBusinessAction;
import com.alipay.dtx.remoting.annotation.InjvmRemoting;

@InjvmRemoting
public interface SecondTccPointAction {

	 /**
     * 一阶段方法，注意要打上xts的标注哦
     * 
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "SecondTccPointAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare_add_point(BusinessActionContext businessActionContext,String accountNo, double point);

    public boolean commit(BusinessActionContext businessActionContext);

    public boolean rollback(BusinessActionContext businessActionContext);
    
}
