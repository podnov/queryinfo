package com.evanzeimet.queryinfo;

import java.io.Serializable;

public interface Sort extends Serializable {

    String getDirection();

    void setDirection(String direction);

    String getFieldName();

    void setFieldName(String fieldName);

}
