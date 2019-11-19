package com.kongque.dao;

import com.kongque.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMessageDao {

    int insert(Message message);

    List<Message> getList();
}
