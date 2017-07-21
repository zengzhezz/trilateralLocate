package com.ibeacon.thread;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import Jama.Matrix;

import com.ibeacon.model.beacon.Node;
import com.ibeacon.model.beacon.OriginBeaconModel;
import com.ibeacon.model.beacon.WeightBeaconModel;
import com.ibeacon.model.location.LocationModel;
import com.ibeacon.model.variables.StaticVariables;
import com.ibeacon.service.beacon.ShowBeaconService;
import com.ibeacon.service.label.LabelService;
import com.ibeacon.service.location.TrilLocationService;
import com.ibeacon.service.node.NodeService;
import com.ibeacon.utils.Algorithm;
import com.ibeacon.utils.CombineAlgorithm;
import com.ibeacon.utils.Constants;
import com.ibeacon.utils.SpringContextHolder;
import com.ibeacon.web.websocket.Websocket;

/**
 * 该线程对应HandledMsgThread，使用加权三边定位算法得到定位信息
 * notice: 该线程与HandledMsgThread只能启动其一
 * @author zz
 * @version 1.0 2017年7月17日
 */
public class WeightTrilateralThread extends Thread{

    private static Logger log = Logger.getLogger("WeightTrilateralThread");

    // 定义总权值
    private double totalWeight = 0;

    // 记录当前时间
    private Date this_date;

