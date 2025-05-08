package com.mytradein.service;

import com.mytradein.model.*;
import com.mytradein.repository.OfferRepository;
import com.mytradein.repository.OfferSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> findBySellerAndStatus(String status, AuthUser authUser, Pageable pageable) {
        return offerRepository.findBySellerAndStatus(status, authUser);
    }

    public Page<Offer> searchOffers(Map<String, Object> searchMap, Pageable pageable) {
        Specification<Offer> specification = Specification.where(null);

        if (searchMap.containsKey("customerid")) {
            specification = specification.and(OfferSpecification.hasCustomer((Customer)searchMap.get("customerid")));
        }
        if (searchMap.containsKey("sellerid")) {
            specification = specification.and(OfferSpecification.hasSeller((AuthUser)searchMap.get("sellerid")));
        }
        if (searchMap.containsKey("status")) {
            specification = specification.and(OfferSpecification.hasStatus((String)searchMap.get("status")));
        }

        return offerRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly=true)
    public Offer findById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    public Offer findByImei(String imei) {
        return offerRepository.findByImei(imei);
    }

    @Transactional
    public void save(Offer offer) {
        offerRepository.save(offer);
    }

    @Transactional
    public void delete(Offer offer) {
        offerRepository.delete(offer);
    }

    @Transactional(readOnly=true)
    public long countSellerOffers(AuthUser authUser) {
        return offerRepository.countOffersByAuthuser(authUser);
    }

    @Transactional(readOnly=true)
    public long countCustomerOffers(Customer customer) {
        return offerRepository.countOffersByCustomer(customer);
    }

    @Transactional(readOnly=true)
    public boolean isModelOffersFound(Brandmodel brandmodel) {
        return offerRepository.existsByBrandmodel(brandmodel);
    }

    public Map getNextOfferStatuses(String currentOfferStatus) {
        Map<String, String> offerMap = new HashMap<>();
        if (currentOfferStatus.equals(OfferStatuses.NEW.name())) {
            offerMap.put(OfferStatuses.PROCESSING.name(), OfferStatuses.PROCESSING.getLabel());
        } else if (currentOfferStatus.equals(OfferStatuses.PROCESSING.name())) {
            offerMap.put(OfferStatuses.APPROVED.name(), OfferStatuses.APPROVED.getLabel());
            offerMap.put(OfferStatuses.REJECTED.name(), OfferStatuses.REJECTED.getLabel());
        } else if (currentOfferStatus.equals(OfferStatuses.APPROVED.name())) {
            offerMap.put(OfferStatuses.REJECTED.name(), OfferStatuses.REJECTED.getLabel());
            offerMap.put(OfferStatuses.PURCHASED.name(), OfferStatuses.PURCHASED.getLabel());
        }

        return offerMap;
    }
}
