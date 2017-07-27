package com.ibeacon.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ibeacon.model.base.IdEntity;

/**
 * 保存三边定位算法的距离信息(uuid mac1 dis1 mac2 dis2 mac3 dis3 left top)
 * @author zz
 * @version 1.0 2017年7月14日
 */
@Entity
@Table(name="tri_location")
public class TrilLocation extends IdEntity {

    private static final long serialVersionUID = 1L;

    @Column(name="uuid")
    private String uuid;

    @Column(name="msg")
    private String message;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
