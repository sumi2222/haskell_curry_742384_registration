package com.galvanize.badgearamaregistration.exception;

import antlr.StringUtils;
import com.galvanize.badgearamaregistration.controller.VisitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GuestNotFoundException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestNotFoundException.class);
    public GuestNotFoundException(String phoneNumber) {
        LOGGER.info(String.format("Phone number %s doesnot exits",phoneNumber));
    }
}
