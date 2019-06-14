package com.dream.util.http;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

/**
 * Http请求工具类
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static OutputStreamWriter out = null;
    private static BufferedReader in = null;

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url,Map<String,String> param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = buildUrl(url,param);;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送GET请求出现异常！" + e);
        } finally {
            close();
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        String result = "";
        try {
            // 打开和URL之间的连接
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送POST请求出现异常！" + e);
        } finally {
            close();
        }
        return result;
    }

    /**
     * 关闭输出流、输入流
     */
    private static void close() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 初始化URL,将参数拼接到URL之后
     *
     * @param originalUrl 原始的URL
     * @param map         参数mao
     * @return 拼接后的get形式URL
     */
    public static String buildUrl(String originalUrl, Map<String, String> map) {
        if (!CollectionUtils.isEmpty(map)) {
            StringBuilder builder = new StringBuilder(originalUrl);
            if (StringUtils.contains(originalUrl, "?")) {
                builder.append("&");
            } else {
                builder.append("?");
            }
            builder.append(buildUrl(map));
            return builder.toString();
        }
        return originalUrl;
    }

    public static String buildUrl(Map<String, String> map) {
        if (!CollectionUtils.isEmpty(map)) {
            StringBuilder builder = new StringBuilder();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                builder.append(key);
                builder.append("=");
                builder.append(map.get(key));
                builder.append("&");
            }
            String newOriginalUrl = builder.toString();
            newOriginalUrl = newOriginalUrl.substring(0, newOriginalUrl.length() - 1);
            return newOriginalUrl;
        }
        return "";
    }


}
