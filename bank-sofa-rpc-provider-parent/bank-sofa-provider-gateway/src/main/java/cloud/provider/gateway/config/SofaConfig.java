package cloud.provider.gateway.config;

import com.alipay.sofa.rpc.common.utils.StringUtils;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SofaConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${com.alipay.env:shared}")
    private String alipayEnv;

    @Value("${com.alipay.instanceid}")
    private String alipayInstanceId;

    @Value("${com.antcloud.antvip.endpoint}")
    private String antcloudEndpoint;

    @Value("${com.antcloud.mw.access}")
    private String antcloudAccess;

    @Value("${com.antcloud.mw.secret}")
    private String antcloudSecret;

    @Value("${run.mode}")
    private String runMode;

    @Value("${com.alipay.sofa.rpc.registry.address:local}")
    private String registryAddress;

    @Bean
    public ApplicationConfig applicationConfig() {

        if (StringUtils.isEmpty(runMode)) {
            System.setProperty("run.mode", "DEV");
        }else if (!StringUtils.isEmpty(runMode) && StringUtils.equals(runMode, "DEV")) {
            System.setProperty("run.mode", runMode);
        }else {
            System.setProperty("run.mode", runMode);
            System.setProperty("com.alipay.env", alipayEnv);
            System.setProperty("com.alipay.instanceid", alipayInstanceId);
            System.setProperty("com.antcloud.antvip.endpoint", antcloudEndpoint);

            if (StringUtils.isEmpty(antcloudAccess)) {
                System.setProperty("com.antcloud.mw.access", antcloudAccess);
            }
            if (StringUtils.isEmpty(antcloudSecret)) {
                System.setProperty("com.antcloud.mw.secret", antcloudSecret);
            }
        }

        ApplicationConfig appConfiguration = new ApplicationConfig();
        appConfiguration.setAppName(appName);
        return appConfiguration;
    }
}
