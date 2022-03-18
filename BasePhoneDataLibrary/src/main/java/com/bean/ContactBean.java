package com.bean;

import java.io.Serializable;
import java.util.Objects;

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

    public String firstName; //第一名称
    public String middleName; //中间名称
    public String lastName; //最后名称
    public String companyName; //公司名称
    public String jobTitle; //工作种类
    public String phoneLabel=""; //电话标签

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
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", phoneLabel='" + phoneLabel + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactBean that = (ContactBean) o;
        return Objects.equals(name, that.name) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }
}