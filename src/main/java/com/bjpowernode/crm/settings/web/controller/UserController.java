package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    /**
     * url要和controller方法处理完请求之后，相应信息返回的目录资源保持一致。
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse httpServletResponse, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        //进行判断
        if(user==null){
            //登陆失败，用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("用户名或密码错误");
        }else{
            String nowStr = DateUtils.formateDateTime(new Date());
            if(nowStr.compareTo(user.getExpireTime())>0){
                //登陆失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())) {
                //登陆失败，账号被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("账号被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("ip不符合");
            }else{
                session.setAttribute(Contants.USER_SESSION_INFO,user);
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("登陆成功");
                //添加cookie
                if("true".equals(isRemPwd)){
                    Cookie c1= new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    httpServletResponse.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    httpServletResponse.addCookie(c2);
                }else{
                    Cookie c1= new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    httpServletResponse.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    httpServletResponse.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse httpServletResponse,HttpSession session){
        //销毁cookie 服务器为什么不能直接删除掉cookie
        Cookie c1= new Cookie("loginAct","1");
        c1.setMaxAge(0);
        httpServletResponse.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        httpServletResponse.addCookie(c2);
        //销毁session
        session.invalidate();
        return "redirect:/";
    }
}
