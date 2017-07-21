package com.ibeacon.web.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


/**
 * websocket类，与前端进行实时通信
 * @author zz
 * @version 1.0 2017年3月23日
 */
@ServerEndpoint("/websocket")
public class Websocket {

    private static Logger log = Logger.getLogger("Websocket");

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // concurrent包的线程安全Set，用来存放每个客户端对应的DemoWebServlet对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<Websocket> webSocketSet = new CopyOnWriteArraySet<Websocket>();
    //目前使用一个唯一的websocket
    public static Websocket instance = null;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        instance = this;	//把instance单例赋值给此次连接
        webSocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线数加1
        log.debug("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        instance = null;
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
        log.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     * @param session
     *            可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("来自客户端的消息:" + message);
        //群发消息
//	        for(Websocket item: webSocketSet){
//	            try {
//	                item.sendMessage(message);
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	                continue;
//	            }
//	        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.debug("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发消息
     * @param message
     */
    public static void sendMessageToAll(String message){
        for(Websocket item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        Websocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        Websocket.onlineCount--;
    }
}
