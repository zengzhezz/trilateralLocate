package com.ibeacon.model.label;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ibeacon.model.base.IdEntity;

/**
 * 标签管理实体类，在Person基础上增加了标签类型
 * @author zz
 * @version 1.0 2017年5月4日
 */
@Entity
@Table(name="label")
public class Label extends IdEntity {

    private static final long serialVersionUID = -1150971920148226328L;

    @Column(name="uuid")
    private String uuid;

    @Column(name="uuidName")
    private String uuidName;

    @Column(name="label_type")
    private Integer labelType;

    @Column(name="description")
    private String description;

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

    public Integer getLabelType() {
        return labelType;
    }

    public void setLabelType(Integer labelType) {
        this.labelType = labelType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
