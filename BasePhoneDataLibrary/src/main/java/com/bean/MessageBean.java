package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:30.
 * Describe：短信信息对象
 */
public class MessageBean implements Serializable {
    public String name;     //收发人
    public String phone;    //手机号码
    public int type;	    //收发标识 1：发送、2：接收
    public int read;        //是否已读 0：未读、1：已读
    public long time;       //短信时间（13位毫秒）
    public String content;  //短信内容
    public String  smsId;   //短信ID


    public MessageBean(String name, String phone, int type, int read, long time, String content, String smsId) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.read = read;
        this.time = time;
        this.content = content;
        this.smsId = smsId;
    }

    public MessageBean() {
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type + " 1发送  2接收" +
                ", read=" + read +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", smsId='" + smsId + '\'' +
                '}';
    }
}