package com.mytradein.controller;

import com.mytradein.dto.*;
import com.mytradein.exception.ConflictException;
import com.mytradein.exception.NotFoundException;
import com.mytradein.exception.UnauthorizedException;
import com.mytradein.model.Authtoken;
import com.mytradein.model.Offer;
import com.mytradein.model.OfferStatuses;
import com.mytradein.service.AuthtokenService;
import com.mytradein.service.Constants;
import com.mytradein.service.OfferService;
import com.mytradein.service.Utilities;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final OfferService offerService;

    private final AuthtokenService authtokenService;

    public ApiController(OfferService offerService,
                         AuthtokenService authtokenService) {
        this.offerService = offerService;
        this.authtokenService = authtokenService;
    }

    @PostMapping("/offer/auth")
    public AuthTokenResponse getAuthToken(@RequestBody AuthTokenRequest authToken) {
        String authLogin = authToken.getLogin();
        String authPassword = authToken.getPassword();
        if (authLogin.isEmpty() || authPassword.isEmpty()) {
            throw new UnauthorizedException("Invalid login or password");
        }
        if (!authLogin.equals(Constants.API_LOGIN)) {
            throw new UnauthorizedException("Wrong login or password");
        }

        Authtoken apiTokenConfig = authtokenService.getApiTokenCredentials(authLogin, Utilities.encodeSha256(authPassword));
        if (Objects.isNull(apiTokenConfig)) {
            throw new UnauthorizedException("Wrong login or password");
        }
        Authtoken currentToken = authtokenService.getActiveAuthToken(authLogin);
        if (Objects.isNull(currentToken)) {
            currentToken = authtokenService.createAuthToken(authLogin);
        }

        return new AuthTokenResponse(currentToken);
    }


    @GetMapping("/offer/{id}")
    public OfferResponse getOffer(@PathVariable("id") Long id,
                                  @RequestHeader(value=HttpHeaders.AUTHORIZATION, required = true) String authToken) {
        if (authToken.isEmpty()) {
            throw new UnauthorizedException("Invalid token");
        }
        Authtoken currentToken = authtokenService.getActiveTokenByValue(authToken);
        if (Objects.isNull(currentToken)) {
            throw new UnauthorizedException("Token is incorrect or expired");
        }
        if (id < 0) id = 0L;
        Offer offer = offerService.findById(id);
        if (Objects.isNull(offer)) {
            throw new NotFoundException("offer");
        }

        OfferResponse response = new OfferResponse(offer);
        response.setToken(currentToken.getTokenValue());

        return response;
    }


    @PutMapping("/offer/{id}/status")
    public OfferStatusResponse changeOfferStatus(@PathVariable("id") Long id,
                                                 @RequestBody OfferStatusRequest offerStatusRequest,
                                                 @RequestHeader(value=HttpHeaders.AUTHORIZATION, required = true) String authToken) {
        if (authToken.isEmpty()) {
            throw new UnauthorizedException("Invalid token");
        }
        Authtoken currentToken = authtokenService.getActiveTokenByValue(authToken);
        if (Objects.isNull(currentToken)) {
            throw new UnauthorizedException("Token is incorrect or expired");
        }

        if (id < 0) id = 0L;
        Offer offer = offerService.findById(id);
        if (Objects.isNull(offer)) {
            throw new NotFoundException("offer");
        }
        if (!offer.getOfferStatus().equalsIgnoreCase(OfferStatuses.APPROVED.name())) {
            throw new ConflictException("Status can't be changed");
        }
        String newStatus = offerStatusRequest.getOfferStatus();
        Map nextStatuses = offerService.getNextOfferStatuses(offer.getOfferStatus());
        if (!nextStatuses.containsKey(newStatus)) {
            throw new ConflictException("New status is not allowed");
        }

        offer.setOfferStatus(newStatus);
        offerService.save(offer);

        return new OfferStatusResponse(offer);
    }
}
