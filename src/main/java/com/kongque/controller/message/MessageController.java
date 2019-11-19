package com.kongque.controller.message;

import com.kongque.dao.IMessageDao;
import com.kongque.dto.MessageDto;
import com.kongque.service.IMessageService;
import com.kongque.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Resource
    private IMessageDao dao;


    /**
     * 后台管理下发消息
     */
    @PostMapping(value = "/message/push")
    private Result<String> messageToAllUser(@RequestBody MessageDto dto){
        logger.info("后台下发消息开始");
        return messageService.messagePush(dto);
    }

}
