package com.mytradein.repository;

import com.mytradein.model.AuthUser;
import com.mytradein.model.Brandmodel;
import com.mytradein.model.Customer;
import com.mytradein.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    @Query("SELECT o FROM Offer o WHERE o.offerStatus IN :statuses")
    public List<Offer> findNotCompleted(List<String> statuses);

    @Query("SELECT o FROM Offer o WHERE o.offerStatus = :offerStatus AND o.authuser = :authuser")
    public List<Offer> findBySellerAndStatus(@Param("offerStatus") String offerStatus, @Param("authuser") AuthUser authuser);

    @Query("SELECT o FROM Offer o WHERE o.authuser = :authuser")
    public List<Offer> findBySeller(@Param("authUser") AuthUser authUser);

    @Query("SELECT o FROM Offer o WHERE o.offerStatus = :status ORDER BY o.created ASC")
    public List<Offer> findByStatusOrderByCreatedAsc(@Param("status") String status);

    @Query("SELECT o FROM Offer o WHERE o.imei = :imei")
    public Offer findByImei(@Param("imei") String imei);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Offer o WHERE o.brandmodel = :brandmodel")
    boolean existsByBrandmodel(@Param("brandmodel") Brandmodel brandmodel);

    long countOffersByAuthuser(AuthUser authuser);

    long countOffersByCustomer(Customer customer);
}
