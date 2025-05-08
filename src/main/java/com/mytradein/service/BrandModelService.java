package com.mytradein.service;

import com.mytradein.model.Brand;
import com.mytradein.model.Brandmodel;

import java.util.List;

public interface BrandModelService {

    void addBrand(Brand brand);

    void deleteBrand(Brand brand);

    void addModel(Brandmodel brandmodel);

    void deleteModel(Brandmodel brandmodel);

    void enableBrand(Brand brand);

    void disableBrand(Brand brand);

    void enableModel(Brandmodel brandmodel);

    void disableModel(Brandmodel brandmodel);

    Brand getBrand(Long brandId);

    Brand findBrandByName(String name);

    boolean isBrandmodelEnabledExist(Brand brand);

    boolean isBrandsLoaded();

    boolean isModelsFoundInBrand(Brand brand);

    Brandmodel getModel(Long modelId);

    Brandmodel findModelByName(String name);

    List<Brand> findAllBrands();

    List<Brandmodel> findAllBrandmodels();
}
