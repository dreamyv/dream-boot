package com.dream.core.netty.cache;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公用内存
 */
@SuppressWarnings("all")
public class CommonCache {

    public static Map<String,Channel> snChannelMap = new ConcurrentHashMap<>();

    public static Map<Channel,String> channelSnMap = new ConcurrentHashMap<>();
}
