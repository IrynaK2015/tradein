package com.mytradein.service;

import com.mytradein.model.Brand;
import com.mytradein.model.Brandmodel;
import com.mytradein.repository.BrandRepository;
import com.mytradein.repository.BrandmodelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandModelServiceImpl implements BrandModelService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandmodelRepository brandmodelRepository;

    @Transactional
    public void addBrand(Brand brand) {
        brandRepository.save(brand);
    }

    @Transactional
    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    @Transactional
    public void addModel(Brandmodel brandmodel) {
        brandmodelRepository.save(brandmodel);
    }

    @Transactional
    public void deleteModel(Brandmodel brandmodel) {
        brandmodelRepository.delete(brandmodel);
    }

    @Transactional
    public void enableBrand(Brand brand) {
        if (!brand.isEnabled()) {
            brand.setEnabled(true);
            brandRepository.save(brand);
        }
    }

    @Transactional
    public void disableBrand(Brand brand) {
        if (brand.isEnabled()) {
            brand.setEnabled(false);
            brandRepository.save(brand);
        }
    }

    @Transactional
    public void enableModel(Brandmodel brandmodel) {
        brandmodel.setEnabled(true);
        brandmodelRepository.save(brandmodel);
    }

    @Transactional
    public void disableModel(Brandmodel brandmodel) {
        brandmodel.setEnabled(false);
        brandmodelRepository.save(brandmodel);
    }

    @Transactional(readOnly=true)
    public Brand getBrand(Long brandId) {
        return brandRepository.findById(brandId).orElse(null);
    }

    @Transactional(readOnly=true)
    public Brand findBrandByName(String name) {
        return brandRepository.findByBrandName(name);
    }

    @Transactional(readOnly=true)
    public boolean isBrandmodelEnabledExist(Brand brand) {
        return brandmodelRepository.existsEnabledByBrand(brand);
    }

    @Transactional(readOnly=true)
    public boolean isBrandsLoaded() {
        return brandRepository.count() > 0;
    }

    @Transactional(readOnly=true)
    public boolean isModelsFoundInBrand(Brand brand) {
        return brandmodelRepository.existsByBrand(brand);
    }

    @Transactional(readOnly=true)
    public Brandmodel getModel(Long modelId) {
        return brandmodelRepository.findById(modelId).orElse(null);
    }

    @Transactional(readOnly=true)
    public Brandmodel findModelByName(String name) {
        return brandmodelRepository.findByModelName(name);
    }

    @Transactional(readOnly=true)
    public List<Brand> findAllBrands() {
        return brandRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<Brandmodel> findAllBrandmodels() {
        return brandmodelRepository.findAll();
    }
}
