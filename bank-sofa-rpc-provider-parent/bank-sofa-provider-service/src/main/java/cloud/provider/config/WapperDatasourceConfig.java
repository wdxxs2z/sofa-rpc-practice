package cloud.provider.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alipay.sofa.tracer.plugins.datasource.SmartDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySource(value = {"classpath:config/application.properties"},encoding="utf-8")
public class WapperDatasourceConfig {

    @Value("${spring.self.datasource.username}")
    private String username;

    @Value("${spring.self.datasource.password}")
    private String password;

    @Value("${spring.self.datasource.url}")
    private String url;

    @Value("${spring.self.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.self.datasource.initialSize}")
    private Integer initialSize;

    @Value("${spring.self.datasource.maxActive}")
    private Integer maxActive;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.self.datasource.dbType}")
    private String dbType;

    @Value("${spring.self.datasource.database}")
    private String database;

    @Bean
    public DruidDataSource druidDataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.init();
        return datasource;
    }

    @Bean(name = "smartDataSource")
    @Qualifier("smartDataSource")
    @Primary
    public DataSource smartDataSource() throws SQLException {
        SmartDataSource smartDataSource = new SmartDataSource();
        smartDataSource.setAppName(appName);
        smartDataSource.setDbType(dbType);
        smartDataSource.setDatabase(database);
        smartDataSource.setEnableTrace(true);
        smartDataSource.setDelegate(druidDataSource());
        smartDataSource.init();
        return smartDataSource;
    }
}
