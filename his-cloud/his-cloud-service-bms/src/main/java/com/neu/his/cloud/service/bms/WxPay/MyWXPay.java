package com.neu.his.cloud.service.bms.WxPay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class MyWXPay {

    private static final String PAY_SUCCESS = "SUCCESS";
    private static final String PAY_USERPAYING = "USERPAYING";

    private MyWXPay(){}

    private static Log log = LogFactory.getLog(MyWXPay.class);

    /**
     * 扫码支付
     * @throws Exception
     * 调用微信支付的接口
     */
    public static Map<String,String> scanCodeToPay(String auth_code,String total_fee) throws Exception {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);
        String out_trade_no = WXPayUtil.generateNonceStr();
        Map<String, String> map = new HashMap<>(16);
        map.put("attach", "订单额外描述");
        map.put("auth_code", auth_code);
        map.put("body", "付款码支付测试");
        map.put("device_info", "1");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("out_trade_no", out_trade_no);
        map.put("spbill_create_ip", "127.0.0.1");
        map.put("total_fee", total_fee);
        /*map.put("total_fee", "1.01");*/
        //生成签名
        String sign = WXPayUtil.generateSignature(map, config.getKey());
        map.put("sign", sign);
        String mapToXml = null;
        try {
            //调用微信的扫码支付接口(需要密码)
            Map<String, String> resp = wxpay.microPay(map);
            mapToXml = WXPayUtil.mapToXml(resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信支付失败"+ e);
        }
        //判断支付是否成功
        String return_code = null;
        String result_code = null;
        String err_code_des = null;
        String err_code = null;

        Map<String, String> stringStringMap = WXPayUtil.xmlToMap(mapToXml);
        return_code=stringStringMap.get("return_code");
        result_code=stringStringMap.get("result_code");
        err_code_des=stringStringMap.get("err_code_des");
        err_code=stringStringMap.get("err_code");

        if(PAY_SUCCESS.equals(return_code) && PAY_SUCCESS.equals(result_code)){
            for(int i = 0; i < 10; i++){
                Thread.sleep(3000);
                Map<String, String> data = new HashMap<>(16);
                data.put("out_trade_no", out_trade_no);
                //调用微信的查询接口
                Map<String, String> orderQuery = wxpay.orderQuery(data);
                String orderResp = WXPayUtil.mapToXml(orderQuery);
                String trade_state = null;
                Map<String, String> stringStringMap1 = WXPayUtil.xmlToMap(orderResp);
                trade_state=stringStringMap1.get("trade_state");
                if(PAY_SUCCESS.equals(trade_state)){
                    String results = WXPayUtil.mapToXml(stringStringMap1);
                    stringStringMap1.put("results",results);
                    return stringStringMap1;
                }
            }
        } else if (PAY_USERPAYING.equals(err_code)){
            for(int i = 0; i < 10; i++){
                Thread.sleep(3000);
                Map<String, String> data = new HashMap<>(16);
                data.put("out_trade_no", out_trade_no);
                //调用微信的查询接口
                Map<String, String> orderQuery = wxpay.orderQuery(data);
                String orderResp = WXPayUtil.mapToXml(orderQuery);
                String trade_state = null;
                Map<String, String> stringStringMap1 = WXPayUtil.xmlToMap(orderResp);
                trade_state=stringStringMap1.get("trade_state");
                if(PAY_SUCCESS.equals(trade_state)){
                    String results = WXPayUtil.mapToXml(stringStringMap1);
                    stringStringMap1.put("results",results);
                    return stringStringMap1;
                }
            }
        }
        log.error("微信支付失败！");
        return null;
    }

    /**
     * 退款
     * @throws Exception
     */

    public static Map<String,String> refund(String transaction_id,String total_fee,String refund_fee) throws Exception{

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);
        String nonce_str = WXPayUtil.generateNonceStr();
        String out_refund_no=WXPayUtil.generateNonceStr();
        Map<String, String> map = new HashMap<>(16);
        map.put("nonce_str",nonce_str);
        map.put("out_refund_no",out_refund_no);
        map.put("transaction_id",transaction_id);
        map.put("refund_fee_type", "CNY");
        map.put("total_fee", total_fee);
        map.put("refund_fee", refund_fee);//部退款金额
        String sign = WXPayUtil.generateSignature(map, config.getKey());
        map.put("sign",sign);
        String mapToXml = null;
        try {
            //调用微信退款
            Map<String, String> resp = wxpay.refund(map);
            mapToXml = WXPayUtil.mapToXml(resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信退款失败"+ e);
        }

        Map<String, String> stringStringMap = WXPayUtil.xmlToMap(mapToXml);
        String return_code = stringStringMap.get("return_code");
        String result_code = stringStringMap.get("result_code");
        if(PAY_SUCCESS.equals(return_code) && PAY_SUCCESS.equals(result_code)){
            Map<String, String> data = new HashMap<>(16);
            data.put("out_refund_no", out_refund_no);
            //查询退款返回结果
            Map<String, String> stringStringMap1 = wxpay.refundQuery(data);
            String results = WXPayUtil.mapToXml(stringStringMap1);
            stringStringMap1.put("results",results);
            return stringStringMap1;
        }
        return null;
    }

}
