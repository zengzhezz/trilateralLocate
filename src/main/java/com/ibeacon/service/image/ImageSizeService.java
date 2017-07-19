package com.ibeacon.service.image;

import com.ibeacon.model.image.ImageSize;
import com.ibeacon.service.base.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by zz on 2017/7/19.
 */
@Service
public class ImageSizeService extends AbstractService{

    /**
     * 保存图片尺寸信息
     */
    public void saveImageSize(String width,String height){
        ImageSize size = new ImageSize();
        size.setHeight(height);
        size.setWidth(width);
        this.save(size);
    }

    /**
     * 从数据库中查找图片的尺寸信息
     * @return
     */
    public ImageSize findImageSize(){
        return (ImageSize) this.find("from ImageSize").get(0);
    }

    /**
     * 删除图片尺寸信息
     * @return
     */
    public boolean deleteImageSize(){
        ImageSize size = findImageSize();
        if(size != null){
            this.delete(size);
            return true;
        }else{
            return false;
        }
    }

}
