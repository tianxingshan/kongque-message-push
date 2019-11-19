package com.kongque.entity;

import java.util.Date;

/**
 * 消息推送
 */
public class MessagePush {

    /**
     * 主键
     */
    private String id;

    /*
    消息id
     */
    private String messageId;

    /**
     * 推送时间
     */
    private Date pushTime;

    /**
     * 推送用户id
     */
    private String accountId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
