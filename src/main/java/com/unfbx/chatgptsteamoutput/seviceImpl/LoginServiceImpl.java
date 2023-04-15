package com.unfbx.chatgptsteamoutput.seviceImpl;/**
 * @Title: yangsong
 * @ProjectName springclould2021
 * @author yangsong
 * @date 2021/2/30:45
 * @Description: TODO
 */


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unfbx.chatgptsteamoutput.config.BeanUtil;
import com.unfbx.chatgptsteamoutput.config.MD5;
import com.unfbx.chatgptsteamoutput.config.PkUtil;
import com.unfbx.chatgptsteamoutput.config.UuidKeys;
import com.unfbx.chatgptsteamoutput.dto.LoginDTO;
import com.unfbx.chatgptsteamoutput.entity.Login;
import com.unfbx.chatgptsteamoutput.entity.mapper.LoginMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * author yangsong
 * date 2021/2/3 0:45
 **/
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> {

    public Login authenticateUser(String loginName, String password) {
        LambdaQueryWrapper<Login> loginLambdaQueryWrapper = Wrappers.lambdaQuery();
        loginLambdaQueryWrapper.eq(Login::getLoginName, loginName).eq(Login::getDelFlag, 0);
        Login login = this.getOne(loginLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(login)) {
            return null;
        }
        if (login.getPasswd().equals(MD5.hashPassword(password))) {
            return login;
        }
        return null;
    }

    public Login addOrUpdateUser(LoginDTO loginDTO, HttpServletRequest request) {
        LambdaQueryWrapper<Login> loginLambdaQueryWrapper = Wrappers.lambdaQuery();
        loginLambdaQueryWrapper.eq(Login::getLoginName, loginDTO.getLoginName()).eq(Login::getDelFlag, 0);
        Login login = this.getOne(loginLambdaQueryWrapper);
        Login conversion = BeanUtil.conversion(loginDTO, Login.class);
        if (login == null) {
            conversion.setId(PkUtil.getId());
            conversion.setPasswd(MD5.hashPassword(conversion.getPasswd()));
            conversion.setNickName("ai-shopping");
            conversion.setShoppingKey(UuidKeys.getShoppingKey());
            conversion.setCreateTime(new Date());
            conversion.setCreateBy("system");
            conversion.setDelFlag(0);
            conversion.setFromLoginName("no");
            this.save(conversion);
        }else {
            conversion.setId(login.getId());
            conversion.setPasswd(MD5.hashPassword(conversion.getPasswd()));
            this.updateById(conversion);
        }
        // Login user = (Login) request.getSession().getAttribute("user");
        // Optional.ofNullable(user).ifPresent(userIfp->{ conversion.setCreateBy(user.getLoginName());});
        return conversion;
    }

    public Login getLoginBykey(String key) {
        LambdaQueryWrapper<Login> loginLambdaQueryWrapper = Wrappers.lambdaQuery();
        loginLambdaQueryWrapper.eq(Login::getShoppingKey, key).eq(Login::getDelFlag, 0);
        return this.getOne(loginLambdaQueryWrapper);
    }
}
