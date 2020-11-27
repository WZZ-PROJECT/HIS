package com.neu.his.cloud.zuul.controller.bms;

import cn.hutool.json.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.component.WeChatCode;
import com.neu.his.cloud.zuul.controller.bms.wxpay.MyConfig;
import com.neu.his.cloud.zuul.controller.bms.wxpay.WXPay;
import com.neu.his.cloud.zuul.util.GZHPay;
import com.neu.his.cloud.zuul.util.IdGen;
import com.neu.his.cloud.zuul.util.PaymentApi;
import com.neu.his.cloud.zuul.util.PaymentKit;
import com.neu.his.cloud.zuul.controller.bms.wxpay.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
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

    @Value("${AppId}")
    private String AppId;

    @Value("${Appsecret}")
    private String Appsecret;

    @Value("${AppGrant_type}")
    private String AppGrant_type;

    @Value("${AppMch_id}")
    private String AppMch_id;

    @Value("${AppPassword}")
    private String AppPassword;

    @HystrixCommand(fallbackMethod = "getPackage")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/getPackage", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String, String>>  getPackage(String openId, BigDecimal rechargeMoney) throws Exception {
        /**
         微信小程序支付
         */
        //订单号  uuid
        /*String outTradeNo= IdGen.uuid();*/
        String outTradeNo=WXPayUtil.generateNonceStr();

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
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + IdGen.randomLong()+"1");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "微信小程序统一下单支付");

        //商户订单号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        reqParams.put("notify_url","http://his.cgjiankang.com");
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, this.AppPassword);
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
        packageParams.put("nonceStr", System.currentTimeMillis() + IdGen.randomLong()+"1");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, this.AppPassword);
        packageParams.put("paySign", packageSign);
        packageParams.put("out_trade_no", outTradeNo);
        return packageParams;
//        return prepay_id;
    }


    //公众号的支付
    //获取openid
    @HystrixCommand(fallbackMethod = "getOpenId")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/getOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getOpenId(String code){
//        String url1="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+this.AppId
//                +"&secret="+this.Appsecret
//                +"&code="+code
//                +"&grant_type="+AppGrant_type;
//        JSONObject jo = new JSONObject(WeChatCode.sendGet(url1));
        String open = GZHPay.getOpenid(code,this.AppId,this.Appsecret,AppGrant_type);
        System.out.println(open);
        return CommonResult.success(open,"成功");

    }
    //获取SignType
    @HystrixCommand(fallbackMethod = "getSignType")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/getSignType", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getSignType(String timeStamp,String nonceStr,String prepay_id) throws Exception {
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appId", this.AppId);
        packageParams.put("timeStamp", timeStamp);
        packageParams.put("nonceStr", nonceStr);
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, this.AppPassword);

        return CommonResult.success(packageSign,"成功");

    }
    //获取Signature
    @HystrixCommand(fallbackMethod = "getSignature")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/getSignature", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getSignature(String noncestr,String timestamp,String url) throws Exception {
        String signature = GZHPay.getWxSignature(noncestr,timestamp,url,this.AppId,this.Appsecret);
        return CommonResult.success(signature,"成功");

    }
    /**
     * 功能描述: <调用统一下单的接口>
     * @return:
     * @auther: majker
     * @date: 2019/3/7
     **/
    @HystrixCommand(fallbackMethod = "unifiedOrder")
    @ApiOperation(value = "根据挂号Id查询所有未交费项目")
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String, String>> unifiedOrder(BigDecimal money, String openid) throws Exception {
        String outTradeNo=WXPayUtil.generateNonceStr();
        Map<String, String> reqParams = new HashMap<>();
        //微信分配的公众账号ID
        reqParams.put("appid", this.AppId);
        //微信支付分配的商户号
        reqParams.put("mch_id", this.AppMch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + IdGen.randomLong()+"2");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "微信公众号统一下单支付");

        //商户订单号
        reqParams.put("out_trade_no",outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");

        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        reqParams.put("notify_url","http://his.cgjiankang.com");
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, this.AppPassword);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        result.put("out_trade_no", outTradeNo);

        return CommonResult.success(result,"成功");
    }


    public static Map<String, String> getResultsMap(String outTradeNo) throws Exception {

        if(!StringUtils.isEmpty(outTradeNo)){
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>(16);
            data.put("out_trade_no", outTradeNo);
            Map<String, String> stringStringMap = wxpay.orderQuery(data);
            if("SUCCESS".equals(stringStringMap.get("result_code")) && "SUCCESS".equals(stringStringMap.get("return_code")) && "SUCCESS".equals(stringStringMap.get("trade_state"))){
                String results = WXPayUtil.mapToXml(stringStringMap);
                stringStringMap.put("results",results);
                return stringStringMap;
            }
        }
        return null;
    }

}
