package com.ibeacon.model.location;

/**
 * 定义距离模型，定义一个mac地址和标签距离该mac的rssi
 * @author zz
 * @version 1.0 2017年7月17日
 */
public class LocationModel {

    private String mac;

    private int rssi;

    public LocationModel() {
        super();
    }

    public LocationModel(String mac, int rssi) {
        super();
        this.mac = mac;
        this.rssi = rssi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

}
