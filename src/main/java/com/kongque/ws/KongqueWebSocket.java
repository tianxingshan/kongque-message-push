package com.kongque.ws;


import com.alibaba.fastjson.JSONArray;
import com.kongque.dao.IMessageDao;
import com.kongque.dao.IMessagePushDao;
import com.kongque.dto.WebSockDataDto;
import com.kongque.entity.Message;
import com.kongque.entity.MessageDecoder;
import com.kongque.entity.MessageEncoder;
import com.kongque.entity.MessagePush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket服务
 */
@ServerEndpoint(value = "/ws/{accountId}",encoders = MessageEncoder.class,decoders = MessageDecoder.class)
@Component
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

    private  IMessagePushDao pushDao;

    private  IMessageDao messageDao;


    private static ConfigurableApplicationContext applicationContext;
    public static void  setApplicationContext(ConfigurableApplicationContext applicationContext){
        KongqueWebSocket.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "accountId") String accountId) {
        this.session = session;
        webSocketMap.put(accountId, this);
        log.info("用户accountId=" + accountId + "开始接入socket,当前通信人数为:" + webSocketMap.size());

        List<Message> pushNewMessageList = getPushNewMessageList(accountId);
        //推送消息
        if(pushNewMessageList!=null && pushNewMessageList.size()>0){
            WebSockDataDto webSockDataDto = new WebSockDataDto();
            webSockDataDto.setMessageList(pushNewMessageList);
            onMessage(webSockDataDto);
            saveMessagePushList(pushNewMessageList, accountId);
            log.info("推送成功,开始向用户accountId="+accountId+"发送消息:"+JSONArray.toJSONString(pushNewMessageList));
        }else{

            log.info("推送成功,暂无新消息推送给用户accountId="+accountId);
        }
    }

    /*
    推送给用户后保存数据库
     */
    public void saveMessagePushList(List<Message> messageList,String accountId){
        List<MessagePush> pushList = new ArrayList<>();
        messageList.forEach(message->{
            MessagePush push = new MessagePush();
            push.setAccountId(accountId);
            push.setMessageId(message.getId());
            push.setPushTime(new Date());
            pushList.add(push);
        });
        pushDao.insertBatch(pushList);
        log.info("保存数据成功,推送给用户accountId="+accountId+"\t数据:"+JSONArray.toJSONString(pushList));
    }

    /*
    获取要推送给用户新消息集合
     */
    private List<Message> getPushNewMessageList(String accountId){
        messageDao = applicationContext.getBean("IMessageDao",IMessageDao.class);
        pushDao = applicationContext.getBean("IMessagePushDao",IMessagePushDao.class);
        List<Message> messageList = messageDao.getList();
        MessagePush messagePush = new MessagePush();
        messagePush.setAccountId(accountId);
        List<MessagePush> listByParams = pushDao.getListByParams(messagePush);
        List<Message> completeList = new ArrayList<>();
        if (messageList != null && messageList.size() > 0) {//过滤已经推送过的消息
            for (Message message : messageList) {
                if (listByParams != null && listByParams.size() > 0) {
                    for (MessagePush push : listByParams) {
                        if (message.getId().equals(push.getMessageId())) {
                            completeList.add(message);
                        }
                    }
                }
            }
        }
        messageList.removeAll(completeList);
        return messageList;
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
     * 监听方法,前后端的onMessage有变动便进行通信
     *
     * @param dataDto 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(WebSockDataDto dataDto) {
        List<String> accountIds = new ArrayList<String>();
        for (Map.Entry<String,KongqueWebSocket> kongqueWebSocket:webSocketMap.entrySet()) {
            kongqueWebSocket.getValue().sendObj(dataDto);
        }
        log.info("推送成功");
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
    private void sendObj(WebSockDataDto dataDto) {
        try {
            this.session.getBasicRemote().sendObject(dataDto);
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
