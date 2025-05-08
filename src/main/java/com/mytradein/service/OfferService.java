package com.mytradein.service;

import com.mytradein.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface OfferService {
    public List<Offer> findBySellerAndStatus(String status, AuthUser authUser, Pageable pageable);

    public Page<Offer> searchOffers(Map<String, Object> searchMap, Pageable pageable);

    @Transactional(readOnly=true)
    public Offer findById(Long id);

    @Transactional(readOnly=true)
    public Offer findByImei(String imei);

    @Transactional
    public void save(Offer offer);

    @Transactional
    public void delete(Offer offer);

    @Transactional(readOnly=true)
    public long countSellerOffers(AuthUser authUser);

    @Transactional(readOnly=true)
    public long countCustomerOffers(Customer customer);

    @Transactional(readOnly=true)
    public boolean isModelOffersFound(Brandmodel brandmodel);

    public Map getNextOfferStatuses(String currentOfferStatus);
}
