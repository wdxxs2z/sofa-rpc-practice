package cloud.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource({"classpath*:META-INF/xingye-sofa-rpc-provider/*.xml"})
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SOFABootSpringApplication {

    public static void main(String[] args){

        SpringApplication springApplication = new SpringApplication(SOFABootSpringApplication.class);
        springApplication.run(args);
    }
}
