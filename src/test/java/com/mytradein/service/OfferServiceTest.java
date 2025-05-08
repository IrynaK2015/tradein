package com.mytradein.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.mytradein.model.AuthUser;
import com.mytradein.model.Brandmodel;
import com.mytradein.model.Customer;
import com.mytradein.model.Offer;
import com.mytradein.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBySellerAndStatus() {
        AuthUser user = new AuthUser();
        List<Offer> offers = List.of(new Offer(), new Offer());

        when(offerRepository.findBySellerAndStatus("NEW", user)).thenReturn(offers);

        List<Offer> result = offerService.findBySellerAndStatus("NEW", user, Pageable.unpaged());

        assertEquals(2, result.size());
    }

    @Test
    void testSearchOffersWithoutPagination() {
        Customer customer = new Customer();
        AuthUser seller = new AuthUser();
        String status = "APPROVED";

        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("customerid", customer);
        searchMap.put("sellerid", seller);
        searchMap.put("status", status);

        List<Offer> expected = List.of(new Offer());

        //when(offerRepository.findAll(any(Specification.class))).thenReturn(expected);

        //List<Offer> result = offerService.searchOffers(searchMap);

        //assertEquals(1, result.size());
    }

    @Test
    void testSearchOffersWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("status", "NEW");

        Page<Offer> mockPage = new PageImpl<>(List.of(new Offer()));
       // when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

       // Page<Offer> result = offerService.searchOffers(searchMap, pageable);

      //  assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindById() {
        Offer offer = new Offer();
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        Offer result = offerService.findById(1L);
        assertEquals(offer, result);
    }

    @Test
    void testFindByIdNotFound() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        Offer result = offerService.findById(1L);
        assertNull(result);
    }

    @Test
    void testFindByImei() {
        Offer offer = new Offer();
        when(offerRepository.findByImei("123456")).thenReturn(offer);

        Offer result = offerService.findByImei("123456");
        assertEquals(offer, result);
    }

    @Test
    void testSave() {
        Offer offer = new Offer();
        offerService.save(offer);
        verify(offerRepository).save(offer);
    }

    @Test
    void testDelete() {
        Offer offer = new Offer();
        offerService.delete(offer);
        verify(offerRepository).delete(offer);
    }

    @Test
    void testCountSellerOffers() {
        AuthUser user = new AuthUser();
        when(offerRepository.countOffersByAuthuser(user)).thenReturn(5L);

        long result = offerService.countSellerOffers(user);
        assertEquals(5L, result);
    }

    @Test
    void testCountCustomerOffers() {
        Customer customer = new Customer();
        when(offerRepository.countOffersByCustomer(customer)).thenReturn(3L);

        long result = offerService.countCustomerOffers(customer);
        assertEquals(3L, result);
    }

    @Test
    void testIsModelOffersFound() {
        Brandmodel brandmodel = new Brandmodel();
        when(offerRepository.existsByBrandmodel(brandmodel)).thenReturn(true);

        assertTrue(offerService.isModelOffersFound(brandmodel));
    }

    @Test
    void testGetNextOfferStatuses_New() {
        Map result = offerService.getNextOfferStatuses("NEW");
        assertTrue(result.containsKey("PROCESSING"));
    }

    @Test
    void testGetNextOfferStatuses_Processing() {
        Map result = offerService.getNextOfferStatuses("PROCESSING");
        assertTrue(result.containsKey("APPROVED"));
        assertTrue(result.containsKey("REJECTED"));
    }

    @Test
    void testGetNextOfferStatuses_Approved() {
        Map result = offerService.getNextOfferStatuses("APPROVED");
        assertTrue(result.containsKey("REJECTED"));
        assertTrue(result.containsKey("PURCHASED"));
    }
}
