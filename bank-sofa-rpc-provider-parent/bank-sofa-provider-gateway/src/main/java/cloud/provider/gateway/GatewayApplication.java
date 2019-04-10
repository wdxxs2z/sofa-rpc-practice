package cloud.provider.gateway;

import cloud.provider.gateway.adapt.sofa.SofaProtocolAdapt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class GatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(GatewayApplication.class, args);

    }

    /**
     * {
     *   "interfaceName": "cloud.provider.facade.CallerService",
     *   "method": "datasource",
     *   "content": {
     *     "java.lang.String": "hello"
     *   }
     * }
     * */
    @RequestMapping(value = "/gateway", method = RequestMethod.POST)
    public Object revertRequest(@RequestBody String requestJson){

        Map<String, Object> requestObject = JSON.parseObject(requestJson, new TypeReference<Map<String, Object>>() {
        });

        String interfaceName = (String)requestObject.get("interfaceName");

        String method = (String)requestObject.get("method");

        List<Map<String, Object>> args = (List<Map<String, Object>>)requestObject.get("content");

        SofaProtocolAdapt adapt = new SofaProtocolAdapt();

        Object response = adapt.doGenericInvoke(interfaceName, method, args);

        return response;
    }

}