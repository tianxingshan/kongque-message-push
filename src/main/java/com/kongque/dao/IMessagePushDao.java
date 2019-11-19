package com.kongque.dao;

import com.kongque.entity.Message;
import com.kongque.entity.MessagePush;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMessagePushDao {

    int insert(MessagePush messagePush);

    /**
     * 根据参数查询推送消息
     * @param messagePush
     * @return
     */
    List<MessagePush> getListByParams(MessagePush messagePush);


    /**
     * 批量添加
     * @param list
     */
    int insertBatch(List<MessagePush> list);
}
