package cloud.provider.gateway.adapt.sofa;

import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.sofa.rpc.api.GenericService;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SofaProtocolAdapt {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private RegistryConfig registryConfig;

    private static Map<String, GenericService> cache = new HashMap<>();

    /**
     * [{"java.lang.String":"hello"},{"com.alipay.demo.Person":{"age":10,"username":"tony"}}]
     * */
    public Object doGenericInvoke(String interfaceClass, String methodName, List<Map<String, Object>> params){

        if (applicationConfig == null || registryConfig == null) {
            return "";
        }

        GenericService genericService;

        if (cache.get(interfaceClass) != null) {
            genericService = cache.get(interfaceClass);
        }else {

            ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>()
                    .setInterfaceId(interfaceClass)
                    .setApplication(applicationConfig)
                    .setGeneric(true)
                    .setTimeout(10000)
                    .setDirectUrl("bolt://127.0.0.1:12200?appName=generic-server");
                    //.setRegistry(registryConfig);

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

        String[] genericTypes =new String[types.size()];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date start = new Date();
        System.out.println(simpleDateFormat.format(start));

        Object response = genericService.$genericInvoke(methodName,
                types.toArray(genericTypes),
                args.toArray());


        Date end = new Date();
        System.out.println(simpleDateFormat.format(end));
        return response;
    }
}
