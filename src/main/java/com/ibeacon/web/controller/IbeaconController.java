package com.ibeacon.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.beacon.Ibeacon;
import com.ibeacon.service.beacon.IbeaconService;



@Controller
@RequestMapping("/history")
public class IbeaconController {

    @Autowired
    private IbeaconService ibeaconService;

    @RequestMapping("/showmy")
    @ResponseBody
    public String getHistory(HttpServletRequest request)
            throws ParseException {

        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        List<Ibeacon> list = ibeaconService.findHistoryWithUuid(
                startTime, endTime);

        // 这样转换主要是解决createTime直接读取出来格式不对
        List<Ibeacon> result_list = new ArrayList<Ibeacon>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        JSONArray jsonArray = new JSONArray();
        for (Ibeacon mid:list) {
            JSONObject jsonobj = new JSONObject();
            String uuid = mid.getUuid();
            String rssi = mid.getRssi();
            String type = mid.getType();
            String mac = mid.getMac();
            String timelast= mid.getLastUpdateTime();
            Date creattime = mid.getCreateTime();


            jsonobj.put("uuid",uuid);
            jsonobj.put("rssi",rssi);
            jsonobj.put("type",type);
            jsonobj.put("mac",mac);
            jsonobj.put("timelast",timelast);

            jsonArray.put(jsonobj);
            System.out.println(jsonobj);

        }

        String result = jsonArray.toString();

//		for (ShowBeacon showBeacon : list) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("locationTop", showBeacon.getLocationTop());
//			map.put("locationLeft", showBeacon.getLocationLeft());
//			map.put("createTime", format.format(showBeacon.getCreateTime()));
//			result_list.add(map);
//		}
        return result;
    }

}
