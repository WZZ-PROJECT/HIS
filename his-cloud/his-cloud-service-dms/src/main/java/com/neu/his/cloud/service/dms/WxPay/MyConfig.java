package com.neu.his.cloud.service.dms.WxPay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

    /** 加载证书  这里证书需要到微信商户平台进行下载*/
    private byte[] certData;

    public MyConfig() throws Exception {
        //证书只是撤销订单时会使用，在这里的demo中没有用到
        /*String certPath = "自己商户平台下载的证书";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();*/
    }

    /**
     * 设置自己的appId ,商户号,密钥
     * @return
     */
    @Override
    public String getAppID() {
        return "wx5413df4415f5b4eb";
    }//自己的appId

    @Override
    public String getMchID() {
        return "1602987321";
    }//自己的商户号

    @Override
    public String getKey() {
        return "UtQldsIcA8rldkOUdlyHOdGZzHhJX7fz";
    }//自己的密钥

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    /**
     *这里的方法，实现必须如下
     */
    IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo("api.mch.weixin.qq.com", true);
            }
        };
        return iwxPayDomain;
    }
}

