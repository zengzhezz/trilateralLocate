package com.ibeacon.service.node;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ibeacon.model.beacon.Node;
import com.ibeacon.service.base.AbstractService;

/**
 * 节点Service
 * @author zz
 * @version 1.0 2017年3月22日
 */
@Service
public class NodeService extends AbstractService {

    /**
     * 通过mac找到对应的节点
     * @param mac
     * @return
     */
    public Node findNodeByMac(String mac){
        return this.findUnique("from Node where mac = ?", mac);
    }

    /**
     * 找到所有节点
     * @return
     */
    public List<Node> findAllNode(){
        return this.find("from Node");
    }

    /**
     * 根据mac查找对应的名称
     * @param mac
     * @return
     */
    public String findNodeNameByMac(String mac){
        Node node = findNodeByMac(mac);
        if(node!=null){
            return node.getName();
        }else{
            return "";
        }
    }

    /**
     * 保存节点
     * @param mac
     * @param name
     * @param nodeTop
     * @param nodeLeft
     */
    public void saveNode(String mac, String name, String nodeTop, String nodeLeft){
        Node node = new Node();
        node.setMac(mac);
        node.setName(name);
        node.setNodeTop(nodeTop);
        node.setNodeLeft(nodeLeft);
        node.setUuidString("");
        this.save(node);
    }

    /**
     * 根据mac删除node，删除成功返回true，否则返回false
     * @param mac
     * @return
     */
    public boolean deleteNodeByMac(String mac){
        Node node = findNodeByMac(mac);
        if(node!=null){
            this.delete(node);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 更新节点的位置
     * @param mac
     * @param nodeTop
     * @param nodeLeft
     */
    public boolean updateNodeLocation(String mac, String nodeTop, String nodeLeft){
        Node node = findNodeByMac(mac);
        if(node!=null){
            node.setNodeTop(nodeTop);
            node.setNodeLeft(nodeLeft);
            this.update(node);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 检测此mac的node是否已经存在
     * @param mac
     * @return
     */
    public boolean checkNodeExist(String mac){
        Node node = findNodeByMac(mac);
        return node != null;
    }

    /**
     * 更新节点的uuidString
     * @param mac
     * @param uuidString
     */
    public void updateNodeUuidString(String mac, String uuidString){
        Node node = findNodeByMac(mac);
        node.setUuidString(uuidString);
        this.update(node);
    }

}
