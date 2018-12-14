package com.galvanize.badgearamaregistration.controller;

import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visitor")
//@ControllerAdvice
public class VisitorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorController.class);
    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody ExtendedPersonFrontEnd person) {
        String response = visitorService.register(person);
        if (response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/lookup/{phoneNumber}")
    public ResponseEntity<ExtendedPersonFrontEnd> getPersonByPhone(@PathVariable String phoneNumber) {
        ExtendedPersonFrontEnd exPerFrontEnd = visitorService.getPersonByPhone(phoneNumber);
        if (exPerFrontEnd != null)
            return ResponseEntity.ok().body(exPerFrontEnd);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<ExtendedPersonFrontEnd>> getPersonByPhone() {
        List<ExtendedPersonFrontEnd> frontEndPersonList = visitorService.getAllPerson();
        if (frontEndPersonList != null)
            return ResponseEntity.ok().body(frontEndPersonList);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> visitorUpdate(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitorService.updatePersonTable(extendedPersonFrontEnd);
        if (response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }

   /* @PutMapping("/delete")
    public ResponseEntity<String> visitorDelete(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitorService.deletePersonTable(extendedPersonFrontEnd);
        if (response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }*/

    //************************** REST Call To Reception ***************************

    @PutMapping("/checkout")
    public ResponseEntity<String> visitorCheckout(@RequestBody ExtendedPersonFrontEnd extendedPersonFrontEnd) {
        String response = visitorService.visitorCheckoutFromVisitTable_REST_CALL(extendedPersonFrontEnd);
        if (response != null)
            return ResponseEntity.ok().body(response);
        else return ResponseEntity.notFound().build();
    }
}
