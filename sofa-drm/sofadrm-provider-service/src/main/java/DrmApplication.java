import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:META-INF/sofadrm-provider-service/*.xml"})
@ComponentScan(value = "com.alipay.demo.sofadrm")
public class DrmApplication {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(DrmApplication.class);
        springApplication.run(args);
    }

}
