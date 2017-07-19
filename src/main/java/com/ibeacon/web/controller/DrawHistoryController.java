package com.ibeacon.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.beacon.ShowBeacon;
import com.ibeacon.service.beacon.ShowBeaconService;

@Controller
@RequestMapping("/history")
public class DrawHistoryController {

    @Autowired
    private ShowBeaconService showBeaconService;

    @RequestMapping("/show")
    @ResponseBody
    public List<Map<String, String>> getHistory(HttpServletRequest request)
            throws ParseException {
        String uuid = request.getParameter("uuid");
        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        List<ShowBeacon> list = showBeaconService.findHistoryWithUuid(uuid,
                startTime, endTime);
        // 这样转换主要是解决createTime直接读取出来格式不对
        List<Map<String, String>> result_list = new ArrayList<Map<String, String>>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        // 做一次中值滤波
        for (int i = 0; i < list.size() - 3; i++) {
            Map<String, String> map = new HashMap<String, String>();
            double top = (Double.valueOf(list.get(i).getLocationTop())
                    + Double.valueOf(list.get(i + 1).getLocationTop())
                    + Double.valueOf(list.get(i + 2).getLocationTop())+Double.valueOf(list.get(i + 3).getLocationTop()))/4;
            double left = (Double.valueOf(list.get(i).getLocationLeft())
                    + Double.valueOf(list.get(i + 1).getLocationLeft())
                    + Double.valueOf(list.get(i + 2).getLocationLeft())+Double.valueOf(list.get(i + 3).getLocationTop()))/4;
            map.put("locationTop", String.valueOf(top));
            map.put("locationLeft", String.valueOf(left));
            map.put("createTime", format.format(list.get(i).getCreateTime()));
            result_list.add(map);
        }
//		for (ShowBeacon showBeacon : list) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("locationTop", showBeacon.getLocationTop());
//			map.put("locationLeft", showBeacon.getLocationLeft());
//			map.put("createTime", format.format(showBeacon.getCreateTime()));
//			result_list.add(map);
//		}
        return result_list;
    }

}
