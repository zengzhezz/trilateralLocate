package com.ibeacon.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.label.Label;
import com.ibeacon.model.msginfo.MessageInfo;
import com.ibeacon.service.label.LabelService;

@Controller
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 添加Label
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/add_label")
    public MessageInfo addLabel(HttpServletRequest request){
        String uuid = request.getParameter("uuid");
        String uuidName = request.getParameter("name");
        String type = request.getParameter("type");
        String description = request.getParameter("description");
        if(labelService.checkLabelExist(uuid)){
            return new MessageInfo(1, "该标签的uuid已经存在");
        }else{
            labelService.saveLabel(uuid, uuidName, Integer.valueOf(type), description);
            return new MessageInfo(0, "success");
        }
    }

    /**
     * 删除指定uuid的Label
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete_label")
    public MessageInfo delateLabel(HttpServletRequest request){
        String uuid = request.getParameter("uuid");
        if(labelService.deleteLabel(uuid)){
            return new MessageInfo(0,"success");
        }else{
            return new MessageInfo(1, "删除失败...");
        }
    }

    /**
     * 查找所有的Label
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/find_all")
    public List<Label> findAllLabel(){
        List<Label> list = labelService.findAllLabel();
        return list;
    }

    /**
     * 得到uuid的labelType
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/get_labelType")
    public String getLabelType(HttpServletRequest request){
        String uuid = request.getParameter("uuid");
        return String.valueOf(labelService.findLabelTypeByUuid(uuid));
    }

    /**
     * 得到uuid的Label
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/get_label")
    public Label getLabel(HttpServletRequest request){
        String uuid = request.getParameter("uuid");
        return labelService.findLabelByUuid(uuid);
    }

}
