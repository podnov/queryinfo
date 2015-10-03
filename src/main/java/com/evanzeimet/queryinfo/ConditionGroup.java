package com.evanzeimet.queryinfo;

import java.io.Serializable;
import java.util.List;

public interface ConditionGroup extends Serializable {

    List<ConditionGroup> getConditionGroups();

    void setConditionGroups(List<ConditionGroup> conditionGroups);

    List<Condition> getConditions();

    void setConditions(List<Condition> conditions);

    String getOperator();

    void setOperator(String operator);
}
