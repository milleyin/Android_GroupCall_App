package com.afmobi.palmcall.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
public class TestConfig extends AbstractConfig {

    @Override
    public boolean showErrorStr() {
        return true;
    }

    @Override
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/palmcall?autoReconnect=true&useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(100);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(15000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(true);
        dataSource.setFilters("stat,wall");
        dataSource.setConnectionProperties("druid.stat.mergeSql=true");
        return dataSource;
    }

    @Override
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        return jedisConnectionFactory;
    }

    @Override
    public Map<String, String> csHostMap() {
        Map<String, String> csHostMap = new HashMap<>();
        csHostMap.put("01", "http://127.0.0.1:8090");
        csHostMap.put("0N", "http://127.0.0.1:8090");
        return csHostMap;
    }

    @Override
    public String palmcallPushUrl() {
        return "http://internal-put00.ir.afmobi:5577/public_account/";
    }

    @Override
    public String MinutesRechargePalmCoinUrl() {
        return "http://127.0.0.1:8090/ProfitServer/campaign";
    }

    @Override
    public String getPalmchatProfileUrl() {
        return "http://127.0.0.1:8090/UserCenter/user/userBriefInfo";
    }

    @Override
    public String showPicDownloadUrl() {
        return "http://52.76.193.207:34588/showpic.php";
    }

}
