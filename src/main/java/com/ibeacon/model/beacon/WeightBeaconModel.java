package com.ibeacon.model.beacon;

import java.util.ArrayList;
import java.util.List;

import com.ibeacon.model.location.LocationModel;

/**
 * 针对三边定位算法的Bean，便于处理数据
 * @author zz
 * @version 1.0 2017年7月17日
 */
public class WeightBeaconModel {

    private String uuid;

    private String uuidName;

    private long lastUpdateTime;

    // 该周期是否需要发送数据
    private int need_send;

    // 保存上次定位的message
    private String lastMessage;

    private double lastLeft;

    private double lastTop;

    private List<LocationModel> locationList = new ArrayList<LocationModel>();

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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getNeed_send() {
        return need_send;
    }

    public void setNeed_send(int need_send) {
        this.need_send = need_send;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
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

    public List<LocationModel> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationModel> locationList) {
        this.locationList = locationList;
    }

    @Override
    public String toString() {
        return "WeightBeaconModel [uuid=" + uuid + ", uuidName=" + uuidName
                + ", lastUpdateTime=" + lastUpdateTime + ", need_send="
                + need_send + ", lastMessage=" + lastMessage + ", lastLeft="
                + lastLeft + ", lastTop=" + lastTop + ", locationList="
                + locationList + "]";
    }

}
