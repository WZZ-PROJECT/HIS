package com.neu.his.cloud.zuul.service.sms.impl;


import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.sms.CheckPassword;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffParam;
import com.neu.his.cloud.zuul.mapper.SmsStaffMapper;
import com.neu.his.cloud.zuul.model.SmsStaff;
import com.neu.his.cloud.zuul.model.SmsStaffExample;
import com.neu.his.cloud.zuul.service.sms.SmsStaffService;
import com.neu.his.cloud.zuul.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
@Service
public class SmsStaffServiceImpl implements SmsStaffService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsStaffServiceImpl.class);
    @Autowired
    private SmsStaffMapper smsStaffMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Value("${jwt.tokenHead}")  //application.yml
    private String tokenHead;

    public String test(){
        return tokenHead;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);//返回的是一个userDetails的实现类AdminUserDetails
            if(!passwordEncoder.matches(password,userDetails.getPassword())){  //password是从前端过来未经过编译的，而userDetails.getPassword()是从数据库中出来经过编译的
                throw new BadCredentialsException("密码不正确");
            }
            //创建一个新的token
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);  //在securityContext中添加该验证信息
            token = jwtTokenUtil.generateToken(userDetails);
            //updateLoginTimeByUsername(username);
            //insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }
    @Override
    public SmsStaff selectByUserName(String username) {
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andUsernameEqualTo(username).andStatusNotEqualTo(0);//查找名字相同的且状态不为0的用户
        List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(example);
        if (smsStaffList != null && smsStaffList.size() > 0) {
            return smsStaffList.get(0);
        }
        return null;
    }


    /**
     *
     * zuul网关鉴权使用 其实不会有员工注册这个功能，只有创建员工
     * @param smsStaffParam
     * @return
     */
    @Override
    public SmsStaff register(SmsStaffParam smsStaffParam) {
        SmsStaff smsStaff = new SmsStaff();
        BeanUtils.copyProperties(smsStaffParam, smsStaff);
        //封装dto中没有传来的属性
        smsStaff.setCreateTime(new Date());
        smsStaff.setStatus(1);
        //查询是否有相同用户名的用户
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andUsernameEqualTo(smsStaff.getUsername());//spring MVC必须保证Username不为null
        List<SmsStaff> lists = smsStaffMapper.selectByExample(example);

        List<SmsStaff> staffList = smsStaffMapper.selectByExample(example);
        if (staffList.size() > 0) { //存在,若通过此接口进行创建，则重复数量只可能为1
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(smsStaff.getPassword());
        smsStaff.setPassword(encodePassword);
        System.out.println(smsStaff.getPassword());
        smsStaffMapper.insert(smsStaff);
        return smsStaff;  //返回对象的密码是经过加密的
    }

    @Override
    public CommonResult checkPasswords(CheckPassword checkPassword) {
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(checkPassword.getStaffId());
        if (!StringUtils.isEmpty(smsStaff)) {
            if (!passwordEncoder.matches(checkPassword.getNewPassword(),smsStaff.getPassword())) {
                return CommonResult.success(1,"校验正确");
            }
        }
        return CommonResult.success(-1,"新密码与原始密码重复");
    }

    @Override
    public CommonResult checkPassword(CheckPassword checkPassword) {
        //将密码进行加密操作
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(checkPassword.getStaffId());
        if (!StringUtils.isEmpty(smsStaff)) {
            if (passwordEncoder.matches(checkPassword.getOldPassword(),smsStaff.getPassword())) {
                return CommonResult.success(1,"校验正确");
            }
        }
        return CommonResult.success(0,"原始密码不正确");
    }

    @Override
    public CommonResult updatePassword(CheckPassword checkPassword) {
        CommonResult commonResult;
        commonResult = checkPassword(checkPassword);
        if ((Integer) commonResult.getData() != 0) {
            commonResult = checkPasswords(checkPassword);
            if ((Integer) commonResult.getData() != -1) {
                SmsStaff smsStaff = new SmsStaff();
                smsStaff.setPassword(passwordEncoder.encode(checkPassword.getNewPassword()));
                smsStaff.setId(checkPassword.getStaffId());
                int i = smsStaffMapper.updateByPrimaryKeySelective(smsStaff);
                return CommonResult.success(i,"密码更新成功");
            }
        }
        return commonResult;
    }

    @Override
    public String getToken(String username) {
        String token = null;
        try {
            token = jwtTokenUtil.APPGenerateToken(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }
}
