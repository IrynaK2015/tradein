package com.mytradein.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Brand {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "brand_name", nullable = false, length=20, unique=true)
    private String brandname;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    final private List<Brandmodel> brandmodels = new ArrayList<>();

    public Brand() {}

    public Brand(String brandname) {
        this.brandname = brandname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addBrandmodel(Brandmodel brandmodel) {
        this.brandmodels.add(brandmodel);
    }

    public List<Brandmodel> getBrandmodels() {
        return brandmodels;
    }

    public Brandmodel getBrandmodel(String modelname) {
        for (Brandmodel brandmodel : brandmodels) {
            if (modelname.equalsIgnoreCase(brandmodel.getModelname())) {
                return brandmodel;
            }
        }
        throw new IllegalArgumentException("Model " + modelname + " not found");
    }
}
