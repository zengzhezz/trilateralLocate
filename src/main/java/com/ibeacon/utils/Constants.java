package com.ibeacon.utils;

/**
 * 存放常数
 * @author zz
 * @version 1.0 2017年3月13日
 */
public class Constants {

	public static final String VERSION = "a0";
	//定义数据包类型：正常数据包
	public static final Integer DATA_NORMAL = 0;
	//定义数据包类型：同步数据包
	public static final Integer DATA_SYCH = 1;
	// 定义进出事件类型-->无事件
	public static final Integer EVENT_NO = -1;
	// 定义进出事件类型-->进入
	public static final Integer EVENT_IN = 0;
	// 定义进出事件类型-->离开
	public static final Integer EVENT_OUT = 1;
	// 定义图和实际距离的比值
	public static double SCALE = 0.003;
	// 定义图片长宽比
	public static double IMAGE_RATIO = 0.367475;
	// 定义数据发送的状态
	public interface PushData {
		// 未发送 0
        int DATA_UNSENT = 0;
		// 已发送 1
        int DATA_SENT = 1;
	}

	// 定义数据发送的状态
	public interface PushTimeOut {
		// 未发送 0
        int connectionTimeout = 2000;
		// 已发送 1
        int soTimeout = 5000;
	}

	// 定义标签的类型
	public interface LabelType {
		// 标签类型：人
        int TYPE_PERSON = 1;
		// 标签类型：狗
        int TYPE_DOG = 2;
		// 标签类型：车
        int TYPE_CAR = 3;
		// 标签类型：非法人员
        int TYPE_ILLEAGE = 4;
	}

	// 定义是否需要发送更新信息到前端
	public interface needSend {
		// 需要发送
        int NEED = 0;
		// 不需要
        int NO_NEED = 1;
	}

	public static final String CURRENTUSER = "User";
}
