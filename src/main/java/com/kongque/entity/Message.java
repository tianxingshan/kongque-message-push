package com.kongque.entity;

import com.kongque.dto.MessageDto;

import java.util.Date;

public class Message {

    /**
     * 主键
     */
    private String id;

    /**
     * 主题
     */
    private String  theme;
    /**
     * 内容
     */
    private String  content;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Message() {
    }

    public Message(MessageDto dto) {
        this.theme = dto.getTheme();
        this.content=dto.getContent();
    }
}
