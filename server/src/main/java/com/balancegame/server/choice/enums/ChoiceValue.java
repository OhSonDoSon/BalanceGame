package com.balancegame.server.choice.enums;

public enum ChoiceValue {

    LEFT("L")
    ,RIGHT("R")
    ;
    private final String valueName;

    ChoiceValue(String valueName) {
        this.valueName = valueName;
    }
}
