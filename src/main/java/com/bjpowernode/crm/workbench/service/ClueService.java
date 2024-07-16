package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int insertClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String, Object> map);

    int queryCountOfClueByCondition(Map<String, Object> map);

    int deleteClueByIds(String[] ids);

    Clue queryClueById(String id);

    int saveEditClue(Clue clue);

    List<Activity> queryActivityForDetailByClueId(String id);

    Clue queryClueForDetailByClueId(String id);

    void saveConvertClue(Map<String,Object> map);
}
