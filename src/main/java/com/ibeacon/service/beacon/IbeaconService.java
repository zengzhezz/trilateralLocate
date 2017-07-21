package com.ibeacon.service.beacon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

import com.ibeacon.model.beacon.Ibeacon;
import com.ibeacon.service.base.AbstractService;

@Service
public class IbeaconService extends AbstractService {

    /**
     * 保存Ibeacon信息
     * @param uuid
     * @param mac
     * @param rssi
     * @param type
     * @param lastUpdateTime
     */
    public void saveIbeacon(String uuid, String mac, String rssi,
                            String type,String lastUpdateTime) {
        Ibeacon ibeacon = new Ibeacon();
        ibeacon.setUuid(uuid);
        ibeacon.setMac(mac);
        ibeacon.setRssi(rssi);
        ibeacon.setType(type);
        ibeacon.setLastUpdateTime(lastUpdateTime);
        this.save(ibeacon);
    }

    /**
     * 根据uuid找到该uuid所有的位置信息
     *
     * @param uuid
     * @return
     */
    public List<Ibeacon> findAllWithUuid(String uuid) {
        return this.find("from Ibeacon where uuid = ?", uuid);
    }
    public List<Ibeacon> findAll() {
        return this.find("from Ibeacon");
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
    public List<Ibeacon> findHistoryWithUuid(String start,
                                             String end) throws ParseException {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date startDate = sdfDateFormat.parse(start);
        Date endDate = sdfDateFormat.parse(end);
        List<Ibeacon> list = this
                .find("from Ibeacon where createTime between ? and ? order by createTime desc",
                        startDate, endDate);
        List<Ibeacon> result_list =new ArrayList();
        Ibeacon ibeacon = new Ibeacon();
        int size;
        for(Ibeacon l:list){
            //
            result_list.add(ibeacon);
            size = result_list.size();
            int flag = -1;
            String uuidone=l.getUuid();
            //int uuid1 = Integer.parseInt(uuidone.trim(),16);
            if(size>1){
                for(int i=0;i<size-1;i++){
                    String uuidtwo=result_list.get(i).getUuid();
                    //int uuid2 = Integer.parseInt(uuidtwo.trim(),16);
                    int j = uuidone.compareTo(uuidtwo);
                    if(j<0){
                        flag = i;
                        break;
                    }else if(j>0){
                        continue;
                    }else{

                        flag =-2;
                        break;
                    }
                }
            }else{
                flag=0;
            }

            if(flag>=0){

                size = size-2;

                for(;size>=flag;size--){
                    result_list.set(size+1, result_list.get(size));
                }
                result_list.set(size+1,l);
            }else if(flag==-1){
                result_list.set(size-1,l);
            }else{
                result_list.remove(size-1);
            }

        }


        return result_list;
    }

}
