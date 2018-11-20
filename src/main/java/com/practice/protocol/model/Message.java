package com.practice.protocol.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Message {
    public static final String MSG_TYPE_SYNC_HOSTER_TIME = "SYNC_HOSTER_TIME";
    public static final String MSG_TYPE_SYNC = "SYNC";
    public static final String MSG_TYPE_REQUEST_DATA = "requestData";
    public static final String MSG_TYPE_ERROR = "error";
    String type;
    @JSONField(jsonDirect = true)
    String data;

    public Message() {
    }

    private Message(Builder builder) {
        setType(builder.type);
        setData(builder.data);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public static final class Builder {
        private String type;
        private String data;

        public Builder() {
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

}
