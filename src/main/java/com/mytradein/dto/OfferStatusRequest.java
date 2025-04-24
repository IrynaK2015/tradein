package com.mytradein.dto;

public class OfferStatusRequest {
    private final String offerStatus;

    public OfferStatusRequest(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getOfferStatus() {
        return offerStatus;
    }
}
