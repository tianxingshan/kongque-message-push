package com.kongque.dto;

import com.kongque.entity.Message;

import java.util.List;

public class WebSockDataDto {

    private List<Message> messageList;

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

}
