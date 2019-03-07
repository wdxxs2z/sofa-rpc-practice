package cloud.consumer.endpoint.impl;


import cloud.consumer.endpoint.response.RestSampleFacadeResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.PathParam;

/**
 * rest service interface impl
 * <p>
 * sofa rest resource to deal with rest request
 * <p/>
 */
public class SampleRestFacadeRestImpl implements cloud.consumer.endpoint.facade.SampleRestFacade {

    private static final Logger logger = LoggerFactory.getLogger("MDC-EXAMPLE");

    @Override
    public RestSampleFacadeResp<cloud.consumer.endpoint.model.DemoUserModel> userInfo(String userName) throws cloud.consumer.endpoint.exception.CommonException {

        cloud.consumer.endpoint.model.DemoUserModel demoUserModel = new cloud.consumer.endpoint.model.DemoUserModel();
        demoUserModel.setRealName("Real " + userName);
        demoUserModel.setUserName(userName);

        logger.info("rest mdc example");

        RestSampleFacadeResp<cloud.consumer.endpoint.model.DemoUserModel> result = new RestSampleFacadeResp<cloud.consumer.endpoint.model.DemoUserModel>();
        result.setData(demoUserModel);
        result.setSuccess(true);
        return result;
    }

    @Override
    public RestSampleFacadeResp<Integer> addUserInfo(cloud.consumer.endpoint.model.DemoUserModel user) {
        int id = 1;
        RestSampleFacadeResp<Integer> result = new RestSampleFacadeResp<Integer>();
        result.setData(id);
        result.setSuccess(true);
        return result;
    }

    @Override
    public RestSampleFacadeResp<Integer> deleteUser(String userName) {
        int deletedCount = 1;
        RestSampleFacadeResp<Integer> result = new RestSampleFacadeResp<Integer>();
        result.setData(deletedCount);
        result.setSuccess(true);
        return result;
    }

    @Override
    public RestSampleFacadeResp<Integer> updateUser(String userName, cloud.consumer.endpoint.model.DemoUserModel demoUserModel) {
        int updatedCount = 1;
        RestSampleFacadeResp<Integer> result = new RestSampleFacadeResp<Integer>();
        result.setData(updatedCount);
        result.setSuccess(true);
        return result;
    }
}
