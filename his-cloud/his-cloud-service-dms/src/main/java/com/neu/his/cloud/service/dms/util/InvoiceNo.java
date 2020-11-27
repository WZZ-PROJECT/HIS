package com.neu.his.cloud.service.dms.util;

import java.util.UUID;

/**
 * 自定义发票号生成器
 */
public class InvoiceNo {
    //生成16位唯一性的订单号
    public static long getInvoiceNo(){
        //随机生成一位整数
        int random = (int) (Math.random()*9+1);
        String valueOf = String.valueOf(random);
        //生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        //可能为负数
        if(hashCode<0){
            hashCode = -hashCode;
        }
        String value = valueOf + String.format("%015d", hashCode);
        return Long.parseLong(value);
    }
}
