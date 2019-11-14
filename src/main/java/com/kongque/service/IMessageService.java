package com.kongque.service;

import com.kongque.util.Result;

/**
 * 消息服务
 */
public interface IMessageService {

    /**
     * 向全部用户推送消息
     * @return
     */
    Result<String> messagePushToAllUser();
}
