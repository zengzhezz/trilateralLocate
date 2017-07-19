package com.ibeacon.model.variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibeacon.model.beacon.BeaconModel;
import com.ibeacon.model.beacon.OriginBeaconModel;
import com.ibeacon.model.beacon.WeightBeaconModel;

/**
 * 保存储存于内存中的变量
 * @author zz
 * @version 1.0 2017年4月20日
 */
public class StaticVariables {

    // 创建线程安全的List,一个周期内的原始Beacon数据
    public static List<OriginBeaconModel> originalBeaconList = Collections
            .synchronizedList(new ArrayList<OriginBeaconModel>());

    // 一个周期经过处理后的Beacon数据
    public static List<BeaconModel> handledBeaconList = Collections
            .synchronizedList(new ArrayList<BeaconModel>());

    // 加权定位算法一个周期处理后的数据
    public static List<WeightBeaconModel> weightHandledList = Collections
            .synchronizedList(new ArrayList<WeightBeaconModel>());

    // 地图对应的实际宽度
    volatile public static double real_width;

    // 地图对应的实际高度
    volatile public static double real_height;

}
