package com.mytradein.model;

public enum AuthRoles {
    USER("Manager"),
    ADMIN("Supervisor");

    public final String label;

    public String getLabel() {
        return label;
    }

    AuthRoles(String label) {
        this.label = label;
    }
}
