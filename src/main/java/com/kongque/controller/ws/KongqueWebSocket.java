package com.kongque.controller.ws;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kongque.entity.Message;
import com.kongque.entity.MessageDecoder;
import com.kongque.entity.MessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws/{accountId}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
@Component
@Slf4j
public class KongqueWebSocket {

    private Logger log = LoggerFactory.getLogger(KongqueWebSocket.class);

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static ConcurrentHashMap<String, KongqueWebSocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "accountId") String accountId) {
        this.session = session;
        webSocketMap.put(accountId, this);
        log.info("新加入用户,accountId= " + accountId + "\t" + "目前总连接数 : " + webSocketMap.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "accountId") String accountId) {
        webSocketMap.remove(accountId);
        log.info("websocket关闭连接");
        log.info("用户下线,accountId:"+accountId + "目前在线用户数 : " + webSocketMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Message message, @PathParam(value = "accountId") String accountId) {
        List<String> accountIds = new ArrayList<String>();
        for (Map.Entry<String,KongqueWebSocket> entry:webSocketMap.entrySet()) {
            accountIds.add(entry.getKey());
            entry.getValue().sendObj(message);
        }
        log.info("onmessage监听成功,向如下用户推送了消息 : \t 用户:" + JSONArray.toJSONString(accountIds) + "\t消息:"+ JSONObject.toJSONString(message));
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Throwable error) {
        log.info(error.toString());
    }

    /**
     * 发送对象到客户端
     */
    private void sendObj(Message message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (EncodeException | IOException e) {
            log.error("消息推送失败",e);
        }
    }

    /**
     * 发送文本到客户端
     */
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.info("发送消息错误");
        }
    }
}
