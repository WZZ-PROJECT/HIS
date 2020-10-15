package com.neu.his.cloud.zuul.util;

import lombok.Data;

@Data
public class Package {
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String body;
    private String outTradeNo;
    private String totalFee;
    private String spbillCreateIp;
    private String notifyUrl;
    private String tradeType;
}
