package com.ibeacon.thread;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import Jama.Matrix;
import com.ibeacon.model.beacon.BeaconModel;
import com.ibeacon.model.beacon.OriginBeaconModel;
import com.ibeacon.model.beacon.Node;
import com.ibeacon.model.variables.StaticVariables;
import com.ibeacon.service.beacon.ShowBeaconService;
import com.ibeacon.service.label.LabelService;
import com.ibeacon.service.location.TrilLocationService;
import com.ibeacon.service.node.NodeService;
import com.ibeacon.utils.Algorithm;
import com.ibeacon.utils.Constants;
import com.ibeacon.utils.MapValueComparator;
import com.ibeacon.utils.SpringContextHolder;
import com.ibeacon.web.websocket.Websocket;

/**
 * 处理数据线程，周期为6s，处理OriginBeaconModel中的数据，保存在BeaconModel中，发送到前端并清空这两个List
 *
 * @author zz
 * @version 1.0 2017年4月20日
 */
public class TrilateralThread extends Thread {

    private static Logger log = Logger.getLogger("HandleMsgThread");

    private List<NodeModel> nodeList = new ArrayList<TrilateralThread.NodeModel>();

    private String formatTimeStamp;

    @Override
    public void run() {
        while (true) {
            try {
                // 休眠6s
                Thread.sleep(6000);
                log.debug("start thread...");
                // Websocket.sendMessageToAll("delete_all,label");
                // 选取当前系统时间作为lastUpdateTime
                Date this_date = new Date();
                if (!CollectionUtils
                        .isEmpty(StaticVariables.originalBeaconList)) {
                    log.debug("写入数据到handledBeaconList...");
                    for (OriginBeaconModel model : StaticVariables.originalBeaconList) {
                        String uuid = model.getUuid();
                        String uuidName = model.getUuidName();
                        String mac = model.getMac();
                        String rssi = model.getRssi();
                        formatTimeStamp = model.getLastUpdateTime();
                        if (!isUuidExsitInBeaconModel(uuid)) {
                            addMsgToHandledList(uuid, uuidName, mac, rssi,
                                    Constants.needSend.NEED,
                                    this_date.getTime());
                        } else {
                            updateMsgToHandledList(uuid, mac, rssi,
                                    Constants.needSend.NEED,
                                    this_date.getTime());
                        }
                    }
                } else {
                    log.debug("originalBeaconList无数据...");
                    Websocket.sendMessageToAll("alarm,0");
                }
                if (!CollectionUtils.isEmpty(StaticVariables.handledBeaconList)) {
                    // 删除多余节点rssi信息
                    removeSurplusData();
                    log.debug("开始执行sortHandledListMap");
                    // 把HandledList的数据按Map中值的大小排序
                    sortHandledListMap();
                    // // 保存到数据库
                    // saveBeaconList();
                    log.debug("做电子围栏检查");
                    // 发送数据到前端
                    log.debug("发数据到前端");
                    sendStringFromHandledList();
                }
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            } finally {
                // 清空缓存
                clearList();
                log.debug("end thread...");
            }
        }
    }

    /**
     * 判断该uuid是否在handledBeaconList中已存在
     *
     * @param uuid
     * @return
     */
    public boolean isUuidExsitInBeaconModel(String uuid) {
        if (!CollectionUtils.isEmpty(StaticVariables.handledBeaconList)) {
            for (BeaconModel beaconModel : StaticVariables.handledBeaconList) {
                if (beaconModel.getUuid().equals(uuid))
                    return true;
            }
        }
        return false;
    }

    /**
     * 添加一个uuid的信息到HandledBeaconList中
     *
     * @param uuid
     * @param uuidName
     * @param mac
     * @param rssi
     */
    public void addMsgToHandledList(String uuid, String uuidName, String mac,
                                    String rssi, int needSend, long lastUpdateTime) {
        BeaconModel model = new BeaconModel();
        model.setUuid(uuid);
        model.setUuidName(uuidName);
        model.setLastUpdateTime(lastUpdateTime);
        model.setNeed_send(needSend);
        Map<String, String> map = new HashMap<String, String>();
        map.put(mac, rssi);
        model.setNodeMap(map);
        StaticVariables.handledBeaconList.add(model);
    }

