package com.kongque.service;

import com.kongque.dto.MessageDto;
import com.kongque.entity.Message;
import com.kongque.util.Result;

/**
 * 消息服务
 */
public interface IMessageService {

    /**
     * 后台下发消息
     * @return
     */
    Result<String> messagePush(MessageDto dto);
}
