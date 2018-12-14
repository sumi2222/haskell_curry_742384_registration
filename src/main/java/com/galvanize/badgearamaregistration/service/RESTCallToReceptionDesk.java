package com.galvanize.badgearamaregistration.service;

import com.galvanize.badgearamaregistration.entity.ExtendedPerson;
import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.exception.GuestNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RESTCallToReceptionDesk {
    private static final Logger LOGGER = LoggerFactory.getLogger(RESTCallToReceptionDesk.class);

    @Value("${reception.endpoint.person.checkout}")
    private String guestCheckoutURL;

    public String getVisitorCheckout(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        if (extendedPersonFrontEnd != null) {
            RestTemplate restTemplate = new RestTemplate();
            try {
              //  URI uri = new URI("http://localhost:8182/visit/visitor/checkout");
                String checkoutURL = guestCheckoutURL;
                restTemplate.put(checkoutURL, extendedPersonFrontEnd);
            } catch (Exception e) {
                String.format(" Rest call Exception :: %s ", e);
            }
            LOGGER.info(String.format(" %s :: you are checkedOut", extendedPersonFrontEnd.getPhoneNumber()));
            return extendedPersonFrontEnd.getPhoneNumber() + " :: you are checkedOut";
        }
        return extendedPersonFrontEnd.getPhoneNumber() + " :: is not found. ";
    }
}
