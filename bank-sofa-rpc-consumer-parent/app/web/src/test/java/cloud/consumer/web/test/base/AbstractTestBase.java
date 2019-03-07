package cloud.consumer.web.test.base;

import cloud.consumer.SOFABootWebSpringApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * referenced document: http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-testing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SOFABootWebSpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractTestBase {

    public static final String SOFA_REST_PORT = "8341";

    @Autowired
    public EmbeddedWebApplicationContext server;

    /**
     * 8080
     */
    @LocalServerPort
    public int definedPort;


    @Autowired
    public TestRestTemplate testRestTemplate;

    public String urlHttpPrefix;

    public String sofaRestHttpPrefix;

    @Before
    public void setUp(){
        sofaRestHttpPrefix = "http://localhost:" + SOFA_REST_PORT;
        urlHttpPrefix = "http://localhost:" + definedPort;
        childSetUp();
    }

    /**
     * 测试子类每个方法执行前需要进行的初始化代码放在此
     */
    public abstract void childSetUp();

}

