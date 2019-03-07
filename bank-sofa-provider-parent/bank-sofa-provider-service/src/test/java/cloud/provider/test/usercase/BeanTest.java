package cloud.provider.test.usercase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BeanTest
 */
public class BeanTest extends cloud.provider.test.base.AbstractTestBase {

    @Autowired
    private cloud.provider.facade.CallerService sampleService;

    @Test
    public void testRpcSimple(){
        String result = sampleService.message();
        Assert.assertEquals("Hello, Service slitecore", result);
        Assert.assertTrue(result != null && result.length() > 0);
    }

    @Override
    protected void childSetUp() {

    }
}
