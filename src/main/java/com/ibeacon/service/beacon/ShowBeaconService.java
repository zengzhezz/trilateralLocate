package com.ibeacon.service.beacon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibeacon.model.beacon.ShowBeacon;
import com.ibeacon.service.base.AbstractService;

@Service
public class ShowBeaconService extends AbstractService {

    /**
     * 保存ShowBeacon
     *
     * @param uuid
     * @param uuidName
     * @param left
     * @param top
     */
    public void saveShowBeacon(String uuid, String uuidName, String left,
                               String top) {
        ShowBeacon showBeacon = new ShowBeacon();
        showBeacon.setUuid(uuid);
        showBeacon.setUuidName(uuidName);
        showBeacon.setLocationLeft(left);
        showBeacon.setLocationTop(top);
        this.save(showBeacon);
    }

    /**
     * 根据uuid找到该uuid所有的位置信息
     *
     * @param uuid
     * @return
     */
    public List<ShowBeacon> findAllWithUuid(String uuid) {
        return this.find("from ShowBeacon where uuid = ?", uuid);
    }

    /**
     * 查找对应uuid的历史信息
     *
     * @param uuid
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public List<ShowBeacon> findHistoryWithUuid(String uuid, String start,
                                                String end) throws ParseException {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date startDate = sdfDateFormat.parse(start);
        Date endDate = sdfDateFormat.parse(end);
        List<ShowBeacon> list = this
                .find("from ShowBeacon where uuid = ? and createTime between ? and ? order by createTime asc",
                        uuid, startDate, endDate);
        return list;
    }

}
