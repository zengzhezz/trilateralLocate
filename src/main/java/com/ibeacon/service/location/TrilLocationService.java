package com.ibeacon.service.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibeacon.model.location.TrilLocation;
import com.ibeacon.service.base.AbstractService;

@Service
public class TrilLocationService extends AbstractService {

    /**
     * 保存一条数据
     * @param msg
     */
    public void saveTrilLocation(String uuid, String msg){
        TrilLocation location = new TrilLocation();
        location.setUuid(uuid);
        location.setMessage(msg);
        this.save(location);
    }

    /**
     * 查找一段时间的TrilLocation数据
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public List<TrilLocation> findHistory(String uuid, String start, String end) throws ParseException{
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date startDate = sdfDateFormat.parse(start);
        Date endDate = sdfDateFormat.parse(end);
        List<TrilLocation> list = this
                .find("from TrilLocation where uuid = ? and createTime between ? and ? order by createTime asc",
                        uuid, startDate, endDate);
        return list;
    }

}