    /**
     * 更新HandledBeaconList已存在的uuid的数据,若传入的mac的rssi已存在，则更新成较小的，否则添加此mac的rssi如map中
     *
     * @param uuid
     * @param mac
     * @param rssi
     */
    public void updateMsgToHandledList(String uuid, String mac, String rssi,
                                       int needSend, long lastUpdateTime) {
        for (BeaconModel model : StaticVariables.handledBeaconList) {
            if (model.getUuid().equals(uuid)) {
                model.setLastUpdateTime(lastUpdateTime);
                model.setNeed_send(needSend);
                Map<String, String> map = model.getNodeMap();
                if (map.containsKey(mac)) {
                    // 若传入的rssi比原本的rssi小，则更新为传入的rssi
                    if (Integer.valueOf(map.get(mac)) > Integer.valueOf(rssi)) {
                        map.put(mac, rssi);
                    }
                } else {
                    // map里没有此mac的信息，则添加此信息
                    map.put(mac, rssi);
                }
            }
        }
    }

    /**
     * HandledBeaconList中一个uuid只能保存3个节点的距离信息，多余的数据需要删除，找到信号(rssi最小)最好的两个节点
     */
    public void removeSurplusData() {
        log.debug("HandledBeaconList处理前的数据："
                + StaticVariables.handledBeaconList.toString());
        for (BeaconModel model : StaticVariables.handledBeaconList) {
            while (model.getNodeMap().size() > 3) {
                Collection<String> collection = model.getNodeMap().values();
                // 定义一个Set保存信号最好的两个rssi
                String max = getMaxFromCollection(collection);
                collection.remove(max);
            }
        }
    }

    /**
     * 找到String集合里最大的数
     *
     * @param collection
     * @return
     */
    public String getMaxFromCollection(Collection<String> collection) {
        String result = "0";
        for (String rssi : collection) {
            result = String.valueOf(Math.max(Integer.valueOf(rssi),
                    Integer.valueOf(result)));
        }
        return result.equals("0") ? null : result;
    }

    /**
     * 找到String集合里最小的数
     *
     * @param collection
     * @return
     */
    public String getMinFromCollection(Collection<String> collection) {
        String result = "1000";
        for (String rssi : collection) {
            if (Integer.valueOf(rssi) <= Integer.valueOf(result)) {
                result = rssi;
            }
        }
        return result;
    }

    /**
     * 把HandledList中的Map排序
     */
    public void sortHandledListMap() {
        for (BeaconModel model : StaticVariables.handledBeaconList) {
            model.setNodeMap(sortMapByValue(model.getNodeMap()));
        }
        log.debug("HandledBeaconList处理后的数据："
                + StaticVariables.handledBeaconList.toString());
    }

