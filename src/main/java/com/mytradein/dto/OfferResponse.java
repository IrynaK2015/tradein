package com.mytradein.dto;

import com.mytradein.model.Offer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OfferResponse {

    private final Map<String, Object> offerData = new HashMap<>();
    private final Map<String, Object> customerData = new HashMap<>();
    private String token;

    public OfferResponse(Offer offer) {
        setBrand(offer);
        setModel(offer);
        setOfferParameters(offer);
        setCustomerData(offer);
        setSeller(offer);
    }

    public Map getOfferData() {
        return new TreeMap<>(offerData);
    }

    public Map getCustomerData() {
        return new TreeMap<>(customerData);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void setBrand(Offer offer) {
        Map<String, Object> brand = new HashMap<>();
        brand.put("id", offer.getBrandmodel().getBrand().getId());
        brand.put("name", offer.getBrandmodel().getBrand().getBrandname());
        offerData.put("brand", brand);
    }

    private void setModel(Offer offer) {
        Map<String, Object> deviceModel = new HashMap<>();
        deviceModel.put("id", offer.getBrandmodel().getId());
        deviceModel.put("name", offer.getBrandmodel().getModelname());
        offerData.put("model", deviceModel);
    }

    private void setOfferParameters(Offer offer) {
        offerData.put("id", offer.getId());
        offerData.put("imei", offer.getImei());
        offerData.put("constructionYear", offer.getConstructionYear());
        offerData.put("condition", offer.getCondition());
        offerData.put("color", offer.getColor());
        offerData.put("capacity", offer.getCapacity());
        offerData.put("desiredDevice", offer.getDesiredDevice());
        offerData.put("purchasePrice", offer.getPurchasePrice());
        offerData.put("status", offer.getOfferStatus());
    }

    private void setCustomerData(Offer offer) {
        customerData.put("id", offer.getCustomer().getId());
        customerData.put("firstName", offer.getCustomer().getFirstname());
        customerData.put("lastName", offer.getCustomer().getLastname());
        customerData.put("email", offer.getCustomer().getEmail());
        customerData.put("phone", offer.getCustomer().getPhone());
    }

    private void setSeller(Offer offer) {
        Map<String, Object> seller = new HashMap<>();
        seller.put("id", offer.getAuthuser().getId());
        seller.put("username", offer.getAuthuser().getUsername());
        offerData.put("seller", seller);
    }
}
