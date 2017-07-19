package com.ibeacon.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.web.websocket.Websocket;
import com.ibeacon.model.beacon.OriginBeaconModel;
import com.ibeacon.model.msginfo.MessageInfo;
import com.ibeacon.model.variables.StaticVariables;
import com.ibeacon.service.beacon.IbeaconService;
import com.ibeacon.service.label.LabelService;
import com.ibeacon.service.node.NodeService;
import com.ibeacon.utils.Constants;
import com.ibeacon.utils.ConvertUtils;
import com.ibeacon.utils.HttpUtils;

/**
 * 处理从慧联接口传过来的原始数据
 *
 * @author zz
 * @version 1.0 2017年3月13日
 */
@Controller
@RequestMapping("/api/")
public class ReceiveDataController {

    private static Logger log = Logger.getLogger("ReceiveDataController");

    @Autowired
    private IbeaconService ibeaconService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private LabelService labelService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "data.do" })
    public MessageInfo GetOriginData(HttpServletRequest request) {
        try {
            // 得到post过来的json字符串
            String jsonData = HttpUtils.getStringFromHttpRequest(request).toLowerCase();
            log.debug("receive data:" + jsonData);
            JSONObject obj = new JSONObject(jsonData);
            // 新建一些字符串存取数据
            String mac = obj.getString("mac");
            String appeui = obj.getString("appeui");
            String data = obj.getString("data");
            String lut = obj.getString("last_update_time");

            // 数据确定为定位包
            if(data.substring(0,2).equals(Constants.VERSION)){
                for(int i=2;i < data.length();){
                    // 解析data
                    int num = Integer.parseInt(data.substring(i,i+2),16);
                    log.debug("标签数:"+num);
                    i=i+2;
                    String uuid1 = data.substring(i,i+4);
                    i=i+4;

                    for(int j=0;j<num;j++){
                        String uuid2 = data.substring(i,i+4);
                        String rssi = data.substring(i+4,i+6);
                        String rssilast = getrssilast(rssi);
                        String type = getType(rssi);
                        String uuid =uuid1+uuid2;
                        log.debug("写入数据到ibeacon..."+uuid+","+rssi+","+type+","+lut);
                        ibeaconService.saveIbeacon(uuid, mac, rssilast, type, ConvertUtils.formatTimestamp(lut));
                        i=i+6;
                        String uuidName = labelService.findUuidNameByUuid(uuid);
                        //把日志信息传到前端,信息格式("command","uuid","rssi","type","time")
                        Websocket.sendMessageToAll("flow,"+uuid+","+rssilast+","+type+","+ConvertUtils.formatTimestamp(lut)+","+mac);
                        log.debug("写入数据到originalBeaconList...");
                        StaticVariables.originalBeaconList.add(new OriginBeaconModel(uuid,uuidName,mac,rssilast,ConvertUtils.formatTimestamp(lut)));
                    }

                }

            }else{
                log.debug("数据格式错误:" + data);
            }
        } catch (JSONException e) {
            log.error(e);
            log.debug("return failed....");
            return new MessageInfo(1, "failed");
        }
        log.debug("return success....");
        return new MessageInfo(0, "success");
    }

    public String getType(String rssi){

        int type;
        type = Integer.parseInt(rssi.substring(0,1),16);
        if(type>=8){
            return "静止";
        }
        return "运动";
    }
    public String getrssilast(String rssi){
        int type = Integer.parseInt(rssi.substring(0,1),16);
        String rssilast;
        if(type>=8){
            int rssiint =Integer.parseInt(rssi.substring(0),16)-128;
            rssilast = String.valueOf(rssiint);

        }else{
            int rssiint =Integer.parseInt(rssi.substring(0),16);
            rssilast = String.valueOf(rssiint);
        }
        return rssilast;
    }
}
