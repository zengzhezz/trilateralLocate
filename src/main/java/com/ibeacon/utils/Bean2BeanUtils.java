package com.ibeacon.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *<pre>
 * 作用:
 * 注意:
 * 其他:
 *</pre>
 *
 * @author  mark
 * @version 1.0, 2016-4-27
 * @see
 * @since
 */
public class Bean2BeanUtils {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object covertBean2Bean(Object from, Object to) {
        Class fClass = from.getClass();
        Class tClass = to.getClass();
        // 拿to所有属性（如果有继承，父类属性拿不到）
        Field[] cFields = tClass.getDeclaredFields();
        try {
            for (Field field : cFields) {
                String cKey = field.getName();
                // 确定第一个字母大写
                cKey = cKey.substring(0, 1).toUpperCase() + cKey.substring(1);

                Method fMethod;
                Object fValue;
                try {
                    fMethod = fClass.getMethod("get" + cKey);// public方法
                    fValue = fMethod.invoke(from);// 取getfKey的值
                } catch (Exception e) {
                    // 如果from没有此属性的get方法，跳过
                    continue;
                }

                try {
                    // 用fMethod.getReturnType()，而不用fValue.getClass()
                    // 为了保证get方法时，参数类型是基本类型而传入对象时会找不到方法
                    Method cMethod = tClass.getMethod("set" + cKey, fMethod.getReturnType());
                    cMethod.invoke(to, fValue);
                } catch (Exception e) {
                    // 如果to没有此属性set方法，跳过
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return to;
    }
}
