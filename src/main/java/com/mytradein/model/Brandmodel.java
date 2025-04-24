package com.mytradein.model;

import jakarta.persistence.*;

@Entity
public class Brandmodel {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "model_name", nullable = false, length=60, unique=true)
    private String modelname;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;

    public Brandmodel() {}

    public Brandmodel(Brand brand, String modelname) {
        this.brand = brand;
        this.modelname = modelname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
