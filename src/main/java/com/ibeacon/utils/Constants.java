package com.ibeacon.utils;

/**
 * 存放常数
 * @author zz
 * @version 1.0 2017年3月13日
 */
public class Constants {
	// 定义版本号
	public static final String VERSION = "01";
	// 定义接收到的数据包类型-->正常数据包
	public static final String PACKAGE_NORMAL = "00";
	// 定义接收到的数据包类型-->心跳数据包
	public static final String PACKAGE_HEARTBEAT = "01";
	// 定义接收到的数据包类型-->同步数据包
	public static final String PACKAGE_SYCH = "02";
	// 定义要发送的数据包类型：正常数据包
	public static final Integer DATA_NORMAL = 0;
	// 定义要发送的数据包类型：心跳数据包
	public static final Integer DATA_HEARTBEAT = 1;
	// 定义要发送的数据包类型：同步数据包
	public static final Integer DATA_SYCH = 2;
	// 定义进出事件类型-->无事件
	public static final Integer EVENT_NO = -1;
	// 定义进出事件类型-->进入
	public static final Integer EVENT_IN = 0;
	// 定义进出事件类型-->离开
	public static final Integer EVENT_OUT = 1;
	// 定义数据发送的状态
	public interface PushData {
		// 未发送 0
		static int DATA_UNSENT = 0;
		// 已发送 1
		static int DATA_SENT = 1;
	}

	// 定义数据发送的状态
	public interface PushTimeOut {
		// 未发送 0
		static int connectionTimeout = 2000;
		// 已发送 1
		static int soTimeout = 5000;
	}
}
