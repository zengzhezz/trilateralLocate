package com.ibeacon.model.beacon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ibeacon.model.base.IdEntity;

/**
 * 映射到数据库，保存所有beacon信息
 * @author kws
 * @version 1.0 2017年4月24日
 */
@Entity
@Table(name="ibeacon")
public class Ibeacon extends IdEntity {

    private static final long serialVersionUID = 5020509702927340781L;

    @Column(name="uuid")
    private String uuid;

    @Column(name="type")
    private String type;

    @Column(name="mac")
    private String mac;

    // 该mac对应的名字
    @Column(name="mac_name")
    private String macName;

    @Column(name="rssi")
    private String rssi;

    @Column(name="last_update_time")
    private String lastUpdateTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }



}
