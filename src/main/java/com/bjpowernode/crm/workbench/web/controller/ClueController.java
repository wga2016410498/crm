package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.DicValue;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    UserService userService;
    @Autowired
    DicValueService dicValueService;

    @Autowired
    ActivityService activityService;
    @Autowired
    ClueService clueService;

    @Autowired
    ClueRemarkService clueRemarkService;

    @Autowired
    ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.SelectAllUsers();
        List<DicValue> appellationList = dicValueService.selectDicValueByType("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValueByType("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValueByType("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User) session.getAttribute(Contants.USER_SESSION_INFO);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        int ret = clueService.insertClue(clue);
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统繁忙,请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统繁忙，请稍后...");
        }
       return  returnObject;
    }

    /*
    分页查询线索表
     */
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String name,String company,String phone,String mphone,String owner,String state,String source,
                                              int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("owner",owner);
        map.put("state",state);
        map.put("source",source);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用Service层
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        //根据查询结果，生成相应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return  retMap;
    }
    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        int ret = clueService.deleteClueByIds(id);
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统繁忙,请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统繁忙，请稍后...");
        }
        return  returnObject;
    }
    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id){
        Clue clue = clueService.queryClueById(id);
        return clue;
    }

    @RequestMapping("/workbench/clue/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue,HttpSession session){
        User user = (User) session.getAttribute(Contants.USER_SESSION_INFO);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject =new ReturnObject();
        int ret = clueService.saveEditClue(clue);
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统繁忙,请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统繁忙，请稍后...");
        }
        return  returnObject;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailByClueId(id);
        request.setAttribute("clue",clue);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForClueDetailById(id);
        request.setAttribute("remarkList",clueRemarkList);
        List<Activity> activityList = clueService.queryActivityForDetailByClueId(id);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        return  activityList;
    }


    @RequestMapping("/workbench/clue/bulidConnectionBetweenClueAndActivity.do")
    @ResponseBody
    public Object saveBulidConnectionBetweenClueAndActivity(String[] activityId,String clueId){
        ClueActivityRelation car = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for(String ai:activityId){
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }
        int ret = clueActivityRelationService.saveBuildConnectionBetweenClueAndActivity(relationList);
        ReturnObject returnObject = new ReturnObject();
        try{
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
                returnObject.setMessage("系统繁忙,请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统繁忙，请稍后...");
        }
        return  returnObject;
    }

    @RequestMapping("/workbench/clue/deleteClueAndActivityRelation.do")
    @ResponseBody
    public Object deleteClueAndActivityRelation(ClueActivityRelation clueActivityRelation){

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除线索和市场活动的关联关系
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(clueActivityRelation);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue  clue = clueService.queryClueForDetailByClueId(id);
        List<DicValue> stageList = dicValueService.selectDicValueByType("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.USER_SESSION_INFO,session.getAttribute(Contants.USER_SESSION_INFO));
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存线索转换
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FALSE);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
}
