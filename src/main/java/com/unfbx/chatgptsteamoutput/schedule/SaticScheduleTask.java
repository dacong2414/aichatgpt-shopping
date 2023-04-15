package com.unfbx.chatgptsteamoutput.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.unfbx.chatgptsteamoutput.entity.Login;
import com.unfbx.chatgptsteamoutput.seviceImpl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    LoginServiceImpl loginService;
    //3.添加定时任务
    @Scheduled(cron = "0 49 16 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        LambdaQueryWrapper<Login> loginLambdaQueryWrapper = Wrappers.lambdaQuery();
        loginLambdaQueryWrapper.eq(Login::getDelFlag,0).last(" and  enable_time>0 ");
        List<Login> list = loginService.list(loginLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(login -> {
                Integer enableTime = login.getEnableTime();
                login.setEnableTime(enableTime-1);
                login.setUpdateBy("saticScheduleTask");
                login.setUpdateTime(new Date());
            });
            loginService.updateBatchById(list);
        }
    }
}
