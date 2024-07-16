package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRemarkService activityRemarkService;
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
        public Object queryActivityById(String id) {
            Activity activity = activityService.queryActivityById(id);
            return activity;
        }
        @RequestMapping("/workbench/activity/saveEditActivity.do")
        @ResponseBody
        public Object saveEditActivity(Activity activity,HttpSession session){
            User user = (User)session.getAttribute(Contants.USER_SESSION_INFO);
            activity.setEditTime(DateUtils.formateDateTime(new Date()));
            activity.setEditBy(user.getId());
            ReturnObject returnObject = new ReturnObject();
            int ret = activityService.updateActivity(activity);
            try
            {
                if(ret>0) {
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                }else {
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                    returnObject.setMessage("系统忙，请稍后...");
                }
            }
            catch(Exception e){
                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
            return returnObject;
    }
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws Exception{
        List<Activity> activityList = activityService.queryAllActivitys();
        HSSFUtils.createExcel(activityList,response);
    }

    @RequestMapping("/workbench/activity/exportChoosedActivity.do")
    public void exportChoosedActivity(@RequestParam(value = "id",required = false)String[] ids, HttpServletResponse response) throws Exception{
        List<Activity> activityList = activityService.queryChoosedActivitys(ids);
        HSSFUtils.createExcel(activityList,response);
    }

    /*
    *实现文件上传功能
    *先把要上传的文件解析到服务器的某一个目录上，然后对目录上的文件进行解析
    * 解析完成之后输入到数据库当中
    */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,String userName,HttpSession session){
        User user=(User) session.getAttribute(Contants.USER_SESSION_INFO);
        ReturnObject returnObject = new ReturnObject();

        try {
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for(int i=1;i<=sheet.getLastRowNum();i++){
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());

                for(int j=0;j<row.getLastCellNum();j++){
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    } else if (j==1) {
                        activity.setStartDate(cellValue);
                    } else if (j==2) {
                        activity.setEndDate(cellValue);
                    } else if (j==3) {
                        activity.setCost(cellValue);
                    } else if (j==4) {
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);
            }
            int ret = activityService.insertActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统忙，请稍后...");
        }
    return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        request.setAttribute("activity",activity);
        List<ActivityRemark> activityRemarkList = activityRemarkService.selectActivityRemarkForDetailByActivityId(id);
        request.setAttribute("remarkList",activityRemarkList);
//        System.out.println(activityRemarkList.get(0));
//        System.out.println(activityRemarkList.get(1));
        return "workbench/activity/detail";
    }
}
