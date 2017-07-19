package com.ibeacon.model.beacon;

import java.util.HashMap;
import java.util.Map;

/**
 * beaconModel数据库映射类，一个Beacon可以对应到多了node的距离信息
 * @author zz
 * @version 1.0 2017年4月20日
 */
public class BeaconModel{

    private static final long serialVersionUID = -5346725136199525963L;

    private String uuid;

    private String uuidName;

    private long lastUpdateTime;

    // 该周期是否需要发送数据
    private int need_send;

    // 保存上次定位的message
    private String lastMessage;

    private double lastLeft;

    private double lastTop;

    // 上次是否报警
    private boolean last_Alarm = false;

    private Map<String, String> nodeMap = new HashMap<String, String>();

    public BeaconModel() {
        super();
    }

    public BeaconModel(String uuid, String uuidName, Map<String, String> nodeMap) {
        super();
        this.uuid = uuid;
        this.uuidName = uuidName;
        this.nodeMap = nodeMap;
    }

    public boolean isLast_Alarm() {
        return last_Alarm;
    }

    public void setLast_Alarm(boolean last_Alarm) {
        this.last_Alarm = last_Alarm;
    }

    public int getNeed_send() {
        return need_send;
    }

    public void setNeed_send(int need_send) {
        this.need_send = need_send;
    }

    public double getLastLeft() {
        return lastLeft;
    }

    public void setLastLeft(double lastLeft) {
        this.lastLeft = lastLeft;
    }

    public double getLastTop() {
        return lastTop;
    }

    public void setLastTop(double lastTop) {
        this.lastTop = lastTop;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuidName() {
        return uuidName;
    }

    public void setUuidName(String uuidName) {
        this.uuidName = uuidName;
    }

    public Map<String, String> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, String> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "BeaconModel [uuid=" + uuid + ", uuidName=" + uuidName
                + ", lastUpdateTime=" + lastUpdateTime + ", need_send="
                + need_send + ", lastMessage=" + lastMessage + ", lastLeft="
                + lastLeft + ", lastTop=" + lastTop + ", last_Alarm="
                + last_Alarm + ", nodeMap=" + nodeMap + "]";
    }

}

