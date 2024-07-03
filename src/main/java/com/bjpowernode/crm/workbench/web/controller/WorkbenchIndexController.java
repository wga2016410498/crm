package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {//因为在跳转的时候，在不同的webinf目录下面，不能够直接访问webinf，就得需要controller去访问
    //跳转到业务主页面
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
