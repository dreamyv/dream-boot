package com.dream.util;

import java.util.UUID;

@SuppressWarnings("all")
public class UUIDUtil {

    /**
     * 通过jdk自带的uuid生成器生成36位的uuid
     */
    public static String get36UUID() {
        // 使用JDK自带的UUID生成器
        return UUID.randomUUID().toString();
    }

    /**
     * 通过jdk自带的uuid生成器生成32位的uuid
     */
    public static String get32UUID() {
        // 使用JDK自带的UUID生成器
        return UUID.randomUUID().toString().replace("-","");
    }

}
