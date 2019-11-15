package com.kongque.controller.message;

import com.kongque.config.HttpClinetConfig;
import com.kongque.service.IMessageService;
import com.kongque.util.HttpClientUtil;
import com.kongque.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后台消息推送给用户
 */
@RestController
public class MessageController {

    @Autowired
    private IMessageService messageService;


    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/message/pushToAll")
    private Result<String> messageToAllUser(){
        logger.info("后台向用户推送消息");
        return messageService.messagePushToAllUser();
    }

    @GetMapping(value = "/test")
    private Result<String> test(){
        return new Result<String>(null);
    }


}
