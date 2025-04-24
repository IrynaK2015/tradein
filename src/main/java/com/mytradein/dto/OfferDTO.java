package com.mytradein.dto;

import com.mytradein.model.Offer;
import jakarta.validation.constraints.*;

public class OfferDTO {

    private String id;

    @NotEmpty(message = "Brand model can't be empty")
    private String brandmodelid;

    @Size(min=15, max=20, message="IMEI has length 17 chars")
    private String imei;

    @Pattern(regexp = "^19|20[0-9]{2}$", message = "Year is incorrect")
    private String constractionyear;

    @Size(min=3, max=10, message="Condition is invalid")    // TBD
    private String condition;

    @Size(min=3, max=20, message="Please define color")
    private String color;

    @Pattern(regexp = "^[0-9]+$", message = "Capacity is invalid")
    private String capacity;

    @Size(min=3, max=255, message="Desired device is invalid")
    private String desireddevice;

    @Digits(fraction = 2, integer = 10, message = "Customer price is invalid")
    private String customerprice;

    @Size(min=3, max=10, message="Offer status is invalid")    // TBD
    private String offerstatus;

    @Pattern(regexp = "^[0-9]+$", message = "Seller is invalid") // TBD
    private String sellerid;

    @Pattern(regexp = "^[0-9]+$", message = "Customer is invalid") // TBD
    private String customerid;

    @Digits(fraction = 2, integer = 10, message = "Purchase price is invalid")
    private String purchaseprice;

    public OfferDTO() {}

    public OfferDTO(Offer offer) {
        this.id = String.valueOf(offer.getId());
        this.brandmodelid = String.valueOf(offer.getBrandmodel().getId());
        this.imei = offer.getImei();
        this.constractionyear = String.valueOf(offer.getConstructionYear());
        this.condition = offer.getCondition();
        this.color = offer.getColor();
        this.capacity = String.valueOf(offer.getCapacity());
        this.desireddevice = offer.getDesiredDevice();
        this.customerprice = String.valueOf(offer.getCustomerPrice());
        this.offerstatus = offer.getOfferStatus();
        this.sellerid = String.valueOf(offer.getAuthuser().getId());
        this.customerid = String.valueOf(offer.getCustomer().getId());
        this.purchaseprice = String.valueOf(offer.getPurchasePrice());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBrandmodelid() {
        return brandmodelid;
    }

    public void setBrandmodelid(String brandmodelid) {
        this.brandmodelid = brandmodelid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getConstractionyear() {
        return constractionyear;
    }

    public void setConstractionyear(String constractionyear) {
        this.constractionyear = constractionyear;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomerprice() {
        return customerprice;
    }

    public void setCustomerprice(String customerprice) {
        this.customerprice = customerprice;
    }

    public String getDesireddevice() {
        return desireddevice;
    }

    public void setDesireddevice(String desireddevice) {
        this.desireddevice = desireddevice;
    }

    public String getOfferstatus() {
        return offerstatus;
    }

    public void setOfferstatus(String offerstatus) {
        this.offerstatus = offerstatus;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(String purchaseprice) {
        this.purchaseprice = purchaseprice;
    }
}
