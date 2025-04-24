package com.mytradein.model;

public enum DeviceConditions {
    EXCELLENT("excellent"),
    GOOD("good"),
    POOR("poor"),
    BROKEN("broken");

    public final String label;

    public String getLabel() {
        return label;
    }

    DeviceConditions(String label) {
        this.label = label;
    }
}
