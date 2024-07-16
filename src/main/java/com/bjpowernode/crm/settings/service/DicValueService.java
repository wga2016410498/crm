package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.pojo.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> selectDicValueByType(String typeCode);
}
