package com.ibeacon.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 内部类，一个map的比较器
 * @author zz
 * @version 1.0 2017年4月24日
 */
public class MapValueComparator implements Comparator<Map.Entry<String, String>> {
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {
        return Integer.valueOf(me1.getValue()) - Integer.valueOf(me2.getValue());
    }
}
