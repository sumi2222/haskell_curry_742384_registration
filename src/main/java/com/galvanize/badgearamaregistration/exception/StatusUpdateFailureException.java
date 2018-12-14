package com.galvanize.badgearamaregistration.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.PRECONDITION_FAILED)
public class StatusUpdateFailureException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestNotFoundException.class);
    public StatusUpdateFailureException(String phone){
        LOGGER.info(String.format("Status could not change or save for phone no. %s", phone));
    }
}
