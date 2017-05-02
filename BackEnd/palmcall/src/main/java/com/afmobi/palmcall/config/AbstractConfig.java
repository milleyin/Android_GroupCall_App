package com.afmobi.palmcall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

public abstract class AbstractConfig {

    @Bean
    public String uploadAudioPath() {
        return "/userdata1/www/palmcall/audio";
    }

    @Bean
    public String uploadCoverImgPath() {
        return "/userdata1/www/palmcall/cover";
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(20485760);
        return commonsMultipartResolver;
    }

    @Bean
    public abstract boolean showErrorStr();

    //------------------------------mysqlConfig----------------------------------

    @Bean(initMethod = "init", destroyMethod = "close")
    public abstract DataSource dataSource() throws SQLException;

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    //------------------------------redisConfig----------------------------------

    @Bean
    public abstract JedisConnectionFactory jedisConnectionFactory();

    @Bean
    public StringRedisSerializer stringSerializer(){
        return new StringRedisSerializer();
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory, StringRedisSerializer stringRedisSerializer){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

    //------------------------------urlConfig----------------------------------
    //Ken 检查session pctoken有效性部分
    @Bean
    public abstract Map<String, String>  csHostMap();

    @Bean
    public String checkPalmCallTokenUri() {
        return "/cpctoken/";
    }

    @Bean
    public String updatePalmCallTokenUri() {
        return "/upctoken/";
    }

    //Ken palmcall
    @Bean
    public abstract String palmcallPushUrl();

    //波哥 分钟数换成PalmCoin
    @Bean
    public abstract String MinutesRechargePalmCoinUrl();

    //账号一体化 获取profile
    @Bean
    public abstract String getPalmchatProfileUrl();

    //下载头像url
    @Bean
    public abstract String showPicDownloadUrl();

}
