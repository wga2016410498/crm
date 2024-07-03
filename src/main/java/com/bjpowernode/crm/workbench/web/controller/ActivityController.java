package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.SelectAllUsers();
        request.setAttribute("userList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object SavaCreateActivity(Activity activity, HttpSession session){
        User user = (User)session.getAttribute(Contants.USER_SESSION_INFO);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        int ret = activityService.saveCreateActivity(activity);
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

        @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
        @ResponseBody
        public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
            //封装参数
            Map<String,Object> map=new HashMap<>();
            map.put("name",name);
            map.put("owner",owner);
            map.put("startDate",startDate);
            map.put("endDate",endDate);
            map.put("beginNo",(pageNo-1)*pageSize);
            map.put("pageSize",pageSize);
            //调用service层方法，查询数据
            List<Activity> activityList=activityService.queryActivityByConditionForPage(map);
            int totalRows=activityService.queryCountOfActivityByCondition(map);
            //根据查询结果结果，生成响应信息
            Map<String,Object> retMap=new HashMap<>();
            retMap.put("activityList",activityList);
            retMap.put("totalRows",totalRows);
            return retMap;
        }

        @RequestMapping("/workbench/activity/deleteActivityIds.do")
        @ResponseBody
        public Object deleteActivityByIds(String[] id){
            ReturnObject returnObject = new ReturnObject();

            try{
                int ret = activityService.deleteActivityByIds(id);
                if(ret>0){
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                }else{
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                    returnObject.setMessage("系统忙，请稍后重试...");
                }
            }catch (Exception e){
                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
            return returnObject;
        }

        @RequestMapping("/workbench/activity/queryActivityById.do")
        @ResponseBody
        public Object queryActivityById(String id){
            Activity activity = activityService.selectActivityById(id);
        return activity;
    }
}
