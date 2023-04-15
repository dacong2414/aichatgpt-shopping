package com.unfbx.chatgptsteamoutput.controller;

import com.unfbx.chatgptsteamoutput.config.ErrorCodeEnum;
import com.unfbx.chatgptsteamoutput.config.ServiceException;
import com.unfbx.chatgptsteamoutput.dto.LoginDTO;
import com.unfbx.chatgptsteamoutput.entity.Login;
import com.unfbx.chatgptsteamoutput.seviceImpl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
        Login user = loginService.authenticateUser(loginDTO.getLoginName(), loginDTO.getPasswd());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addOrUpdateUser")
    public ResponseEntity<?> addUser(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
        Login user = loginService.addOrUpdateUser(loginDTO,request);
        if (user == null) {
            throw new ServiceException(ErrorCodeEnum.ERROR.getCode(),"用户新增失败");
        }
        return ResponseEntity.ok(user);
    }

}