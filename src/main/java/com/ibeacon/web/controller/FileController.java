package com.ibeacon.web.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.beacon.ShowBeacon;
import com.ibeacon.model.location.TrilLocation;
import com.ibeacon.model.msginfo.MessageInfo;
import com.ibeacon.model.variables.StaticVariables;
import com.ibeacon.service.beacon.ShowBeaconService;
import com.ibeacon.service.location.TrilLocationService;
import com.ibeacon.utils.FileUtils;

@Controller
@RequestMapping("/file")
public class FileController {

//    private static Logger log = LogManager.getLogger("FileController");

    @Autowired
    private ShowBeaconService showBeaconService;

    @Autowired
    private TrilLocationService trilLocationService;

    @RequestMapping("/add")
    @ResponseBody
    public MessageInfo getHistory(HttpServletRequest request)
            throws ParseException {
        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        String uuid = request.getParameter("uuid");
        String path = request.getSession().getServletContext().getRealPath("") + "/opt/";
        // 首先导出文件到服务器
        ExpTxtOfUuidLocation(uuid, startTime, endTime, path);
        // 然后从服务器下载刚刚导出的文件
        return new MessageInfo(0,"success");
    }

    @RequestMapping("/gettrillocate")
    @ResponseBody
    public MessageInfo getTrillocate(HttpServletRequest request)
            throws ParseException {
        String startTime = request.getParameter("start");
        String endTime = request.getParameter("end");
        String uuid = request.getParameter("uuid");
        String path = request.getSession().getServletContext().getRealPath("") + "/opt/";
        // 首先导出文件到服务器
        ExpTrilLocate(startTime, endTime, path, uuid);
        // 然后从服务器下载刚刚导出的文件
        return new MessageInfo(0,"success");
    }

    /**
     * 导出Uuid的位置信息到opt/(uuid).txt文件中
     * @param uuid
     * @param start
     * @param end
     * @param path
     */
    public void ExpTxtOfUuidLocation(String uuid, String start, String end, String path) {
        FileUtils.writeContent("", path, uuid + ".txt", false);
        try {
            List<ShowBeacon> list = showBeaconService.findHistoryWithUuid(uuid,
                    start, end);
            if(!CollectionUtils.isEmpty(list)){
                for (ShowBeacon l : list) {
                    double left = Double.valueOf(l.getLocationLeft())
                            * StaticVariables.real_width;
                    double top = Double.valueOf(l.getLocationTop())
                            * StaticVariables.real_height;
                    DecimalFormat df = new DecimalFormat("0.00");
                    FileUtils.writeContent(uuid + "," + df.format(left) + "," + df.format(top)
                            + "," + l.getCreateTime() + "\r\n", path, uuid
                            + ".txt", true);
//                    log.debug(uuid + "," + df.format(left) + "," + df.format(top) + ","
//                            + l.getCreateTime() + "\r\n" + path + uuid + ".txt");
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 在服务器上生成trillocate.txt文件
     * @param start
     * @param end
     * @param path
     */
    public void ExpTrilLocate(String start, String end, String path, String uuid) {
        FileUtils.writeContent("", path,  uuid + "_locate.txt", false);
        try {
            List<TrilLocation> list = trilLocationService.findHistory(uuid, start, end);
            if(!CollectionUtils.isEmpty(list)){
                for (TrilLocation locate : list) {
                    String content = locate.getUuid() + " " + locate.getMessage();
                    FileUtils.writeContent(content+"\r\n", path, uuid + "_locate.txt" , true);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
