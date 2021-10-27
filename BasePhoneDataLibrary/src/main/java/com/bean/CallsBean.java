package com.bean;

import com.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-10-27 11:10.
 * Describe：
 */
public class CallsBean implements Serializable {
    public String name;  //名称
    public String phone; //手机号
    public String callTime; //通话时间
    public String callDuration; //通话时长
    public int type;  //1：接听 2：呼出 3：未接听 4：语音邮箱 5： 拒接 6 ：自动拦截
    public int callCnt; //通话次数

    @Override
    public String toString() {
        return "CallsBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", callTime='" + callTime + '\'' +
                ", callDuration='" + callDuration + '\'' +
                ", type=" + type +
                ", callCnt=" + callCnt +
                '}' +"\n";
    }
}