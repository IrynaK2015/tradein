package com.mytradein.repository;

import com.mytradein.model.Brand;
import com.mytradein.model.Brandmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandmodelRepository extends JpaRepository<Brandmodel, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Brandmodel m WHERE m.brand = :brand AND m.enabled = true")
    boolean existsEnabledByBrand(@Param("brand") Brand brand);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Brandmodel m WHERE m.brand = :brand")
    boolean existsByBrand(@Param("brand") Brand brand);

    @Query("SELECT m FROM Brandmodel m WHERE m.modelname = :modelname")
    Brandmodel findByModelName(@Param("modelname") String modelname);
}
