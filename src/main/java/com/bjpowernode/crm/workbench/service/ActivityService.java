package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String Id);

    int updateActivity(Activity activity);

    List<Activity> queryAllActivitys();

    List<Activity> queryChoosedActivitys(String[] ids);

    int insertActivityByList(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map);
}
