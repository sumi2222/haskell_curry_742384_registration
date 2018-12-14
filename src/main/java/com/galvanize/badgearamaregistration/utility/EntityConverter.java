package com.galvanize.badgearamaregistration.utility;

import com.galvanize.badgearamaregistration.entity.ExtendedPerson;
import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EntityConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityConverter.class);

    public ExtendedPerson transformFrontEndPersonToExtendedPerson(ExtendedPersonFrontEnd personFE) {
        LOGGER.info("**** ExtendedPersonFrontEnd :"+ personFE.toString());
        Long phone = stringToLongConversion(personFE.getPhoneNumber());
        return ExtendedPerson.builder()
                .phoneNumber(phone)
                .firstName(personFE.getFirstName())
                .lastName(personFE.getLastName())
                .company(personFE.getCompany())
                .hostName(personFE.getHostName())
                .hostPhoneNumber(stringToLongConversion(personFE.getHostPhone()))
                .purposeOfVisit(personFE.getPurposeOfVisit())
                .build();
    }

    public Person transformFrontEndPersonToPerson(ExtendedPersonFrontEnd personFE) {

        LOGGER.info("**** ExtendedPersonFrontEnd :"+ personFE.toString());
        Long phone = stringToLongConversion(personFE.getPhoneNumber());
        return Person.builder()
                .phoneNumber(phone)
                .firstName(personFE.getFirstName())
                .lastName(personFE.getLastName())
                .company(personFE.getCompany())
                .build();
    }

    public ExtendedPersonFrontEnd transformPersonToFrontEndPerson(Person person) {
        return ExtendedPersonFrontEnd.builder()
                .phoneNumber(convertLongToString(person.getPhoneNumber()))
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .company(person.getCompany()).build();
    }

    public ExtendedPerson transformPersonToExtendedPerson(Person person) {
        return ExtendedPerson.builder()
                .phoneNumber(person.getPhoneNumber())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .company(person.getCompany()).build();
    }

    private Long stringToLongConversion(String phoneNumber) {
        LOGGER.info("String phone number before {}", phoneNumber);
        try {
            return Long.parseLong(phoneNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private String convertLongToString(Long phoneNumber) {
        if (phoneNumber == null || phoneNumber.equals(0L))
            return null;
        LOGGER.info("String phone number after replace {}", phoneNumber.toString());
        return phoneNumber.toString();
    }
}
