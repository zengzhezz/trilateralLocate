package com.ibeacon.model.beacon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ibeacon.model.base.IdEntity;

/**
 * 映射到数据库，保存显示到页面的所有beacon信息,uuid和坐标
 * @author zz
 * @version 1.0 2017年4月24日
 */
@Entity
@Table(name="show_beacon")
public class ShowBeacon extends IdEntity {

    private static final long serialVersionUID = 5020509702927340781L;

    @Column(name="uuid")
    private String uuid;

    @Column(name="uuidname")
    private String uuidName;

    @Column(name="location_left")
    private String locationLeft;

    @Column(name="location_top")
    private String locationTop;

//	@Column(name="last_update_time")
//	private String lastUpdateTime;

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

    public String getLocationLeft() {
        return locationLeft;
    }

    public void setLocationLeft(String locationLeft) {
        this.locationLeft = locationLeft;
    }

    public String getLocationTop() {
        return locationTop;
    }

    public void setLocationTop(String locationTop) {
        this.locationTop = locationTop;
    }

//	public String getLastUpdateTime() {
//		return lastUpdateTime;
//	}
//
//	public void setLastUpdateTime(String lastUpdateTime) {
//		this.lastUpdateTime = lastUpdateTime;
//	}

}
