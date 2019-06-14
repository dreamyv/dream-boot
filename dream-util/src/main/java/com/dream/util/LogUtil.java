package com.dream.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Log日志分析工具类
 */
@SuppressWarnings("all")
public class LogUtil {

    private static final String filePath = "D:\\test\\logger-20190424.log";
    private static final String regx = "LYYTEST";//模糊查询

    /* 读入TXT文件 */
    public static void readText(String filePath) {
        try {
            File filename = new File(filePath); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = null;
            while ((line = br.readLine()) != null) {
                readLine(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int ds = 0;
    static int up = 0;
    static int donw = 0;
    static Set<String> set = new HashSet<>(3000);
    static List<String> lise = new ArrayList<>(3000);
    static HashMap<String,Integer> map = new HashMap<>(3000);
    public static void readLine(String line) {
//        System.out.println(line);
        if(StringUtils.isEmpty(line)){
            return;
        }
        if(!StringUtils.isEmpty(regx) && line.indexOf(regx) == -1) {
            System.out.println(line);
            return;
        }
        if(line.indexOf("DISPATCHER-NEW") != -1){
            ds++;
        }else if(line.indexOf("UPLOAD-UP") != -1){
            up++;
        }
        else if(line.indexOf("UPLOAD-DOWN") != -1){
            donw++;
        }
        int i = line.indexOf("receive message");
        if(i == -1) {
            return;
        }
        String message = line.substring(i+17,line.length()-1);
        JSONObject jsonObject = JSONObject.parseObject(message).getJSONObject("message");
        StringBuffer sb = new StringBuffer();
        sb.append(jsonObject.getString("vin")).append("-");
        sb.append(jsonObject.getString("command")).append("-");
        String collectTimeStr = jsonObject.getLong("collectTime").toString();
        sb.append(collectTimeStr.substring(0, collectTimeStr.length() - 3)+ "000").append("-");
        sb.append(jsonObject.getString("uploaderName"));
        String key =sb.toString();
        set.add(key);
        lise.add(key);
        if(map.containsKey(key)){
            map.put(key,map.get(key)+1);
        }else{
            map.put(key,1);
        }
        if(listStr.contains(key)){
            System.out.println(line);
        }
    }

    public static List<String> listStr = new ArrayList<String>(){{
//        add("LYYTEST05CD010101-REALTIME_DATA-1556096068000-upload_data");
    }};

    private static void test(Map<String,Integer> map) {
        System.out.println("重复主键:");
        for(String item : map.keySet()){
            if(map.get(item)>3){
                System.out.println(item);
            }
        }
    }

    public static void main(String[] args) {
        readText(filePath);
        System.out.println("DISPATCHER-NEW:"+ds);
        System.out.println("UPLOAD-UP:"+up);
        System.out.println("UPLOAD-DOWN:"+donw);
        System.out.println("collectTime set size:"+set.size());
        System.out.println("collectTime list size:"+lise.size()+",size "+lise.size()/3);
        System.out.println("collectTime map size:"+map.size());
        test(map);
//        System.out.println("LYYTEST05CD010149-REALTIME_DATA-1556012494000-upload_data".equals("LYYTEST05CD010149-REALTIME_DATA-1556012494000-upload_data"));
    }




}
