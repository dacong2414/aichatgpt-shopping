package com.unfbx.chatgptsteamoutput.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.javafx.collections.SetListenerHelper;
import com.unfbx.chatgptsteamoutput.config.MD5;
import com.unfbx.chatgptsteamoutput.entity.GptAccount;
import com.unfbx.chatgptsteamoutput.seviceImpl.GptAccountServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class AccountController {
    @Autowired
    GptAccountServiceImpl gptAccountService;
    String type3="3.5";
    String type4="4.0";

    @GetMapping("/get/token")
    public String getToken(HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        String md5 = MD5.hashPassword(ipAddress);
        //校验token 一个token只能领取一个账号
        LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getToken, md5)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3);
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);
        //如果有token记录说明已经领取了
        if (ObjectUtil.isNotEmpty(one)){
            return null;
        }
        //如果没有，那么要绑定一个账号3.5
        LambdaQueryWrapper<GptAccount> newWrapper = Wrappers.lambdaQuery();
        newWrapper.eq(GptAccount::getAccountType,type3).last(" order by create_time desc limit 1 ");
        GptAccount newGptAccount = gptAccountService.getOne(newWrapper);
        //有这个号并且，这个号没有被领取过
        if (ObjectUtil.isNotEmpty(newGptAccount)&&ObjectUtil.isEmpty(newGptAccount.getToken())){
            newGptAccount.setUpdateBy(ipAddress);
            newGptAccount.setUpdateTime(new Date());
            newGptAccount.setToken(md5);
            gptAccountService.updateById(newGptAccount);
            return md5;
        }
        return null;
    }

    /**
     * 发送3.5账号
     *
     * @param token
     * @return
     */
    @GetMapping("/send/account")
    public GptAccount sendAccount(@Param("token") String token) {
        //校验token 一个token只能领取一个账号
        LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getToken, token)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3);
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);
        return one;
    }

    /**
     * 接收4.0账号
     *
     * @param
     * @return
     */
    @GetMapping("/receive/account")
    public String receiveAccount(@Param("accountName") String accountName,@Param("accountPwd") String accountPwd
            ,@Param("loginLocation") String loginLocation,@Param("cardNum") String cardNum,@Param("token") String token) {

        //根据token查询
        LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getAccountName, accountName)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3);
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(one)){
            one.setAccountType(type4);
            one.setSalesFlag(0);//0.未出售  1.已出售  这个是4.0的标识是否已经出售
            one.setVerification(1);//0.已校验是4.0   1.未校验    目前需要人工验证是否是4.0
            one.setUpdateTime(new Date());
            one.setUpdateBy(token);
            one.setLoginLocation(loginLocation);
            one.setCardNum(cardNum);
            gptAccountService.updateById(one);
            return "Success";
        }
        return "Fail";
    }



    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }
}