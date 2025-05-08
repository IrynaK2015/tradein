package com.mytradein.service;

import com.mytradein.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface OfferService {
    List<Offer> findBySellerAndStatus(String status, AuthUser authUser, Pageable pageable);

    Page<Offer> searchOffers(Map<String, Object> searchMap, Pageable pageable);

    Offer findById(Long id);

    Offer findByImei(String imei);

    void save(Offer offer);

    void delete(Offer offer);

    long countSellerOffers(AuthUser authUser);

    long countCustomerOffers(Customer customer);

    boolean isModelOffersFound(Brandmodel brandmodel);

    Map getNextOfferStatuses(String currentOfferStatus);
}
