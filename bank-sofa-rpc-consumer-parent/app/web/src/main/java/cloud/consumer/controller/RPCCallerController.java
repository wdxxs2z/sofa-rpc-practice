package cloud.consumer.controller;

import java.util.Map;

import com.alipay.sofa.rpc.api.future.SofaResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cloud.provider.facade.CallerService;

@RestController
@RequestMapping("/callrpc")
public class RPCCallerController {
	
	private static final Logger logger = LoggerFactory.getLogger("MDC-EXAMPLE");
	
	@Autowired
	private CallerService callerService;
	
	@RequestMapping("/message")
    String futureMessage() {
        this.callerService.message();
		String result = "";
		try {
			result = (String)SofaResponseFuture.getResponse(20000, true);
			logger.info("future message result: " + result);
		}catch (Exception e) {
        	e.printStackTrace();
		}
        return result;
    }
	
	
	@RequestMapping("/datasource")
    public Map<String, Object> datasource(@RequestParam(value = "name", defaultValue = "SOFATracer DataSource DEMO") String name) {
        return callerService.datasource(name);
    }
	
	@RequestMapping("/create")
    public Map<String, Object> create() {
		return callerService.create();
    }
	
	@RequestMapping("division")
	public double division(@RequestParam(value = "a") double a, @RequestParam(value = "b") double b) {
		
		return callerService.division(a, b);
		
	}
	
	@RequestMapping("/tracer_info")
	public Map<String,Object> getCurrentTracerInfo() {
		return callerService.getCurrentTracerInfo();
	}

}
