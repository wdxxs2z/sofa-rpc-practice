package cloud.consumer.endpoint.response;

/**
 * the wrapper of the response data in the format of json
 */
public class RestSampleFacadeResp<T> extends cloud.consumer.endpoint.response.AbstractFacadeResp {

    /**
     * the data in the response
     */
    private T data;


    public RestSampleFacadeResp() {
        super(true);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestSampleFacadeResp{" +
                "data=" + data +
                '}';
    }
}
