package com.mytradein.dto;

import com.mytradein.model.Offer;

public class OfferStatusResponse {

   private final Long id;

   private final String offerStatus;

    public OfferStatusResponse(Offer offer) {
        this.id = offer.getId();
        this.offerStatus = offer.getOfferStatus();
    }

    public Long getId() {
        return id;
    }

    public String getOfferStatus() {
        return offerStatus;
    }
}
