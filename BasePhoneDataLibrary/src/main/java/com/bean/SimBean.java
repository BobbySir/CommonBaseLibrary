package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2022-04-28 11:25.
 * Describe：sim对象信息
 */
public class SimBean implements Serializable {
   public String phone;
   //默认拨号卡
   public boolean isDefaultVoicePhone;

   @Override
   public String toString() {
      return "SimBean{" +
              "phone='" + phone + '\'' +
              ", isDefaultVoicePhone=" + isDefaultVoicePhone +
              '}';
   }
}