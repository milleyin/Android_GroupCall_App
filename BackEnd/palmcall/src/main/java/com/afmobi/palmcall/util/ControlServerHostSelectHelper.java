package com.afmobi.palmcall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ControlServerHostSelectHelper {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private Map<String, String> csHostMap;

    public String selectHostByBtoken(String btoken) {
        String prefixBtoken = btoken.substring(0, 2);

        String csHost = csHostMap.get(prefixBtoken);

        if(csHost == null) {
            log.error("未知的csHost，btoken为：" + btoken);
            throw new RuntimeException("未知的csHost，btoken为：" + btoken);
        }

        return csHost;
    }

}
