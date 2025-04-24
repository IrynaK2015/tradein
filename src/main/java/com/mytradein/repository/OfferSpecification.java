package com.mytradein.repository;

import com.mytradein.model.AuthUser;
import com.mytradein.model.Customer;
import com.mytradein.model.Offer;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification {
    public static Specification<Offer> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("offerStatus"), status);
    }

    public static Specification<Offer> hasSeller(AuthUser seller) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("authuser"), seller);
    }

    public static Specification<Offer> hasCustomer(Customer customer) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.equal(root.get("customer"), customer);
    }
}
