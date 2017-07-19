package com.ibeacon.model.beacon;

/**
 * beaconModel原始数据类，便于处理数据，保存uuid,uuidName,mac,rssi
 * @author zz
 * @version 1.0 2017年4月20日
 */
public class OriginBeaconModel {

    private String uuid;

    private String uuidName;

    private String lastUpdateTime;

    private String mac;

    private String rssi;

    public OriginBeaconModel() {
        super();
    }

    public OriginBeaconModel(String uuid, String uuidName, String mac,
                             String rssi, String lastUpdateTime) {
        super();
        this.uuid = uuid;
        this.uuidName = uuidName;
        this.mac = mac;
        this.rssi = rssi;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime(){
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime){
        this.lastUpdateTime = lastUpdateTime;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "OriginBeaconModel [uuid=" + uuid + ", uuidName=" + uuidName
                + ", lastUpdateTime=" + lastUpdateTime + ", mac=" + mac
                + ", rssi=" + rssi + "]";
    }

}
