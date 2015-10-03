package com.evanzeimet.queryinfo.builder;

import com.evanzeimet.queryinfo.Condition;
import com.evanzeimet.queryinfo.ConditionOperator;
import com.evanzeimet.queryinfo.QueryInfoMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionBuilderTest {

    private ConditionBuilder builder;

    @Before
    public void setUp() {
        builder = new ConditionBuilder();
    }

    @Test
    public void build() {
        String givenLhs = "lhs";
        ConditionOperator givenOperator = ConditionOperator.EQUAL_TO;
        String givenRhs = "rhs";

        Condition actualCondition = builder.leftHandSide(givenLhs)
                .operator(givenOperator)
                .rightHandSide(givenRhs)
                .build();

        String actualLhs = actualCondition.getLeftHandSide();
        assertEquals(givenLhs, actualLhs);

        String expectedOperator = givenOperator.getText();
        String actualOperator = actualCondition.getOperator();
        assertEquals(expectedOperator, actualOperator);

        String actualRhs = actualCondition.getRightHandSide();
        assertEquals(givenRhs, actualRhs);
    }
}
