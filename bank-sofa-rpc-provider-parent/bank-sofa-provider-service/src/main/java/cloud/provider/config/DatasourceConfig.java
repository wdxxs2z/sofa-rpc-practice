package cloud.provider.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alipay.sofa.extds.SmartDataSource;
import com.alipay.sofa.extds.trace.ClientTracer;
import com.alipay.sofa.extds.tracer.DefaultClientTracer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

//@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceConfig {

    private String appName;
    private String dbType;
    private String username;
    private String password;
    private String url;
    private String driverClassName;
    private String database;
    private Integer initialSize;
    private Integer maxActive;

//    @Bean
//    @Qualifier("smartDataSource")
//    @Primary
//    public DataSource smartDataSource() throws SQLException{
//        SmartDataSource smartDataSource = new SmartDataSource();
//        smartDataSource.setAppName(appName);
//        smartDataSource.setDatabase(database);
//        smartDataSource.setDbType(dbType);
//        smartDataSource.setClientTracer(clientTracer());
//        smartDataSource.setDelegate(simpleDataSource());
//        smartDataSource.init();
//        return smartDataSource;
//    }
//
//    @Bean
//    public DataSource simpleDataSource() throws SQLException {
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(url);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//        datasource.setInitialSize(initialSize);
//        datasource.init();
//        return datasource;
//    }
//
//    @Bean
//    @Primary
//    public ClientTracer clientTracer(){
//        return new DefaultClientTracer();
//    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }
}
