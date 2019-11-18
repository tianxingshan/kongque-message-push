package com.kongque.controller.message;

import com.kongque.entity.Message;
import com.kongque.service.IMessageService;
import com.kongque.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台消息推送给用户
 */
@RestController
public class MessageController {

    @Autowired
    private IMessageService messageService;


    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 后台管理向客户推送消息
     * @param message
     * @return
     */
    @PostMapping(value = "/message/push")
    private Result<String> messageToAllUser(@RequestBody Message message){
        logger.info("向用户推送消息开始");
        return messageService.messagePush(message);
    }

    @GetMapping(value = "/test")
    private Result<String> test(){
        return new Result<String>(null);
    }


}
