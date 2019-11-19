package com.kongque.entity;

import com.alibaba.fastjson.JSONObject;
import com.kongque.dto.WebSockDataDto;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<WebSockDataDto>{


    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(WebSockDataDto webSockDataDto) throws EncodeException {
        return JSONObject.toJSONString(webSockDataDto);
    }
}
