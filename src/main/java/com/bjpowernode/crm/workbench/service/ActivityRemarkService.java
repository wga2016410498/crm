package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);

    int saveCreateActicvityRemark(ActivityRemark activityRemark);

    int deleteActivityRemark(String id);

    int saveEditActivityRemark(ActivityRemark activityRemark);
}
