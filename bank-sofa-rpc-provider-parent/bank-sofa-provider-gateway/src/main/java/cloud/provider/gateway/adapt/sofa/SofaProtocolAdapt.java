package cloud.provider.gateway.adapt.sofa;

import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.sofa.rpc.api.GenericService;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SofaProtocolAdapt {

    @Autowired
    private ApplicationConfig applicationConfig;

    private static Map<String, GenericService> cache = new HashMap<>();

    /**
     * [{"java.lang.String":"hello"},{"com.alipay.demo.Person":{"age":10,"username":"tony"}}]
     * */
    public Object doGenericInvoke(String interfaceClass, String methodName, List<Map<String, Object>> params){

        GenericService genericService;

        if (cache.get(interfaceClass) != null) {
            genericService = cache.get(interfaceClass);
        }else {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setProtocol("local");
            registryConfig.setFile("/Users/yuanshaopeng/localFileRegistry/localRegistry.reg");
            registryConfig.setSubscribe(true);
            registryConfig.setConnectTimeout(3000);

            ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>()
                    .setInterfaceId(interfaceClass)
                    .setProtocol("bolt")
                    .setApplication(applicationConfig)
                    .setCheck(false)
                    .setGeneric(true)
                    .setRegistry(registryConfig)
                    .setConnectTimeout(5000)
                    .setTimeout(5000);

            genericService = consumerConfig.refer();
            cache.put(interfaceClass, genericService);
        }

        List<String> types = new ArrayList<>();
        List<Object> args = new ArrayList<>();

        for (Map<String, Object> param : params) {
            for (Map.Entry<String, Object> paramMap : param.entrySet()) {
                GenericObject genericObject = new GenericObject(paramMap.getKey());
                if (paramMap.getValue() instanceof Map){
                    Map<String, Object> attrubits = (Map)paramMap.getValue();
                    for (Map.Entry<String, Object> attrubit : attrubits.entrySet()) {
                        genericObject.putField(attrubit.getKey(), attrubit.getValue());
                    }
                    types.add(paramMap.getKey());
                    args.add(genericObject);
                }else {
                    types.add(paramMap.getKey());
                    args.add(paramMap.getValue());
                }
            }
        }

        String[] array =new String[types.size()];
        Object response = genericService.$genericInvoke(methodName, types.toArray(array), args.toArray());
        return response;
    }
}
