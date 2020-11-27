package com.neu.his.cloud.zuul.util;

import cn.hutool.json.JSONObject;
import com.neu.his.cloud.zuul.controller.bms.wxpay.WXPayUtil;
import springfox.documentation.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GZHPay{

//    public static String appid = "wx5413df4415f5b4eb";
//    public static String appsecret = "b30b3c27b3f8c21003bb5c48d64eea3e";

	public static String getAccessToken(String appid,String appsecret) {
		System.out.println("获取微信AccessToken");
	    String access_token = "";  
	    String grant_type = "client_credential";//获取access_token填写client_credential   
	    String AppId=appid;
	    String secret=appsecret;//第三方用户唯一凭证密钥，即appsecret
		System.out.println("AppId"+AppId+"secret"+secret);
	    //这个url链接地址和参数皆不能变  
	    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;
	       
	    try {  
	        URL urlGet = new URL(url);
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
	        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
	        http.connect();  
	        InputStream is = http.getInputStream();
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        String message = new String(jsonBytes, "UTF-8");  
	        JSONObject demoJson = new JSONObject(message);
	        System.out.println("JSON字符串："+demoJson);
	        access_token = (String)demoJson.get("access_token");
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    return access_token;  
	}  
	
	public static String getTicket(String access_token) {  
	    String ticket = null;  
	    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变  
	    try {  
	        URL urlGet = new URL(url);  
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
	        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
	        http.connect();  
	        InputStream is = http.getInputStream();  
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        String message = new String(jsonBytes, "UTF-8");  
	        JSONObject demoJson = new JSONObject(message);
	        System.out.println("JSON字符串："+demoJson);  
	        ticket = (String)demoJson.get("ticket");
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    return ticket;  
	}  	
	
	public static String SHA1(String decript) {  
	    try {  
	        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
	        digest.update(decript.getBytes());
            byte[] messageDigest = digest.digest();
	        // Create Hex String  
	        StringBuffer hexString = new StringBuffer();  
	        // 字节数组转换为 十六进制 数  
	            for (int i = 0; i < messageDigest.length; i++) {  
	                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
	                if (shaHex.length() < 2) {  
	                    hexString.append(0);  
	                }  
	                hexString.append(shaHex);  
	            }  
	            return hexString.toString();  
	   
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();  
	        }  
	        return "";  
	}
	
	private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for"); 
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {  
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
            System.out.println("Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
            System.out.println("X-Real-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
            System.out.println("getRemoteAddr ip: " + ip);
        } 
        System.out.println("获取客户端ip: " + ip);
        return ip;  
    }
	
//	public String getAccessTokenValue(){
//		if(!ToolRedis.exists("wx_accessToken")){
//			String accessToken=getAccessToken();
//			if(!accessToken.equals("")){
//			ToolRedis.set("wx_accessToken", accessToken);
//			ToolRedis.expire("wx_accessToken", 7200);
//			}
//		}
//		return ToolRedis.get("wx_accessToken");
//	}
//
//	public String getTicketValue(){
//		if(!ToolRedis.exists("wx_ticket")){
//			String accessToken=getAccessTokenValue();
//			String ticket=getTicket(accessToken);
//			ToolRedis.set("wx_ticket", ticket);
//			ToolRedis.expire("wx_ticket", 7200);
//		}
//		return ToolRedis.get("wx_ticket");
//	}
//
//	/**
//	 * 获取微信accessToken
//	 */
//	public void getWxAccessToken(){
//		if(ToolRedis.exists("wx_accessToken")){
//			renderJson(ToolJson.Jsondata("{\"accessToken\":\""+ToolRedis.get("wx_accessToken")+"\"}",1,"获取accessToken成功"));
//		}else {
//			String accessToken=getAccessTokenValue();
//			ToolRedis.set("wx_accessToken", accessToken);
//			ToolRedis.expire("wx_accessToken", 7200);
//			renderJson(ToolJson.Jsondata("{\"accessToken\":\""+ToolRedis.get("wx_accessToken")+"\"}",1,"获取accessToken成功"));
//		}
//
//	}
//
//	/**
//	 * 获取微信ticket
//	 */
//	public void getWxTicket(){
//		if(ToolRedis.exists("wx_ticket")){
//			renderJson(ToolJson.Jsondata("{\"ticket\":\""+ToolRedis.get("wx_ticket")+"\"}",1,"获取ticket成功"));
//		}else {
//			String ticket=getTicketValue();
//			ToolRedis.set("wx_ticket", ticket);
//			ToolRedis.expire("wx_ticket", 7200);
//			renderJson(ToolJson.Jsondata("{\"ticket\":\""+ToolRedis.get("wx_ticket")+"\"}",1,"获取ticket成功"));
//		}
//
//	}
	/**
	 * 获取微信签名
	 */
	public static String getWxSignature(String noncestr,String timestamp,String url,String appid,String appsecret){
		System.out.println("开始请求微信签名");
		//1、获取AccessToken  
	    String accessToken = getAccessToken(appid,appsecret);
	      
	    //2、获取Ticket  
	    String jsapi_ticket = getTicket(accessToken);
	      
//	    //3、时间戳和随机字符串
//	    String noncestr = getPara("noncestr"); // 获得随机字符串
//	    String timestamp =getPara("timestamp"); //获取时间戳时间戳

	    //4、获取url


	    System.out.println("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);  
	    //5、将参数排序并拼接字符串  
	    String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;  	     
	    //6、将字符串进行sha1加密  
	    String signature =SHA1(str);  
	    System.out.println("参数："+str+"\n签名："+signature);  
//	    //获取客户端的IP地址
//	    String ip=getIpAddr(request);
//	    System.out.println("参数：IP\n地址："+ip);
//	    renderJson(ToolJson.Jsondata("{\"signature\":\""+signature+"\",\"IP\":\""+ip+"\"}",1,"获取签名成功"));

        return signature;
	}


//    // 微信公众号支付获得支付签名paySign
//    public static String getPaySign(String timeStamp,String nonceStr,String prepay_id,String appid) throws Exception {
//        System.out.println("打印了哈timeStamp:" + timeStamp + "打印了哈nonceStr:" + nonceStr + "打印了哈prepay_id:" + prepay_id);
//        SortedMap<String, String> packageParams = new TreeMap<String, String>();
//        packageParams.put("appId", appid);
//        packageParams.put("timeStamp", timeStamp);
//        packageParams.put("nonceStr", nonceStr);
//        packageParams.put("package", prepay_id);
//        packageParams.put("signType", "MD5");
//        String packageSign = WXPayUtil.generateSignature(packageParams, "UtQldsIcA8rldkOUdlyHOdGZzHhJX7fz");
////        packageParams.put("paySign", packageSign);
//        return packageSign;
//    }

    // 获取微信公众号支付openid
    public static String getOpenid(String code,String appid,String appsecret,String grant_type) {
//        String grant_type = "authorization_code";
        String openid = "";
        String createOrderURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret
                + "&code=" + code + "&grant_type="+grant_type;
        JSONObject jo = new JSONObject(sendGet(createOrderURL));
        openid = String.valueOf(jo.get("openid"));
        return openid;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 元转换成分
     *
     * @param
     * @return
     */
    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        // 金额转化为分为单位
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        // 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }
}
	