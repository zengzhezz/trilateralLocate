package com.ibeacon.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.msginfo.MessageInfo;
import com.ibeacon.model.beacon.Node;
import com.ibeacon.service.node.NodeService;

import com.ibeacon.utils.HttpUtils;

@Controller
@RequestMapping("/node/")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    //@Autowired
//	private PersonService personService;

    /**
     * 增加节点
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "addnode.do" })
    public MessageInfo addNode(HttpServletRequest request){
        String mac = request.getParameter("mac"),
                name = request.getParameter("name"),
                nodeTop = request.getParameter("nodeTop"),
                nodeLeft = request.getParameter("nodeLeft");
        if(!nodeService.checkNodeExist(mac)){
            nodeService.saveNode(mac, name, nodeTop, nodeLeft);
            return new MessageInfo(0,"success");
        }else{
            return new MessageInfo(1, "failed");
        }
    }

    /**
     * 删除节点
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "deletenode.do" })
    public MessageInfo deleteNode(HttpServletRequest request){
        String mac = request.getParameter("mac");
        if(nodeService.deleteNodeByMac(mac)){
            return new MessageInfo(0,"success");
        }else{
            return new MessageInfo(1,"删除节点失败...此mac在数据库中不存在");
        }
    }

    /**
     * 保存节点
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "savenode.do" })
    public MessageInfo saveNode(HttpServletRequest request){
        try {
            String jsonData = HttpUtils.getStringFromHttpRequest(request);
            JSONArray jsonArray = new JSONArray(jsonData);
            for (Object object : jsonArray) {
                JSONObject jsonObj = (JSONObject) object;
                String mac = jsonObj.getString("mac");
                String name = jsonObj.getString("name");
                String nodeTop = jsonObj.getString("nodeTop");
                String nodeLeft = jsonObj.getString("nodeLeft");
                if(!nodeService.checkNodeExist(mac)){
                    nodeService.saveNode(mac, name, nodeTop, nodeLeft);
                }else{
                    nodeService.updateNodeLocation(mac, nodeTop, nodeLeft);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new MessageInfo(1,"服务器错误...");
        }
        return new MessageInfo(0,"success");
    }

    /**
     * 得到所有的节点
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = { "getallnode.do" })
    public List<Node> getAllNode(){
        List<Node> list = nodeService.findAllNode();
        return list;
    }

    /**
     * 得到节点下所有的人员
     * @return

     @ResponseBody
     @RequestMapping(method = RequestMethod.POST, value = { "getallnodeperson.do" })
     public List<Map<String, String>> getAllNodePerson(){
     List<Node> list = nodeService.findAllNode();
     List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
     for (Node node : list) {
     Map<String, String> map = new HashMap<String, String>();
     map.put("mac", node.getMac());
     map.put("number", getNumberfromUuidString(node.getUuidString()).toString());
     map.put("uuidNameString", getUuidNameString(node.getUuidString()));
     resultList.add(map);
     }
     return resultList;
     }
     */
    /**
     * 通过uuidString得到其对应人名的String
     * @param uuidString
     * @return

    public String getUuidNameString(String uuidString){
    if(uuidString!=null&&!uuidString.equals("")){
    String[] uuidGroup = uuidString.split(",");
    StringBuilder sb = new StringBuilder();
    for (String uuid : uuidGroup) {
    String name = personService.findPersonNameByUuid(uuid);
    sb.append(name+",");
    }
    sb.deleteCharAt(sb.length()-1);
    return sb.toString();
    }else{
    return "";
    }

    }
     */
    /**
     * 得到uuidString的长度
     * @param uuidString
     * @return
     */
    public Integer getNumberfromUuidString(String uuidString){
        if(uuidString!=null&&!uuidString.equals("")){
            String[] uuidGroup = uuidString.split(",");
            return uuidGroup.length;
        }else{
            return 0;
        }
    }

    @Test
    public void test(){
        String uuidString = "";
        String[] uuidGroup = uuidString.split(",");
        System.out.println(uuidGroup);
    }
}
