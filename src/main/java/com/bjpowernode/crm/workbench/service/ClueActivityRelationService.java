package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveBuildConnectionBetweenClueAndActivity(List<ClueActivityRelation> relationList);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
