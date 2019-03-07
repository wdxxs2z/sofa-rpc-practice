package cloud.consumer.callback;

import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.invoke.SofaResponseCallback;
import com.alipay.sofa.rpc.core.request.RequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivisionCallBack implements SofaResponseCallback {

    private static final Logger logger = LoggerFactory.getLogger(DivisionCallBack.class);

    @Override
    public void onAppResponse(Object appResponse, String methodName, RequestBase request) {
        logger.info("division callback message: {}", appResponse);
    }

    @Override
    public void onAppException(Throwable throwable, String methodName, RequestBase request) {
        logger.info("division app exception: {}", throwable);
    }

    @Override
    public void onSofaException(SofaRpcException sofaException, String methodName, RequestBase request) {
        logger.info("division sofa exception: {}", sofaException);
    }
}
