package com.mytradein.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Offer {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brandmodel_id")
    private Brandmodel brandmodel;

    @Column(length=20, nullable = false)
    private String imei;

    @Column(name = "construction_year", nullable = false)
    private int constructionYear;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false, length=20)
    private String color;

    @Column(nullable = false)
    private int capacity;

    @Column(name="desired_device", nullable = false)
    private String desiredDevice;

    @Column(name="customer_price", precision=13, scale=2, nullable = false)
    private BigDecimal customerPrice;

    @Column(name="purchase_price", precision=13, scale=2)
    //@Column(name="purchase_price", columnDefinition="Decimal(13,2) default '0.00'")
    private BigDecimal purchasePrice;

    @Column(name="offer_status", length=12, nullable = false)
    private String offerStatus;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private AuthUser authuser;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @CreationTimestamp
    private Date created;

    public Offer() {}

    public Offer(Brandmodel brandmodel, AuthUser authuser, Customer customer, String imei, int constructionYear,
                 String condition, String color, int capacity, String desiredDevice) {
        this.brandmodel = brandmodel;
        this.authuser = authuser;
        this.customer = customer;
        this.imei = imei;
        this.constructionYear = constructionYear;
        this.condition = condition;
        this.color = color;
        this.capacity = capacity;
        this.desiredDevice = desiredDevice;
        this.offerStatus = OfferStatuses.NEW.name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brandmodel getBrandmodel() {
        return brandmodel;
    }

    public void setBrandmodel(Brandmodel brandmodel) {
        this.brandmodel = brandmodel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public AuthUser getAuthuser() {
        return authuser;
    }

    public void setAuthuser(AuthUser authuser) {
        this.authuser = authuser;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDesiredDevice() {
        return desiredDevice;
    }

    public void setDesiredDevice(String desiredDevice) {
        this.desiredDevice = desiredDevice;
    }

    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getOfferStatus() {
        return offerStatus;
    }
    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }
}
