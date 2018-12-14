package com.galvanize.badgearamaregistration.service;

import com.galvanize.badgearamaregistration.entity.ExtendedPerson;
import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.entity.Person;
import com.galvanize.badgearamaregistration.repository.VisitorRepository;
import com.galvanize.badgearamaregistration.utility.EntityConverter;
import com.galvanize.badgearamaregistration.utility.VisitorStatus;

import com.sun.media.jfxmedia.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisitorServiceTest {
    @Mock
    VisitorRepository personRepository;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    EntityConverter entityConverter;
    @Mock
    RESTCallToReceptionDesk rESTCallToReceptionDesk;

    @InjectMocks
    VisitorService visitorService;


    @Mock
    ExtendedPerson mockExtendedPerson;

    void initMocks(){}

    @Test
    public void register_Test() throws Exception{

        //doNothing().when(Logger.INFO);
        Person person = Person.builder().phoneNumber(5551112233L).build();
        ExtendedPersonFrontEnd mockVisitorFrontEnd= ExtendedPersonFrontEnd.builder().phoneNumber("(555)111-2233").build();
        ExtendedPerson mockExtendedPerson= ExtendedPerson.builder().phoneNumber(5551112233L).build();
        when(entityConverter.transformFrontEndPersonToExtendedPerson(mockVisitorFrontEnd)).thenReturn(mockExtendedPerson);
        when(personRepository.save(person)).thenReturn(person);
        visitorService.register(mockVisitorFrontEnd);
        verify(personRepository, times(1)).save(person);
       // verify(visitorService, times(1))
                      // .sendMessage(visitorService.appExchangeName, visitorService.appRoutingKey, mockExtendedPerson);
    }

    @Test
    public void getPersonByPhone_Test() throws Exception{
        Person person = Person.builder().phoneNumber(2346786789L).build();
        Long phone = 2346786789L;
        when(personRepository.existsById(phone)).thenReturn(true);
        when(personRepository.findById(phone)).thenReturn(java.util.Optional.ofNullable(person));
        visitorService.getPersonByPhone("(234)-678-6789");
        verify(personRepository, times(1)).existsById(phone);
        verify(personRepository, times(1)).findById(phone);
    }

    @Test
    public void getPersonByPhoneNotFound_Test() throws Exception{
        Long phone = 2346786789L;
        when(personRepository.existsById(phone)).thenReturn(false);
        visitorService.getPersonByPhone("(234)-678-6789");
        verify(personRepository, times(1)).existsById(phone);
        verify(personRepository, times(0)).findById(phone);
    }

    @Test
    public void getAllPerson_Test() throws Exception{
        Person person1 = Person.builder().phoneNumber(1116786789L).firstName("Bobb1").lastName("King1").company("GE").build();
        Person person2 = Person.builder().phoneNumber(2226756467L).firstName("Bobb2").lastName("King2").company("GE").build();
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        when(personRepository.findAll()).thenReturn(personList);
        List<ExtendedPersonFrontEnd> actualList = visitorService.getAllPerson();
        verify(personRepository, times(1)).findAll();
        assertEquals(actualList.size(), personList.size());
    }

    @Test
    public void getAllPersonWhenReturnsNull_Test() throws Exception{
        List<Person> personList = new ArrayList<>();
        when(personRepository.findAll()).thenReturn(personList);
        List<ExtendedPersonFrontEnd> actualList = visitorService.getAllPerson();
        verify(personRepository, times(1)).findAll();
        assertEquals(actualList.size(), personList.size());
        assertEquals(actualList.size(),0);
        assertEquals(personList.size(), 0);
    }
}
