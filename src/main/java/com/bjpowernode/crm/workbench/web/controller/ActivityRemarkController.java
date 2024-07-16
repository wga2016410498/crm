package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveCreateRemarkActivity.do")
    @ResponseBody
    public Object saveCreateRemarkActivity(ActivityRemark activityRemark, HttpSession session){
        User user = (User) session.getAttribute(Contants.USER_SESSION_INFO);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateBy(user.getId());
        activityRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG);
        ReturnObject returnObject = new ReturnObject();

        int ret = activityRemarkService.saveCreateActicvityRemark(activityRemark);
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统忙,请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
       int ret =  activityRemarkService.deleteActivityRemark(id);
       ReturnObject returnObject = new ReturnObject();
       try{
           if(ret>0){
               returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
               returnObject.setMessage("系统繁忙,请稍后...");
           }
       }
       catch (Exception e){
           e.printStackTrace();
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
           returnObject.setMessage("系统繁忙,请稍后重试...");
       }
       return returnObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user=(User) session.getAttribute(Contants.USER_SESSION_INFO);
        //封装参数
        activityRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG);

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存修改的市场活动备注
            int ret = activityRemarkService.saveEditActivityRemark(activityRemark);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
}
