package com.unfbx.chatgptsteamoutput.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.javafx.collections.SetListenerHelper;
import com.unfbx.chatgptsteamoutput.config.*;
import com.unfbx.chatgptsteamoutput.dto.GptAccountDTO;
import com.unfbx.chatgptsteamoutput.entity.GptAccount;
import com.unfbx.chatgptsteamoutput.seviceImpl.GptAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@CrossOrigin
@Slf4j
public class AccountController {
    @Autowired
    GptAccountServiceImpl gptAccountService;
    String type3 = "3.5";
    String type4 = "4.0";

    @PostMapping("/get/token")
    @ResponseBody
    public Result<?> getToken(HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        String md5 = MD5.hashPassword(ipAddress);
        log.info("ipAddress:{}",ipAddress);
        return Result.success(md5);
        //throw new ServiceException(ErrorCodeEnum.ERROR.getCode(), "Sorry, there is no 3.5 account available at the moment. Please contact the administrator.");
    }

    private GptAccount bindAccount35(String ipAddress, String token) {
        //如果没有，那么要绑定一个账号3.5
        LambdaQueryWrapper<GptAccount> newWrapper = Wrappers.lambdaQuery();
        newWrapper.eq(GptAccount::getAccountType, type3).last(" and token is null order by create_time desc limit 1 ");
        GptAccount newGptAccount = gptAccountService.getOne(newWrapper);
        //有这个号并且，这个号没有被领取过
        if (ObjectUtil.isNotEmpty(newGptAccount) && ObjectUtil.isEmpty(newGptAccount.getToken())) {
            newGptAccount.setUpdateBy(ipAddress);
            newGptAccount.setUpdateTime(new Date());
            newGptAccount.setToken(token);
            gptAccountService.updateById(newGptAccount);
            log.info("bindAccount35:{}",newGptAccount);
            return newGptAccount;
        }
        return null;
    }

    /**
     * 发送3.5账号
     *
     * @param token
     * @return
     */
    @PostMapping("/send/3.5account")
    @ResponseBody
    public Result<GptAccountDTO> sendAccount(@Param("token") String token,HttpServletRequest request) {

        //校验token 一个token只能领取一个账号
        LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getToken, token)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3).eq(GptAccount::getSendFlag,1);//已发送
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);
        //如果有token记录说明已经领取了
        if (ObjectUtil.isNotEmpty(one)) {
            GptAccountDTO conversion = BeanUtil.conversion(one, GptAccountDTO.class);
            conversion.setEmail(null);
            conversion.setEmailPwd(null);
            return Result.success(conversion);
            //throw new ServiceException(ErrorCodeEnum.ERROR.getCode(), "Sorry, you have already claimed the 3.5 account.Please recharge within 24 hours and confirm the receipt in our system. We will pay you the reward.");
        }
        GptAccount newGptAccount = bindAccount35(getIpAddress(request), token);
        //if (newGptAccount != null) return Result.success();
        //校验token 一个token只能领取一个账号
    /*    LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getToken, token)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3);
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);*/
        if (newGptAccount != null) {
            GptAccountDTO conversion = BeanUtil.conversion(newGptAccount, GptAccountDTO.class);
            conversion.setEmail(null);
            conversion.setEmailPwd(null);
            newGptAccount.setSendFlag(1);//已发送
            gptAccountService.updateById(newGptAccount);
            log.info("sendAccount3.5:{}",newGptAccount);
            return Result.success(conversion);
        }
        throw new ServiceException(ErrorCodeEnum.ERROR.getCode(), "No matching 3.5 account.");
    }

    /**
     * 接收4.0账号
     *
     * @param
     * @return
     */
    @PostMapping("/receive/4.0account")
    @ResponseBody
    public Result<String> receiveAccount(HttpServletRequest request, @Param("accountName") String accountName, @Param("accountPwd") String accountPwd
            , @Param("loginLocation") String loginLocation, @Param("cardNum") String cardNum, @Param("token") String token) {

        String accountNameTrim = accountName.trim();
        String cardNumTrim = cardNum.trim();
        String loginLocationTirm = loginLocation.trim();
        //根据token查询
        LambdaQueryWrapper<GptAccount> gptAccountLambdaQueryWrapper = Wrappers.lambdaQuery();
        gptAccountLambdaQueryWrapper.eq(GptAccount::getAccountName, accountNameTrim)
                .eq(GptAccount::getDelFlag, 0).eq(GptAccount::getAccountType, type3);
        GptAccount one = gptAccountService.getOne(gptAccountLambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(one)) {
            one.setAccountType(type4);
            one.setSalesFlag(0);//0.未出售  1.已出售  这个是4.0的标识是否已经出售
            one.setVerification(0);//0.未校验是4.0   1.已校验    目前需要人工验证是否是4.0
            one.setUpdateTime(new Date());
            one.setUpdateBy(token);
            one.setLoginLocation(loginLocationTirm);
            one.setCardNum(cardNumTrim);
            one.setToken(null);//4.0回收过后3.5又可以领取
            gptAccountService.updateById(one);
            log.info("receiveAccount4.0:{}",one);
            return Result.success("Recycling successful, we will verify the 4.0 account and make payment to you in one hour.");
        }
        throw new ServiceException(ErrorCodeEnum.ERROR.getCode(), "Recycling failed, no valid account found.. Please contact the administrator.");
    }

    @PostMapping("/add/account35")
    @ResponseBody
    public Result<GptAccount> addAccount35(@RequestBody GptAccountDTO gptAccountDTO) {

        GptAccount gptAccount = BeanUtil.conversion(gptAccountDTO, GptAccount.class);
        gptAccount.setId(PkUtil.getId());
        gptAccount.setCreateTime(new Date());
        gptAccount.setCreateBy("system");
        gptAccountService.save(gptAccount);
        return Result.success(gptAccount);
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