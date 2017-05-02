package com.afmobi.palmchat.constant;

public class ReplaceConstant {

    public static final int MAX_SIZE = 6100;
    public static final String TARGET_NMAE = "{$targetName}";
    public static final String CHAT_TIMES = "{$chatTimes}";
    public static final String TARGET_SEX = "{$targetSex}";
    public static final String TARGET_AGE = "{$targetAge}";
    public static final String TARGET_AFID = "{$targetAfID}";
    public static final String TARGET_PASSWORD = "{$targetPassword}";
    public static final String TARGET_NUMBER = "{$targetNumber}";
    public static final String TARGET_MESSAGE_COUNT = "{$targetMessageCount}";
    public static final int MAX_SIGN_SIZE = 100;
    public static final String TARGET_EMAIL = "{$targetEmail}";
    /*建议以后所有用到占位符的地方，统一都用这个字段，不用根据不同含义再去重新定义，这样规范统一，也方便产品那边进行八国语言翻译*/
    public static final String PLACEHOLDER = "{$placeholder}";
    public static final String TARGET_FOR_REPLACE = "{$target}";//用于替换的文本 从20160824起 都用这种格式 不能用XXX


}
