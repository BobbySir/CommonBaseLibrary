package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:33.
 * Describe：通讯录对象
 */
public class ContactBean implements Serializable {
    public String name;	 //姓名
    public String phone; //手机号码
    public long updateTime; //更新时间（13位毫秒）
    public String inputTime; //联系人最后修改时间
    public long lastTimeContacted; //与联系人最后联系时间（13位毫秒）
    public int timesContacted; //联系次数
    public int source; //联系人来源 1：设备 2：sim卡

    @Override
    public String toString() {
        return "ContactBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", updateTime=" + updateTime +
                ", inputTime='" + inputTime + '\'' +
                ", lastTimeContacted=" + lastTimeContacted +
                ", timesContacted=" + timesContacted +
                ", source=" + source +
                '}' + "\n";
    }
}