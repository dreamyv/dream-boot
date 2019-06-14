package com.dream.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 集合工具类
 */
@SuppressWarnings("all")
public class SortUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortUtil.class);

    private static final int DEFAULT_SIZE = 1000;

    /**
     * <p>拆分List为固定大小的多个集合</p>
     * <p>推荐使用</p>
     * <p>返回集合的size越小,此方法性能越高</p>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<List<T>> fastSplitList(List<T> baseList, int size) {
        if (baseList == null || baseList.size() == 0) {
            return null;
        }
        if (size <= 0) {
            size = DEFAULT_SIZE;
        }
        int arrSize = baseList.size() % size == 0 ? baseList.size() / size : baseList.size() / size + 1;
        List<List<T>> resultList = new ArrayList<List<T>>();
        for (int i = 0; i < arrSize; i++) {
            if (arrSize - 1 == i) {
                resultList.add((List<T>) new ArrayList<Object>( baseList.subList(i * size, baseList.size())));
            } else {
                resultList.add((List<T>) new ArrayList<Object>( baseList.subList(i * size, size * (i + 1))));
            }
        }
        return resultList;
    }

    /**
     * <p>拆分List为固定大小的多个集合</p>
     * <p>返回集合的size越大,此方法性能越高</p>
     */
    public static <T> List<List<T>> splitList(List<T> baseList, int size) {
        if (baseList == null || baseList.size() == 0) {
            return null;
        }
        if (size <= 0) {
            size = DEFAULT_SIZE;
        }
        List<List<T>> resultList = new ArrayList<List<T>>();
        for (int i = 0; i < baseList.size(); ++i) {
            if (i % size == 0) {
                List<T> result = new ArrayList<T>();
                resultList.add(result);
            }
            resultList.get(i / size).add(baseList.get(i));
        }
        return resultList;
    }


    /**
     * SQL in ( ); 字段拼接.数据库字段值类型的数据
     * @author dongyangyang
     * @param list
     */
    public static String listToString(List<String> list){
        StringBuffer sf = new StringBuffer();
        sf.append("(");
        for (int i = 0; i < list.size()-1; i++) {
            sf.append(list.get(i));
            sf.append(",");
        }
        sf.append(list.get(list.size()-1));
        sf.append(")");
        return sf.toString();
    }

    /**
     * SQL in ( ); 字段拼接.数据库字段字符串类型的数据
     * @author dongyangyang
     * @param list
     */
    public static String listToString2(List<String> list){
        StringBuffer sf = new StringBuffer();
        sf.append("(");
        for (int i = 0; i < list.size()-1; i++) {
            sf.append("'");
            sf.append(list.get(i));
            sf.append("'");
            sf.append(",");
        }
        sf.append("'");
        sf.append(list.get(list.size()-1));
        sf.append("'");
        sf.append(")");
        return sf.toString();
    }

    /**
     * set里元素不重复，用LinkedHashSet是保持原来顺序的
     * 滤重方法
     * @param list
     * @return
     */
    public static List<String> deweight(List<String> list) {
        LinkedHashSet hs = new LinkedHashSet(list);
        List<String> resultlist = new ArrayList<String>();
        Iterator it = hs.iterator();
        while (it.hasNext()) {
            resultlist.add(it.next().toString());
        }
        return resultlist;
    }

    /**
     * 取出2个List中不一样的元素，返回List
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getDiff(List<String> list1,List<String> list2){
        List listAll = new ArrayList();
        List resultList= new ArrayList();
        listAll.addAll(list1);
        listAll.addAll(list2);
        for (int i = 0; i < listAll.size(); i++) {
            if(list1.contains(listAll.get(i))&&list2.contains(listAll.get(i))){
                continue;
            }else{
                resultList.add(listAll.get(i));
            }
        }
        return resultList;
    }

}
