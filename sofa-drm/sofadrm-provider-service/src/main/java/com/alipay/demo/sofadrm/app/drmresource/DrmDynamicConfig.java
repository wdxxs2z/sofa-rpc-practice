package com.alipay.demo.sofadrm.app.drmresource;


import com.alipay.drm.client.DRMClient;
import com.alipay.drm.client.api.annotation.AfterUpdate;
import com.alipay.drm.client.api.annotation.BeforeUpdate;
import com.alipay.drm.client.api.annotation.DAttribute;
import com.alipay.drm.client.api.annotation.DObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DObject(appName = "sofadrm-provider-service", id = "com.alipay.demo.sofadrm.app.drmresource.DrmDynamicConfig")
public class DrmDynamicConfig {

    private static final Logger logger = LoggerFactory.getLogger(DrmDynamicConfig.class);

    @DAttribute
    private String name;

    @DAttribute
    private int age;

    @DAttribute
    private boolean open;

    public void init() {
        DRMClient.getInstance().register(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @BeforeUpdate
    public void before(String key, Object value) {
        logger.info("前置回调" + key + ":" + value);
    }
    @AfterUpdate
    public void after(String key, Object value) {
        logger.info("后置回调" + key + ":" + value);
    }

}
