package com.neu.his.cloud.zuul.controller.bms;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.util.IdGen;
import com.neu.his.cloud.zuul.util.PaymentApi;
import com.neu.his.cloud.zuul.util.PaymentKit;
import com.neu.his.cloud.zuul.controller.bms.wxpay.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;


@RestController
@Api(tags = "getPackage", description = "微信支付")
@RequestMapping("/getPackage")
@CrossOrigin(allowCredentials = "true")
public class GetPackageController {

    @Value("${appid}")
    private String appid;
    @Value("${mch_id}")
    private String mch_id;

    @HystrixCommand(fallbackMethod = "getPackage")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/getPackage", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String, String>>  getPackage(String openId, BigDecimal rechargeMoney) throws Exception {
        /**
         微信小程序支付
         */
        //订单号  uuid
        String outTradeNo= IdGen.uuid();

        Map<String, String> map = unifiedOrder(outTradeNo,rechargeMoney,openId);
        return CommonResult.success(map,"成功");

    }
    /**
     * 功能描述: <调用统一下单的接口>
     * @return:
     * @auther: majker
     * @date: 2019/3/7
     **/
    public Map<String, String> unifiedOrder(String outTradeNo, BigDecimal money, String openid) throws Exception {
        Map<String, String> reqParams = new HashMap<>();
        //微信分配的小程序ID
        reqParams.put("appid", appid);
        //微信支付分配的商户号
        reqParams.put("mch_id", mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "2343abc345343ABC3453");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "微信小程序统一下单支付");

        //商户订单号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
//        reqParams.put("total_fee", String.valueOf(money));
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        reqParams.put("notify_url","http://qq.com");
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, "UtQldsIcA8rldkOUdlyHOdGZzHhJX7fz");
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        //预付单信息
        String prepay_id = result.get("prepay_id");

        /*
            小程序调起支付数据签名
         */
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appId", appid);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "2343abc345343ABC3453");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, "UtQldsIcA8rldkOUdlyHOdGZzHhJX7fz");
        packageParams.put("paySign", packageSign);
        return packageParams;
//        return prepay_id;
    }
}
