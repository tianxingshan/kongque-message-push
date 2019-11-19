package com.kongque.dto;

import java.util.Date;

public class MessageDto {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 主题
     */
    private String  theme;
    /**
     * 内容
     */
    private String  content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
