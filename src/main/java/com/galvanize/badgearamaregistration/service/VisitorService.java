package com.galvanize.badgearamaregistration.service;

import com.galvanize.badgearamaregistration.entity.ExtendedPerson;
import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.entity.Person;
import com.galvanize.badgearamaregistration.exception.GuestNotFoundException;
import com.galvanize.badgearamaregistration.exception.StatusUpdateFailureException;
import com.galvanize.badgearamaregistration.repository.VisitorRepository;
import com.galvanize.badgearamaregistration.utility.EntityConverter;
import com.galvanize.badgearamaregistration.utility.VisitorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorService.class);

    @Autowired
    VisitorRepository personRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    EntityConverter entityConverter;
    @Autowired
    RESTCallToReceptionDesk rESTCallToReceptionDesk;

    @Value("${amqp.exchange.name}")
    String appExchangeName;

    @Value("${amqp.routing.key}")
    String appRoutingKey;

    // New Visitor
    @Transactional
    public String register(ExtendedPersonFrontEnd personFrontEnd) {
        String status = "true";
        try {
            LOGGER.info("**** personFE :" + personFrontEnd.toString());
            ExtendedPerson extendedPerson = entityConverter.transformFrontEndPersonToExtendedPerson(personFrontEnd);
            updateOrSavePersonTable(extendedPerson);
            extendedPerson.setStatus(VisitorStatus.UNVERIFIED);
            sendMessage(appExchangeName, appRoutingKey, extendedPerson);
            return status;
        } catch (Exception e) {
            return null;
        }
    }

    private void updateOrSavePersonTable(ExtendedPerson extendedPerson) {
        personRepository.save(Person.builder()
                .phoneNumber(extendedPerson.getPhoneNumber())
                .firstName(extendedPerson.getFirstName())
                .lastName(extendedPerson.getLastName())
                .company(extendedPerson.getCompany())
                .build());
    }

    // Returning Visitor : get visitor by phonenumber
    // ?? Not sure how to return - HTTPStatus --> 404, if not successful

    @Transactional
    public ExtendedPersonFrontEnd getPersonByPhone(String phoneNumber) {
        try {
            Long phone = stringToLongConversion(phoneNumber);
            if (!personRepository.existsById(phone)) {
                throw new GuestNotFoundException(phoneNumber);
            } else {
                ExtendedPersonFrontEnd transformedPerson = entityConverter.transformPersonToFrontEndPerson(personRepository.findById(phone).get());
                return transformedPerson;
            }
        } catch (Exception e) {
            String.format("Phone number %s doesnot exists", phoneNumber);
            return null;
        }
    }

    public List<ExtendedPersonFrontEnd> getAllPerson() {
        List<Person> persons = (List<Person>) personRepository.findAll();
        List<ExtendedPersonFrontEnd> result = new ArrayList<>();
        LOGGER.info(" ************ visits :" + persons.toString());
        persons.forEach(person -> {
            result.add(entityConverter.transformPersonToFrontEndPerson(person));
        });
        return result;
    }

    @Transactional
    public String updatePersonTable(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        try {
            String phoneNumber = extendedPersonFrontEnd.getPhoneNumber();
            Long phone = stringToLongConversion(phoneNumber);
            if (!personRepository.existsById(phone)) {
                throw new GuestNotFoundException(extendedPersonFrontEnd.getPhoneNumber());
            } else {
                Person person = entityConverter.transformFrontEndPersonToPerson(extendedPersonFrontEnd);
                personRepository.save(person);
                LOGGER.info("Person table updated " + person.toString());
                return String.format("%s :: information updated ", extendedPersonFrontEnd.getPhoneNumber());
            }
        } catch (StatusUpdateFailureException ex) {
            return null;      // As per Ray's front end wants to consume.
        }
    }

    @Transactional
    public String deletePersonTable(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        try {
            String phoneNumber = extendedPersonFrontEnd.getPhoneNumber();
            Long phone = stringToLongConversion(phoneNumber);
            if (!personRepository.existsById(phone)) {
                throw new GuestNotFoundException(extendedPersonFrontEnd.getPhoneNumber());
            } else {
                personRepository.deleteByPhoneNumber(phone);
                LOGGER.info(String.format("%s :: deleted ", extendedPersonFrontEnd.getPhoneNumber()));
                return String.format("%s :: deleted ", extendedPersonFrontEnd.getPhoneNumber());
            }
        } catch (StatusUpdateFailureException ex) {
            LOGGER.info(String.format("Could not delete :: Exception  :: %s ", ex));     // As per Ray's front end wants to consume.
            return null;
        }
    }


    @Transactional
    public String visitorCheckoutFromVisitTable_REST_CALL(ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = rESTCallToReceptionDesk.getVisitorCheckout(extendedPersonFrontEnd);
        return response;
    }

    public void sendMessage(String exchange, String routingKey, Object data) {
        LOGGER.info("Sending message to the queue using routingKey {}. Message= {}", routingKey, data);
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
        LOGGER.info("The message has been sent to the queue.");
    }

    private Long stringToLongConversion(String phoneNumber) {
        LOGGER.info("String phone number before {}", phoneNumber);
        try {
            return Long.parseLong(phoneNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }

    }
}
