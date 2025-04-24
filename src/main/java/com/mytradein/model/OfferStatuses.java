package com.mytradein.model;

public enum OfferStatuses {
    NEW("new"),
    PROCESSING("processing"),
    APPROVED("approved"),
    REJECTED("rejected"),
    PURCHASED("purchased");

    public final String label;

    public String getLabel() {
        return label;
    }

    OfferStatuses(String label) {
        this.label = label;
    }
}
