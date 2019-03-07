package cloud.provider.facade;

import java.util.Map;

public interface CallerService {

    String message();

	Map<String, Object> datasource(String name);

	Map<String, Object> create();
	
	double division(double a, double b);
	
	Map<String, Object> getCurrentTracerInfo();
}

