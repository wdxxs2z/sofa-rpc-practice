package cloud.provider;

import cloud.provider.config.MybatisConfig;
import cloud.provider.facade.CallerService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.util.Map;


@org.springframework.boot.autoconfigure.SpringBootApplication
@ImportResource({"classpath*:META-INF/xingye-sofa-rpc-provider/*.xml"})
@ComponentScan(basePackages = "cloud.provider")
@MapperScan(basePackages = "cloud.provider.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
@Import(MybatisConfig.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@RestController
public class SOFABootSpringApplication {

    public static void main(String[] args){

        SpringApplication springApplication = new SpringApplication(SOFABootSpringApplication.class);
        ConfigurableApplicationContext run = springApplication.run(args);
        Map<String, DataSource> beansOfType = run.getBeansOfType(DataSource.class, false, true);
        for (Map.Entry<String, DataSource> bean : beansOfType.entrySet()) {
            System.out.println("bean name: " + bean.getKey() + " ,bean class: " + bean.getValue());
        }
    }

    @Autowired
    CallerService callerService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getMessage() {
        return callerService.message();
    }
}
