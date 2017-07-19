package com.ibeacon.model.beacon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ibeacon.model.base.IdEntity;

/**
 * 节点实体了类
 * @author zz
 * @version 1.0 2017年3月22日
 */
@Entity
@Table(name="node")
public class Node extends IdEntity {

    // 节点的mac
    @Column(name="mac")
    private String mac;

    // 节点的名称
    @Column(name="name")
    private String name;

    // 节点距离浏览器顶部的相对距离
    @Column(name="node_top")
    private String nodeTop;

    // 节点距离浏览器左边的相对距离
    @Column(name="node_left")
    private String nodeLeft;

    // 节点下面的uuid集合
    @Column(name="uuid_string")
    private String uuidString;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeTop() {
        return nodeTop;
    }

    public void setNodeTop(String nodeTop) {
        this.nodeTop = nodeTop;
    }

    public String getNodeLeft() {
        return nodeLeft;
    }

    public void setNodeLeft(String nodeLeft) {
        this.nodeLeft = nodeLeft;
    }

    public String getUuidString() {
        return uuidString;
    }

    public void setUuidString(String uuidString) {
        this.uuidString = uuidString;
    }

}
