package com.ibeacon.model.image;

import com.ibeacon.model.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 映射到数据库，保存所有beacon信息
 * @author kws
 * @version 1.0 2017年4月24日
 */
@Entity
@Table(name="image_size")
public class ImageSize extends IdEntity {

    private static final long serialVersionUID = 5020509702927340781L;

    @Column(name="width")
    private String width;

    @Column(name="height")
    private String height;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
