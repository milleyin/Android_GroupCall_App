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
@Profile("afrika")
public class AfrikaConfig extends AbstractConfig{

    @Override
    public boolean showErrorStr() {
        return false;
    }

    @Override
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(
                "jdbc:mysql://172.17.30.53:3306/palmcall?autoReconnect=true&useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("palmcall");
        dataSource.setPassword("palmcall!@#GSls54");
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
        jedisConnectionFactory.setHostName("172.17.30.54");
        jedisConnectionFactory.setPort(6379);
        return jedisConnectionFactory;
    }

    @Override
    public Map<String, String> csHostMap() {
        Map<String, String> csHostMap = new HashMap<>();
        csHostMap.put("01", "http://172.17.0.61:6666");
        csHostMap.put("0I", "http://172.17.0.112:6666");
        csHostMap.put("0J", "http://172.17.0.141:6666");
        csHostMap.put("0K", "http://172.17.0.145:6666");
        csHostMap.put("0L", "http://172.17.0.146:6666");
        csHostMap.put("0M", "http://172.17.0.147:6666");
        csHostMap.put("0N", "http://172.17.0.148:6666");
        csHostMap.put("0O", "http://172.17.0.149:6666");
        csHostMap.put("0P", "http://172.17.0.167:6666");
        csHostMap.put("0Q", "http://172.17.0.194:6666");
        csHostMap.put("0R", "http://172.17.0.195:6666");
        csHostMap.put("0S", "http://172.17.10.54:6666");
        return csHostMap;
    }

    @Override
    public String palmcallPushUrl() {
        return "http://internal-put00.ir.afmobi:5577/public_account/";
    }

    @Override
    public String MinutesRechargePalmCoinUrl() {
        return "http://172.17.0.249:8080/ProfitServer/campaign";
    }

    @Override
    public String getPalmchatProfileUrl() {
        return "http://172.17.0.22:8080/UserCenter/user/userBriefInfo";
    }

    @Override
    public String showPicDownloadUrl() {
        return "http://54.72.72.212:34588/showpic.php";
    }

}