    /**
     * 根据HandledBeaconList生成字符串，传到前端
     *
     * @return
     */
    public void sendStringFromHandledList() {
        try {
            List<Node> originalList = SpringContextHolder.getBean(
                    NodeService.class).findAllNode();
            for (Node node : originalList) {
                // 将相对坐标转换成实际距离
                NodeModel nodeModel = new NodeModel(node.getMac(),
                        Double.valueOf(node.getNodeLeft())
                                * StaticVariables.real_width,
                        Double.valueOf(node.getNodeTop())
                                * StaticVariables.real_height);
                nodeList.add(nodeModel);
            }
            // 对nodeList进行排序
            Collections.sort(nodeList, new MyComparator());
            // 打印nodeList
            log.debug("nodeList排序后: " + nodeList);
            for (BeaconModel model : StaticVariables.handledBeaconList) {
                if (model.getNeed_send() == Constants.needSend.NEED) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("location,");
                    DecimalFormat df = new DecimalFormat("0.0000");
                    double left = 0, top = 0;
                    // 得到uuid对应的标签类型
                    Integer labelType = SpringContextHolder.getBean(
                            LabelService.class).findLabelTypeByUuid(
                            model.getUuid());
                    String description = SpringContextHolder.getBean(
                            LabelService.class).findDescriptionByUuid(
                            model.getUuid());
                    sb.append(model.getUuid() + "," + model.getUuidName() + ","
                            + labelType + "," + description + ",");
                    StringBuilder sbmsg = new StringBuilder();
                    sbmsg.append(model.getUuid());
                    // 如果该uuid只在>2个节点中出现
                    if (model.getNodeMap().size() > 2) {
                        String[] keys = new String[3];
                        Double[] values = new Double[3];
                        int i = 0;
                        for (Map.Entry<String, String> entry : model
                                .getNodeMap().entrySet()) {
                            keys[i] = entry.getKey();
                            values[i] = Algorithm.calDistanceWithRssi(Integer
                                    .valueOf(entry.getValue()));
                            sbmsg.append(" " + keys[i] + " "
                                    + df.format(values[i]));
                            i++;
                        }
                        Double[] location = getLocation(keys, values);
                        left = location[0];
                        top = location[1];
                        sb.append(df.format(left) + "," + df.format(top));
                        model.setLastLeft(left);
                        model.setLastTop(top);
                        model.setLastMessage(sb.toString());
                        ShowBeaconService showBeaconService = SpringContextHolder
                                .getBean(ShowBeaconService.class);
                        showBeaconService.saveShowBeacon(model.getUuid(),
                                model.getUuidName(), df.format(left),
                                df.format(top));
                        // 把数据发送到前端
                        log.debug("需要发送的数据：" + sb.toString());
                        Websocket.sendMessageToAll(sb.toString());
                        double leftmi = Double.valueOf(left)
                                * StaticVariables.real_width;
                        double topmi = Double.valueOf(top)
                                * StaticVariables.real_height;
                        sbmsg.append(" " + df.format(leftmi) + " "
                                + df.format(topmi));
                        log.debug("需要发送到控制台的数据：" + sbmsg.toString());
                        Websocket.sendMessageToAll(sbmsg.toString());
                        // 保存数据到TrilLocation
                        TrilLocationService trilLocationService = SpringContextHolder
                                .getBean(TrilLocationService.class);
                        trilLocationService.saveTrilLocation(sbmsg.toString());
                    } else {
                        // do nothing.
                    }
                } else {
                    if (model.getLastMessage() != null) {
                        Websocket.sendMessageToAll(model.getLastMessage());
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 三边定位算法得到坐标
     *
     * @param macs
     * @param rssis
     * @return 已经转换成坐标比例的left(index=0)值和top(index=1)值
     */
    public Double[] getLocation(String[] macs, Double[] values) {
        // 定义一个Double数组，index 0 存x值，index 1 存y值
        Double[] location = new Double[2];
        try {
            System.out.println(values[0] + "," + values[1] + "," + values[2]);
            log.debug(values[0] + "," + values[1] + "," + values[2]);
            NodeModel[] nodeModels = new NodeModel[3];
            nodeModels[0] = findNodelModelByMac(macs[0]);
            nodeModels[1] = findNodelModelByMac(macs[1]);
            nodeModels[2] = findNodelModelByMac(macs[2]);
            // 产生canvas画图的数据
            DecimalFormat df = new DecimalFormat("0.0000");
            Websocket.sendMessageToAll("clear_canvas");
            for (int i = 0; i < 3; i++) {
                Websocket.sendMessageToAll("draw_circle,"
                        + df.format(nodeModels[i].getNodeLeft()
                        / StaticVariables.real_width)
                        + ","
                        + df.format(nodeModels[i].getNodeTop()
                        / StaticVariables.real_height) + ","
                        + df.format(values[i] / StaticVariables.real_width));
            }
            double[][] a = new double[2][2];
            double[][] b = new double[2][1];
			/* 数组a初始化 */
            for (int i = 0; i < 2; i++) {
                a[i][0] = 2 * (nodeModels[i].getNodeLeft() - nodeModels[2]
                        .getNodeLeft());
                a[i][1] = 2 * (nodeModels[i].getNodeTop() - nodeModels[2]
                        .getNodeTop());
            }
			/* 数组b初始化 */
            for (int i = 0; i < 2; i++) {
                b[i][0] = Math.pow(nodeModels[i].getNodeLeft(), 2)
                        - Math.pow(nodeModels[2].getNodeLeft(), 2)
                        + Math.pow(nodeModels[i].getNodeTop(), 2)
                        - Math.pow(nodeModels[2].getNodeTop(), 2)
                        + Math.pow(values[2], 2) - Math.pow(values[i], 2);
            }
			/* 将数组封装成矩阵 */
            Matrix b1 = new Matrix(b);
            Matrix a1 = new Matrix(a);
			/* 求矩阵的转置 */
            Matrix a2 = a1.transpose();
			/* 求矩阵a1与矩阵a1转置矩阵a2的乘积 */
            Matrix tmpMatrix1 = a2.times(a1);
            Matrix reTmpMatrix1 = tmpMatrix1.inverse();
            Matrix tmpMatrix2 = reTmpMatrix1.times(a2);
			/* 中间结果乘以最后的b1矩阵 */
            Matrix resultMatrix = tmpMatrix2.times(b1);
            double[][] resultArray = resultMatrix.getArray();
            location[0] = resultArray[0][0] / StaticVariables.real_width;
            location[1] = resultArray[1][0] / StaticVariables.real_height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public Double[] getLocation2(String[] macs, Double[] values) {
        // 定义一个Double数组，index 0 存x值，index 1 存y值
        Double[] location = new Double[2];
        try {
            double[][] dots = { { 1, 1 }, { 10, 1 }, { 5, 10 } };
            double[][] a = new double[2][2];
            double[][] b = new double[2][1];
			/* 数组a初始化 */
            for (int i = 0; i < 2; i++) {
                a[i][0] = 2 * (dots[i][0] - dots[2][0]);
                a[i][1] = 2 * (dots[i][1] - dots[2][1]);
            }
			/* 数组b初始化 */
            for (int i = 0; i < 2; i++) {
                b[i][0] = Math.pow(dots[i][0], 2) - Math.pow(dots[2][0], 2)
                        + Math.pow(dots[i][1], 2) - Math.pow(dots[2][1], 2)
                        + Math.pow(values[2], 2) - Math.pow(values[i], 2);
            }
			/* 将数组封装成矩阵 */
            Matrix b1 = new Matrix(b);
            Matrix a1 = new Matrix(a);
			/* 求矩阵的转置 */
            Matrix a2 = a1.transpose();
			/* 求矩阵a1与矩阵a1转置矩阵a2的乘积 */
            Matrix tmpMatrix1 = a2.times(a1);
            Matrix reTmpMatrix1 = tmpMatrix1.inverse();
            Matrix tmpMatrix2 = reTmpMatrix1.times(a2);
			/* 中间结果乘以最后的b1矩阵 */
            Matrix resultMatrix = tmpMatrix2.times(b1);
            double[][] resultArray = resultMatrix.getArray();
            location[0] = resultArray[0][0];
            location[1] = resultArray[1][0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Test
    public void test() {
        Double[] d = getLocation2(null, new Double[] { 5.0, 6.0, 6.0 });
        System.out.println(d[0] + "," + d[1]);
    }

    /**
     * 根据mac在nodeList中找到对应的NodeModel
     *
     * @param mac
     * @return
     */
    public NodeModel findNodelModelByMac(String mac) {
        for (NodeModel nodeModel : nodeList) {
            if (nodeModel.getMac().equals(mac)) {
                return nodeModel;
            }
        }
        return null;
    }

    // /**
    // * 根据map生成键值对的字符串，(key1,value1,key2,value2...)
    // *
    // * @param map
    // * @return
    // */
    // public String getStringFromMap(Map<String, String> map) {
    // StringBuilder sb = new StringBuilder();
    // Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
    // while (entries.hasNext()) {
    // Map.Entry<String, String> entry = entries.next();
    // sb.append(entry.getKey() + "," + entry.getValue() + ",");
    // }
    // sb.deleteCharAt(sb.length() - 1);
    // return sb.toString();
    // }

    /**
     * 清空originBeaconList和handledBeaconList
     */
    public void clearList() {
        StaticVariables.originalBeaconList.clear();
        // 清nodeList
        nodeList.clear();
        // 清list中的数据 遍历、清除超时10s的数据
        Iterator it = StaticVariables.handledBeaconList.iterator();
        while (it.hasNext()) {
            BeaconModel model = (BeaconModel) it.next();
            long time = new Date().getTime();
            int seconds = (int) ((time - model.getLastUpdateTime()) / 1000);
            if (seconds > 10) {
                it.remove();
                // Websocket.sendMessageToAll("delete," + model.getUuid());
                Websocket.sendMessageToAll("alarm,0");
            } else {
                model.getNodeMap().clear();
                model.setNeed_send(Constants.needSend.NO_NEED);
            }
        }
    }

    /**
     * 按map值的大小排序,从小到大
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByValue(Map<String, String> map) {
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, String> sortedMap = new LinkedHashMap<String, String>();
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(
                    map.entrySet());
            Collections.sort(entryList, new MapValueComparator());
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();
            Map.Entry<String, String> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
            return sortedMap;
        } else {
            return map;
        }
    }

    /**
     * 生成随机的弧度值
     *
     * @return
     */
    private double generateRandomRadiux() {
        Random random = new Random();
        double result = random.nextInt((int) ((2 * Math.PI) * 100)) / 100.0;
        return result;
    }

    /**
     * 内部类，定义节点模型，便于分析节点的位置关系
     *
     * @author zz
     * @version 1.0 2017年4月24日
     */
    private class NodeModel {
        private String mac;
        private double nodeLeft;
        private double nodeTop;

        public NodeModel() {

        }

        public NodeModel(String mac, double nodeLeft, double nodeTop) {
            this.mac = mac;
            this.nodeLeft = nodeLeft;
            this.nodeTop = nodeTop;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public double getNodeLeft() {
            return nodeLeft;
        }

        public void setNodeLeft(double nodeLeft) {
            this.nodeLeft = nodeLeft;
        }

        public double getNodeTop() {
            return nodeTop;
        }

        public void setNodeTop(double nodeTop) {
            this.nodeTop = nodeTop;
        }

        public NodeModel findNextNode() {
            int index = nodeList.indexOf(this);
            if (index != nodeList.size() - 1) {
                return nodeList.get(index + 1);
            } else {
                return null;
            }
        }

        public NodeModel findPrevNode() {
            int index = nodeList.indexOf(this);
            if (index != 0) {
                return nodeList.get(index - 1);
            } else {
                return null;
            }
        }

        public double getTan() {
            double result = 0;
            NodeModel nextNode = this.findNextNode();
            if (nextNode != null) {
                result = (nextNode.getNodeTop() - this.getNodeTop())
                        / (nextNode.getNodeLeft() - this.getNodeLeft());
            } else {
                NodeModel prevNode = this.findPrevNode();
                if (prevNode != null) {
                    result = prevNode.getTan();
                }
            }
            return result;
        }

        public double getSin() {
            double result = 0;
            result = this.getTan() / Math.sqrt(1 + Math.pow(this.getTan(), 2));
            return result;
        }

        public double getCos() {
            double result = 0;
            result = 1 / Math.sqrt(1 + Math.pow(this.getTan(), 2));
            return result;
        }

        @Override
        public String toString() {
            return "NodeModel [mac=" + mac + ", nodeLeft=" + nodeLeft
                    + ", nodeTop=" + nodeTop + "]";
        }

    }

    /**
     * List按x轴的值从小到大排序
     *
     * @author zz
     * @version 1.0 2017年4月28日
     */
    private class MyComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            NodeModel model1 = (NodeModel) o1;
            NodeModel model2 = (NodeModel) o2;
            return (int) (model1.getNodeLeft() - model2.getNodeLeft());
        }

    }

    // @Test
    // public void test(){
    // System.out.println(Integer.parseInt("1f", 16));
    // }

}
