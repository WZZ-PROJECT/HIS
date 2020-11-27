package com.neu.his.cloud.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static com.netflix.config.DeploymentContext.ContextKey.appId;

@Configuration
public class Application {

    @Value("${appid}")
    private String appid;
    @Value("${secret}")
    private String secret;
    @Value("${grantType}")
    private String grantType;

    public String toUrl(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+this.appid+"&secret="+this.secret+"&js_code="
                +code+"&grant_type="+this.grantType;
        return url;
    }

    @Value("${AppId}")
    private String AppId;
    @Value("${Appsecret}")
    private String Appsecret;
    @Value("${grantType}")
    private String AppGrant_type;

    public  String toAppUrl(String code){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + this.AppId +
                "&secret=" + this.Appsecret +
                "&code=" + code +
                "&grant_type="+ this.AppGrant_type;
        return url;
    }


}
