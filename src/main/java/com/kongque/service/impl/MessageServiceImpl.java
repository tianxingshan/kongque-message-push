package com.kongque.service.impl;

import com.kongque.dao.IMessageDao;
import com.kongque.dto.MessageDto;
import com.kongque.dto.WebSockDataDto;
import com.kongque.entity.Message;
import com.kongque.service.IMessageService;
import com.kongque.util.Result;
import com.kongque.ws.KongqueWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class MessageServiceImpl  implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private KongqueWebSocket kongqueWebSocket;

    @Resource
    private IMessageDao messageDao;

    @Override
    public Result<String> messagePush(MessageDto dto) {
        Message message = new Message(dto);
        message.setCreateTime(new Date());
        messageDao.insert(message);
        logger.info("后台下发消息成功,消息id="+message.getId());
        WebSockDataDto webSockDataDto = new WebSockDataDto();
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        webSockDataDto.setMessageList(messageList);
        kongqueWebSocket.onMessage(webSockDataDto);
        logger.info("开始向所有在线用户推送新消息");
        return new Result<String>(message.getId());
    }
}
