package cloud.provider;

import cloud.provider.facade.CallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;
import java.util.Map;

@ImportResource({"classpath*:META-INF/xingye-sofa-rpc-provider/*.xml"})
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SOFABootSpringApplication {

    public static void main(String[] args){

        SpringApplication springApplication = new SpringApplication(SOFABootSpringApplication.class);
        ConfigurableApplicationContext run = springApplication.run(args);
        Map<String, DataSource> beansOfType = run.getBeansOfType(DataSource.class, false, true);
        for (Map.Entry<String, DataSource> bean : beansOfType.entrySet()) {
            System.out.println("bean name: " + bean.getKey() + " ,bean class: " + bean.getValue());
        }
        Map<String, CallerService> services = run.getBeansOfType(CallerService.class);
        for (Map.Entry<String, CallerService> service : services.entrySet()) {
            System.out.println("bean name: " + service.getKey() + " ,bean class: " + service.getValue());
        }
    }
}
