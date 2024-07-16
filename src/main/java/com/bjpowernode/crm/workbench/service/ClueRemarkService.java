package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForClueDetailById(String id);

    int saveCreateClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);

    int saveEditClueRemark(ClueRemark clueRemark);
}