    @Override
    public void run() {
        while(true){
            try {
                // 休眠5s
                Thread.sleep(5000);
                log.debug("start thread...");
                // 选取当前系统时间作为lastUpdateTime
                this_date = new Date();
                if (!CollectionUtils.isEmpty(StaticVariables.originalBeaconList)) {
                    log.debug("originalBeaconList接收到数据，现在写入数据到handledBeaconList...");
                    for (OriginBeaconModel model : StaticVariables.originalBeaconList) {
                        String uuid = model.getUuid();
                        String uuidName = model.getUuidName();
                        String mac = model.getMac();
                        String rssi = model.getRssi();
                        updateMsgToHandledList(uuid, uuidName, mac, rssi,Constants.needSend.NEED,
                                this_date.getTime());
                    }
                }else{
                    log.debug("originalBeaconList无数据...");
                }
                if (!CollectionUtils.isEmpty(StaticVariables.weightHandledList)) {
                    log.debug(StaticVariables.weightHandledList);
                    for (WeightBeaconModel model : StaticVariables.weightHandledList) {
                        Locate locate = getLocation(model);
                        if(locate!=null){
                            DecimalFormat df = new DecimalFormat("0.0000");
                            // 发送位置数据到前端
                            // 得到uuid对应的标签类型
                            Integer labelType = SpringContextHolder.getBean(
                                    LabelService.class).findLabelTypeByUuid(
                                    model.getUuid());
                            String description = SpringContextHolder.getBean(
                                    LabelService.class).findDescriptionByUuid(
                                    model.getUuid());
                            Websocket.sendMessageToAll("location,"+model.getUuid()+","+model.getUuidName()+
                                    "," + labelType + "," + description + "," + df.format(locate.getxAxis())+
                                    "," + df.format(locate.getyAxis()));
                            // 保存位置信息
                            ShowBeaconService showBeaconService = SpringContextHolder
                                    .getBean(ShowBeaconService.class);
                            showBeaconService.saveShowBeacon(model.getUuid(),
                                    model.getUuidName(), df.format(locate.getxAxis()),
                                    df.format(locate.getyAxis()));
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                clear();
                log.debug("end thread...");
            }
        }
    }

    /**
     * 清楚缓存list
     */
    private void clear() {
        StaticVariables.originalBeaconList.clear();
        StaticVariables.weightHandledList.clear();
    }

    /**
     * 根据WeightBeaconModel得到uuid的位置坐标
     * @param model
     * @return
     */
    private Locate getLocation(WeightBeaconModel model) {
        // 定义最后输出的坐标信息
        Locate locate = new Locate();
        // 得到一共有多少节点收到此标签的信息
        int numNodes = model.getLocationList().size();
        // 如果数目小于3，无法定位
        if(numNodes<3){
            return null;
        }
        // 得到组合数组
        Integer[] group = new Integer[numNodes];
        for(int i = 0; i < numNodes; i++){
            group[i] = i;
        }
        // 求组合数
        CombineAlgorithm ca = null;
        try {
            ca = new CombineAlgorithm(group,3);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.debug(e);
        }
        Object[][] c = ca.getResult();
        double[] tempLocation = new double[2];
        for(int i = 0; i<c.length; i++){
            // 得到三个LocationModel用于计算坐标
            List<LocationModel> lmlist = new ArrayList<LocationModel>();
            for(int j = 0; j<3; j++){
                LocationModel lm = model.getLocationList().get(j);
                lmlist.add(lm);
            }
            // 使用三个LocationModel计算坐标
            double[] weightLocate = calculate(lmlist);
            tempLocation[0]+=weightLocate[0];
            tempLocation[1]+=weightLocate[1];
        }
        locate.setxAxis((tempLocation[0]/totalWeight)/StaticVariables.real_width);
        locate.setyAxis((tempLocation[1]/totalWeight)/StaticVariables.real_height);
        // 重置totalWeight
        totalWeight = 0;
        return locate;
    }

    /**
     * 求出通过该组基站距离加权后的坐标
     *
     * @param  bases 接收到的一组基站对象列表(此处列表中的基站应当是id各异的)
     * @return  返回通过该组基站距离加权后的坐标
     */
    public double[] calculate(List<LocationModel> lm){
		/*基站的mac与坐标, double[0]表示x, double[1]表示y*/
        final Map<String, double[]> basesLocation =new HashMap<String, double[]>();
        double[] rawLocation;
		/*距离数组*/
        double[] distanceArray = new double[3];
        String[] macs = new String[3];
        for(int i = 0; i < lm.size(); i++){
            LocationModel model = lm.get(i);
            macs[i] = model.getMac();
            distanceArray[i] = Algorithm.calDistanceWithRssi(model.getRssi());
        }
        // 得到mac的基准坐标
        NodeService nodeService = SpringContextHolder.getBean(NodeService.class);
        for(int i = 0; i < macs.length; i++){
            Node node = nodeService.findNodeByMac(macs[i]);
            double[] loc = new double[2];
            loc[0] = Double.valueOf(node.getNodeLeft()) * StaticVariables.real_width;
            loc[1] = Double.valueOf(node.getNodeTop()) * StaticVariables.real_height;
            basesLocation.put(macs[i], loc);
        }
        int disArrayLength = distanceArray.length;
        double[][] a = new double[2][2];
        double[][] b = new double[2][1];
		/*数组a初始化*/
        for(int i = 0; i < 2; i ++ ) {
            a[i][0] = 2*(basesLocation.get(macs[i])[0]-basesLocation.get(macs[2])[0]);
            a[i][1] = 2*(basesLocation.get(macs[i])[1]-basesLocation.get(macs[2])[1]);
        }
		/*数组b初始化*/
        for(int i = 0; i < 2; i ++ ) {
            b[i][0] = Math.pow(basesLocation.get(macs[i])[0], 2)
                    - Math.pow(basesLocation.get(macs[2])[0], 2)
                    + Math.pow(basesLocation.get(macs[i])[1], 2)
                    - Math.pow(basesLocation.get(macs[2])[1], 2)
                    + Math.pow(distanceArray[disArrayLength-1], 2)
                    - Math.pow(distanceArray[i],2);
        }
		/*将数组封装成矩阵*/
        Matrix b1 = new Matrix(b);
        Matrix a1 = new Matrix(a);
		/*求矩阵的转置*/
        Matrix a2  = a1.transpose();
		/*求矩阵a1与矩阵a1转置矩阵a2的乘积*/
        Matrix tmpMatrix1 = a2.times(a1);
        Matrix reTmpMatrix1 = tmpMatrix1.inverse();
        Matrix tmpMatrix2 = reTmpMatrix1.times(a2);
		/*中间结果乘以最后的b1矩阵*/
        Matrix resultMatrix = tmpMatrix2.times(b1);
        double[][] resultArray = resultMatrix.getArray();
        rawLocation = new double[2];
		/*给未加权的结果数组赋值*/
        for(int i = 0; i < 2; i++) {
            rawLocation[i] = resultArray[i][0];
        }
		/*对应的权值*/
        double weight = 0;
        for(int i = 0; i<3; i++){
            weight+=(1.0/distanceArray[i]);
        }
        totalWeight+=weight;
        double[] resultloc = new double[2];
		/*计算加权过后的坐标*/
        for(int i = 0; i < 2; i++) {
            resultloc[i] = rawLocation[i]*weight;
        }
        // 保存相关数据便于分析
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        DecimalFormat df = new DecimalFormat("0.0000");
        StringBuilder sb = new StringBuilder();
        sb.append(format.format(this_date)+" ");
        for(int i = 0; i < 3; i++){
            sb.append(macs[i]+" "+distanceArray[i]+" ");
        }
        sb.append(df.format(resultloc[0])+" "+df.format(resultloc[1]));
        TrilLocationService trilLocationService = SpringContextHolder
                .getBean(TrilLocationService.class);
        trilLocationService.saveTrilLocation(sb.toString());
        return resultloc;
    }

    /**
     * 根据originList里的数据更新HandedList
     * @param uuid
     * @param uuidName
     * @param mac
     * @param rssi
     * @param need
     * @param time
     */
    private void updateMsgToHandledList(String uuid, String uuidName,
                                        String mac, String rssi, int need, long time) {
        // the flag where this uuid's message is already in handledList.
        boolean isExist = false;
        // check if handedList already has the uuid message. if no, add it ;
        // otherwise update it if rssi is smaller.
        for (WeightBeaconModel model : StaticVariables.weightHandledList) {
            if (model.getUuid().equals(uuid)){
                isExist = true;
                model.setUuidName(uuidName);
                model.setLastUpdateTime(time);
                model.setNeed_send(need);
                boolean isExstMac = false;
                // check if LocationList always has the message,
                // if yes then update to the smaller rssi, otherwise add a new LocationModel
                for(LocationModel beaconModel: model.getLocationList()){
                    if(beaconModel.getMac().equals(mac)){
                        isExstMac = true;
                        if(Integer.valueOf(rssi) < beaconModel.getRssi()){
                            beaconModel.setRssi(Integer.valueOf(rssi));
                        }
                    }
                }
                if(!isExstMac){
                    model.getLocationList().add(new LocationModel(mac, Integer.valueOf(rssi)));
                }
            }
        }
        if(!isExist){
            WeightBeaconModel weightModel = new WeightBeaconModel();
            weightModel.setUuid(uuid);
            weightModel.setUuidName(uuidName);
            weightModel.setLastUpdateTime(time);
            weightModel.setNeed_send(need);
            List<LocationModel> list = new ArrayList<LocationModel>();
            list.add(new LocationModel(mac,Integer.valueOf(rssi)));
            weightModel.setLocationList(list);
            StaticVariables.weightHandledList.add(weightModel);
        }
    }

    /**
     * 内部类，定义位置对象，具有x和y属性
     * @author zz
     * @version 1.0 2017年7月20日
     */
    private class Locate{

        private double xAxis;
        private double yAxis;
        public double getxAxis() {
            return xAxis;
        }
        public void setxAxis(double xAxis) {
            this.xAxis = xAxis;
        }
        public double getyAxis() {
            return yAxis;
        }
        public void setyAxis(double yAxis) {
            this.yAxis = yAxis;
        }

    }

}
