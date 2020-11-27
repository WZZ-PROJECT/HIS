package com.neu.his.cloud.service.dms.config;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;


public class SendSMSUtils {

    private static Logger logger = LoggerFactory.getLogger(SendSMSUtils.class);

    // 短信应用SDK AppID
    // 1400开头
    static int appid = 1400440002;
    // 短信应用SDK AppKey
    static String appkey = "5d2c9b2c9346a20d0bbce9a40824cbbd";
    // 短信模板ID，需要在短信应用中申请
    static int templateId = 756537; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
    // 签名
    // NOTE
    static String smsSign = "慈光健康";
    // 这里的签名'腾讯云'只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`

    public static void sendMessage(String phoneNumber, String patrint, String dateTime, String doctor) {
        ArrayList<String> param = new ArrayList<>();
        param.add(" "+patrint+" ");
        param.add(dateTime);
        param.add(" "+doctor+" ");
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, param,smsSign,"","");
            if (result.errMsg.contains("OK")) {
                logger.info("success",result);
            } else {
                logger.info("false",result);
            }
        } catch (HTTPException e) {// HTTP响应码错误
            logger.info(e.getMessage());
        } catch (JSONException e) {// json解析错误
            logger.info(e.getMessage());
        } catch (IOException e) {// 网络IO错误
            logger.info(e.getMessage());
        } catch (org.json.JSONException e) {
            logger.info(e.getMessage());
        }
    }

}
