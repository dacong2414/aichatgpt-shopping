package com.unfbx.chatgptsteamoutput.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home(HttpServletRequest request) {
        //request.getSession().setAttribute("msg","session");
        return "home";
    }
    @RequestMapping("/chatJsp")
    public String chat(HttpServletRequest request) {
        //request.getSession().setAttribute("msg","session");
        return "chat";
    }
}