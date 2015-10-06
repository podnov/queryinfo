package com.evanzeimet.queryinfo.condition;

import java.io.Serializable;

public interface Condition extends Serializable {

	String getLeftHandSide();

	void setLeftHandSide(String leftHandSide);

	String getOperator();

	void setOperator(String operator);

	String getRightHandSide();

	void setRightHandSide(String rightHandSide);

}
