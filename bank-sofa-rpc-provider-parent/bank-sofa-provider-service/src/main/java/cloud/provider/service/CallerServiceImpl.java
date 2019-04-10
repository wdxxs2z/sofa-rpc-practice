package cloud.provider.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.common.tracer.core.context.span.SofaTracerSpanContext;
import com.alipay.common.tracer.core.context.trace.SofaTraceContext;
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import com.alipay.common.tracer.core.span.LogData;
import com.alipay.common.tracer.core.span.SofaTracerSpan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

public class CallerServiceImpl implements cloud.provider.facade.CallerService {

	@Autowired
	@Qualifier(value = "smartDataSource")
	private DataSource smartDataSource;

	private static final String TEMPLATE = "Hello, %s!";

	private final AtomicLong counter = new AtomicLong();

	@Override
	public String message() {
		return "Hello, Service slitecore";
	}

	@Override
	public Map<String, Object> datasource(String name) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", true);
		resultMap.put("id", counter.incrementAndGet());
		resultMap.put("content", String.format(TEMPLATE, name));
		return resultMap;
	}

	@Override
	public Map<String, Object> create() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Connection cn = smartDataSource.getConnection();
			Statement st = cn.createStatement();
			st.execute("DROP TABLE IF EXISTS test");
			st.execute("create table test(ID INT PRIMARY KEY, NAME VARCHAR(255))");
			resultMap.put("success", true);
			resultMap.put("result", "CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))");
		} catch (Throwable throwable) {
			resultMap.put("success", false);
			resultMap.put("error", throwable.getMessage());
		}
		return resultMap;
	}

	@Override
	public double division(double a, double b) {

		double c = 0.0;

		if (b == 0.0) {
			//人为制造溢出
			try {
				List<String> strList = new ArrayList<String>();
				int item = 0;
				while (true) {
					strList.add(String.valueOf(item++).intern());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		c = a / b;

		return c;
	}

	@Override
	public Map<String, Object> getCurrentTracerInfo() {
		Map<String,Object> traceInfo = new HashMap<>();
		// 1. 通过 SofaTraceContextHolder 上下文获取 SofaTraceContext
		SofaTraceContext sofaTraceContext = SofaTraceContextHolder.getSofaTraceContext();
		// 2. 通过 sofaTraceContext 上下文获取 CurrentSpan
		SofaTracerSpan currentSpan = sofaTraceContext.getCurrentSpan();
		// 3. 通过 CurrentSpan 获取 SofaTracerSpanContext
		SofaTracerSpanContext sofaTracerSpanContext = currentSpan.getSofaTracerSpanContext();
		// 4. 获取相应的traceId
		String traceId = sofaTracerSpanContext.getTraceId();
		traceInfo.put("当前调用的traceid", traceId);
		// 5. 获取当前的spanId
		String spanId = sofaTracerSpanContext.getSpanId();
		traceInfo.put("当前调用的spanid", spanId);
		// 6. 获取当前的tags
		Map<String, String> withStr = currentSpan.getTagsWithStr();
		traceInfo.put("当前调用的一些tags", withStr);
		// 7. 获取当前的logs
		List<LogData> logs = currentSpan.getLogs();
		traceInfo.put("当前调用的logs", logs);
		// 8. 设置一个透传数据
		Map<String, String> bizBaggage = new HashMap<String, String>();
		bizBaggage.put("bizKey","demoTest");
		sofaTracerSpanContext.addBizBaggage(bizBaggage);
		return traceInfo;
	}
}
