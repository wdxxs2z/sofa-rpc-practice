package cloud.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * SOFABootWebSpringApplication
 */
@ImportResource({"classpath*:META-INF/xingye-sofa-rpc-consumer/*.xml"})
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SOFABootWebSpringApplication {

    // init the logger
    private static final Logger logger = LoggerFactory.getLogger(SOFABootWebSpringApplication.class);

    public static void main(String[] args){

        SpringApplication springApplication = new SpringApplication(SOFABootWebSpringApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

        if (logger.isInfoEnabled()){
            logger.info("application start");
        }

        cloud.consumer.endpoint.facade.SampleRestFacade sampleRestFacade = applicationContext.getBean("sampleRestFacadeRest", cloud.consumer.endpoint.impl.SampleRestFacadeRestImpl.class);

        cloud.consumer.endpoint.model.DemoUserModel demoUserModel = new cloud.consumer.endpoint.model.DemoUserModel();
        demoUserModel.setUserId(12);
        demoUserModel.setRealName("realName");
        demoUserModel.setUserName("userName");

        cloud.consumer.endpoint.response.RestSampleFacadeResp<Integer> resp =  sampleRestFacade.addUserInfo(demoUserModel);

        if (logger.isInfoEnabled()) {
            logger.info("the resp id is " + resp.getData());
        }

    }
}
