package cloud.consumer.web.test.usercase;

import cloud.consumer.endpoint.response.RestSampleFacadeResp;
import cloud.consumer.web.test.base.AbstractTestBase;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * test for everything except web module
 * you should perform click on the web pages
 *
 */
public class SofaRestServiceTest extends AbstractTestBase {

    @Test
    public void testSofaRestGet(){
        assertNotNull(testRestTemplate);
        String restUrl = sofaRestHttpPrefix + "/webapi/users/xiaoming";

        //TODO 注意 RestSampleFacadeResp 一定要有默认构造函数
        ResponseEntity<RestSampleFacadeResp> response = testRestTemplate.getForEntity(restUrl, RestSampleFacadeResp.class);
        RestSampleFacadeResp restSampleFacadeResp = response.getBody();
        assertTrue(restSampleFacadeResp.isSuccess());
        // TODO 注意:这里只是测试使用,使用的是 jackson 不支持泛型的反序列化,所以被反序列化为 map,这里是仅供测试使用,使用详情参考文档:http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-testing
        Map<String, Integer> demoUserModel = (Map<String, Integer>) restSampleFacadeResp.getData();
        assertTrue(demoUserModel.get("userId") >= 0);
    }

    @Override
    public void childSetUp() {

    }
}
