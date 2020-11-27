package com.neu.his.cloud.zuul.controller;


import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.common.IErrorCode;
import com.neu.his.cloud.zuul.component.WeChatCode;
import com.neu.his.cloud.zuul.config.Application;
import com.neu.his.cloud.zuul.config.HttpClientUtil;
import com.neu.his.cloud.zuul.dto.sms.CheckPassword;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffLoginParam;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffParam;
import com.neu.his.cloud.zuul.model.PmsPatient;
import com.neu.his.cloud.zuul.model.SmsStaff;
import com.neu.his.cloud.zuul.service.sms.PmsPatientService;
import com.neu.his.cloud.zuul.service.sms.SmsStaffService;

import com.neu.his.cloud.zuul.service.tms.TestFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.netflix.config.DeploymentContext.ContextKey.appId;

@Controller
@Api(tags = "SmsStaffController", description = "用户管理")
@RequestMapping("/staff")
@CrossOrigin(allowCredentials = "true")
public class SmsStaffController {

    @Autowired
    private SmsStaffService smsStaffService;

    @Autowired
    private TestFeignService testFeignService;

    @Autowired
    private Application application;

    @Autowired
    private PmsPatientService service;

    @Value("${jwt.tokenHeader}")  //自动装载jwt
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;


    /**
     * 描述:登录
     * <p>author: 赵煜
     */
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody SmsStaffLoginParam smsStaffLoginParam, BindingResult result) {
        String token = smsStaffService.login(smsStaffLoginParam.getUsername(), smsStaffLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        //如果 token不等于 null
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap); //为什么要用HashMap进行返回，难道是为了减少实体类？
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAdminInfo(Principal principal) {
        String username = principal.getName();
        SmsStaff smsStaff = smsStaffService.selectByUserName(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", smsStaff.getUsername());
        data.put("id", smsStaff.getId());
        data.put("name", smsStaff.getName());
        data.put("deptId", smsStaff.getDeptId());
        data.put("roleId",smsStaff.getRoleId());
        return CommonResult.success(data);
    }

    @ApiOperation(value = "检查原始密码是否正确")
    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult checkPassword(@RequestBody CheckPassword checkPassword) {
        try {
            CommonResult commonResult = smsStaffService.checkPassword(checkPassword);
            return commonResult;
        } catch (Exception e) {
            return CommonResult.success(e.getMessage());
        }
    }

    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestBody CheckPassword checkPassword) {
        CommonResult commonResult = smsStaffService.updatePassword(checkPassword);
        return commonResult;
    }

    /**
     * 描述:新增一个用户
     * <p>author:赵煜
     */
    @ApiOperation("新增用户（调用注册接口）")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SmsStaffParam smsStaffParam, BindingResult result){
        SmsStaff smsStaff= smsStaffService.register(smsStaffParam);
        if(!StringUtils.isEmpty(smsStaff)){
            return CommonResult.success(1,"注册成功");
        }
        return CommonResult.success(0,"注册失败");

    }

    //返回unionid
    @ApiOperation("根据code获取openid")
    @RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult getOpenId(String code){
        String url = application.toUrl(code);
        String result = WeChatCode.sendGet(url);
        JSONObject jo = new JSONObject(result);
        if(StringUtils.isEmpty(jo.get("openid"))) {
            return CommonResult.failed((String) jo.get("errmsg"));
        }
        return CommonResult.success(jo);
    }
    @ApiOperation("根据code判断此微信号是否绑定了用户")
    @RequestMapping(value = "/isBandtoWechat", method = RequestMethod.POST)
    @ResponseBody
    public Boolean haveLogin(String openId){
        return service.isBandtoWechat(openId);
    }

    //微信公众号的登录功能
    @ApiOperation("根据code获取openid")
    @RequestMapping(value = "/wxCallBack", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult wxCallBack(@RequestParam("code") String code) {

        String result = HttpClientUtil.doGet(application.toAppUrl(code));
        com.alibaba.fastjson.JSONObject resultObject = JSON.parseObject(result);
        if(!StringUtils.isEmpty(resultObject.get("openid"))){
            String url="https://api.weixin.qq.com/sns/userinfo?"+"access_token="+
                    resultObject.get("access_token")+"&openid="+
                    resultObject.get("openid")+"&lang=zh_CN";
            String s = HttpClientUtil.doGet(url);
            com.alibaba.fastjson.JSONObject resultObject1 = JSON.parseObject(s);
            if(!StringUtils.isEmpty(resultObject1.get("unionid"))){
                return CommonResult.success(resultObject1);
            }
            return CommonResult.failed((String) resultObject1.get("errmsg"));
        }
        return CommonResult.failed((String) resultObject.get("errmsg"));
    }


//
//    /**
//     * 描述:登录
//     * <p>author: 赵煜
//     */
//    @ApiOperation(value = "test")
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    @ResponseBody
//    @PreAuthorize("hasAuthority('dept:zzz')")
//    public CommonResult test() {
//        return CommonResult.success("success");
//    }

//    /**
//     * 描述:登录
//     * <p>author: 赵煜
//     */
//    @ApiOperation(value = "testHi")
//    @RequestMapping(value = "/testHi", method = RequestMethod.GET)
//    @ResponseBody
////    @PreAuthorize("hasAuthority('dept:create')")
//    public CommonResult testHi(@RequestParam("message") String message) {
//        return CommonResult.success(testFeignService.hiTest(message));
//    }


}
