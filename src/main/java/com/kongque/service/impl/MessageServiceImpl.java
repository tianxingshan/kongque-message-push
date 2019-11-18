package com.kongque.service.impl;

import com.kongque.controller.ws.KongqueWebSocket;
import com.kongque.entity.Message;
import com.kongque.service.IMessageService;
import com.kongque.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class MessageServiceImpl  implements IMessageService {

    @Resource
    private KongqueWebSocket webSocket;

    @Override
    public Result<String> messagePush(Message message) {
        webSocket.onMessage(message, null);
        return null;
    }
}
