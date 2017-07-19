package com.ibeacon.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ibeacon.model.image.ImageSize;
import com.ibeacon.service.image.ImageSizeService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ibeacon.model.msginfo.MessageInfo;
import com.ibeacon.model.variables.StaticVariables;

@Controller
@RequestMapping("/location/")
public class ImageSizeController {

    @Autowired
    private ImageSizeService imageSizeService;

    /**
     * 更新ImageSize信息，若本来没有则添加
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "save.do" })
    public MessageInfo updateImageSize(HttpServletRequest request){
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        // 更新内存中的数据
        StaticVariables.real_width = Double.valueOf(width);
        StaticVariables.real_height = Double.valueOf(height);
        if(imageSizeService.findImageSize()!=null){
            imageSizeService.deleteImageSize();
        }
        imageSizeService.saveImageSize(width, height);
        return new MessageInfo(0,"success");
    }

    /**
     * 查找ImageSize信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "find.do" })
    public ImageSize findImageSize(HttpServletRequest request){
        ImageSize size = imageSizeService.findImageSize();
        return size;

    }

}
