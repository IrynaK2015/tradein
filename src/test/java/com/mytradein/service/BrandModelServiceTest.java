package com.mytradein.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.mytradein.model.Brand;
import com.mytradein.model.Brandmodel;
import com.mytradein.repository.BrandRepository;
import com.mytradein.repository.BrandmodelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


public class BrandModelServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandmodelRepository brandmodelRepository;

    @InjectMocks
    private BrandModelService brandModelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBrand() {
        Brand brand = new Brand();
        brandModelService.addBrand(brand);
        verify(brandRepository).save(brand);
    }

    @Test
    public void testDeleteBrand() {
        Brand brand = new Brand();
        brandModelService.deleteBrand(brand);
        verify(brandRepository).delete(brand);
    }

    @Test
    public void testEnableBrand_AlreadyEnabled() {
        Brand brand = mock(Brand.class);
        when(brand.isEnabled()).thenReturn(true);

        brandModelService.enableBrand(brand);

        verify(brandRepository, never()).save(brand);
    }

    @Test
    public void testEnableBrand_NotEnabled() {
        Brand brand = mock(Brand.class);
        when(brand.isEnabled()).thenReturn(false);

        brandModelService.enableBrand(brand);

        verify(brand).setEnabled(true);
        verify(brandRepository).save(brand);
    }

    @Test
    public void testDisableBrand_AlreadyDisabled() {
        Brand brand = mock(Brand.class);
        when(brand.isEnabled()).thenReturn(false);

        brandModelService.disableBrand(brand);

        verify(brandRepository, never()).save(brand);
    }

    @Test
    public void testDisableBrand_Enabled() {
        Brand brand = mock(Brand.class);
        when(brand.isEnabled()).thenReturn(true);

        brandModelService.disableBrand(brand);

        verify(brand).setEnabled(false);
        verify(brandRepository).save(brand);
    }

    @Test
    public void testGetBrand() {
        Brand brand = new Brand();
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        Brand result = brandModelService.getBrand(1L);

        assertEquals(brand, result);
    }

    @Test
    public void testGetBrand_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        Brand result = brandModelService.getBrand(1L);

        assertNull(result);
    }

    @Test
    public void testFindBrandByName() {
        Brand brand = new Brand();
        when(brandRepository.findByBrandName("TestBrand")).thenReturn(brand);

        Brand result = brandModelService.findBrandByName("TestBrand");

        assertEquals(brand, result);
    }

    @Test
    public void testIsBrandsLoaded() {
        when(brandRepository.count()).thenReturn(3L);

        assertTrue(brandModelService.isBrandsLoaded());
    }

    @Test
    public void testIsBrandsLoaded_Empty() {
        when(brandRepository.count()).thenReturn(0L);

        assertFalse(brandModelService.isBrandsLoaded());
    }

    @Test
    public void testFindAllBrands() {
        List<Brand> brands = Arrays.asList(new Brand(), new Brand());
        when(brandRepository.findAll()).thenReturn(brands);

        List<Brand> result = brandModelService.findAllBrands();

        assertEquals(2, result.size());
    }

    @Test
    public void testAddModel() {
        Brandmodel model = new Brandmodel();
        brandModelService.addModel(model);
        verify(brandmodelRepository).save(model);
    }

    @Test
    public void testDeleteModel() {
        Brandmodel model = new Brandmodel();
        brandModelService.deleteModel(model);
        verify(brandmodelRepository).delete(model);
    }

    @Test
    public void testEnableModel() {
        Brandmodel model = new Brandmodel();
        brandModelService.enableModel(model);
        assertTrue(model.isEnabled());
        verify(brandmodelRepository).save(model);
    }

    @Test
    public void testDisableModel() {
        Brandmodel model = new Brandmodel();
        model.setEnabled(true);
        brandModelService.disableModel(model);
        assertFalse(model.isEnabled());
        verify(brandmodelRepository).save(model);
    }

    @Test
    public void testGetModel() {
        Brandmodel model = new Brandmodel();
        when(brandmodelRepository.findById(1L)).thenReturn(Optional.of(model));

        Brandmodel result = brandModelService.getModel(1L);

        assertEquals(model, result);
    }

    @Test
    public void testGetModel_NotFound() {
        when(brandmodelRepository.findById(1L)).thenReturn(Optional.empty());

        Brandmodel result = brandModelService.getModel(1L);

        assertNull(result);
    }

    @Test
    public void testFindModelByName() {
        Brandmodel model = new Brandmodel();
        when(brandmodelRepository.findByModelName("ModelX")).thenReturn(model);

        Brandmodel result = brandModelService.findModelByName("ModelX");

        assertEquals(model, result);
    }

    @Test
    public void testFindAllBrandmodels() {
        List<Brandmodel> models = Arrays.asList(new Brandmodel(), new Brandmodel());
        when(brandmodelRepository.findAll()).thenReturn(models);

        List<Brandmodel> result = brandModelService.findAllBrandmodels();

        assertEquals(2, result.size());
    }

    @Test
    public void testIsBrandmodelEnabledExist() {
        Brand brand = new Brand();
        when(brandmodelRepository.existsEnabledByBrand(brand)).thenReturn(true);

        assertTrue(brandModelService.isBrandmodelEnabledExist(brand));
    }

    @Test
    public void testIsModelsFoundInBrand() {
        Brand brand = new Brand();
        when(brandmodelRepository.existsByBrand(brand)).thenReturn(true);

        assertTrue(brandModelService.isModelsFoundInBrand(brand));
    }
}