package com.galvanize.badgearamaregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.badgearamaregistration.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamaregistration.service.VisitorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
//@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@WebMvcTest(value = VisitorController.class, secure = false)
public class VisitorControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitorService visitService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void register_Test() throws Exception {
        ExtendedPersonFrontEnd person = ExtendedPersonFrontEnd.builder().build();
        String phoneNumber = "1234567890";
        person.setPhoneNumber("222-333-4444");
        when(visitService.register(person)).thenReturn(anyString());
        //LOGGER.info("perform is: {}", perform);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/visitor/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(person))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).register(person);
    }

    @Test
    public void registerWhenStringReturnsFalse_Test() throws Exception {
        ExtendedPersonFrontEnd person = ExtendedPersonFrontEnd.builder().build();
        String phoneNumber = "1234567890";
        person.setPhoneNumber("222-333-4444");
        when(visitService.register(person)).thenReturn(null);
        //LOGGER.info("perform is: {}", perform);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/visitor/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(person))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).register(person);
    }

    @Test
    public void getPersonByPhone_Test() throws Exception {
        ExtendedPersonFrontEnd person = ExtendedPersonFrontEnd.builder().build();
        String phoneNumber = "222-333-4444";
        person.setPhoneNumber(phoneNumber);
        when(visitService.getPersonByPhone(phoneNumber)).thenReturn(person);
        //LOGGER.info("perform is: {}", perform);
       // ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup/{phoneNumber}", phoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).getPersonByPhone(phoneNumber);
    }

    @Test
    public void getPersonByPhoneIsNull_Test() throws Exception {
        ExtendedPersonFrontEnd person = null;
        String phoneNumber = "713-333-2828";
        when(visitService.getPersonByPhone(phoneNumber)).thenReturn(person);
        //LOGGER.info("perform is: {}", perform);
        // ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup/{phoneNumber}", phoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).getPersonByPhone(phoneNumber);
    }

    @Test
    public void getAllPersonTest() throws Exception {
        ExtendedPersonFrontEnd person1 = ExtendedPersonFrontEnd.builder().build();
        ExtendedPersonFrontEnd person2 = ExtendedPersonFrontEnd.builder().build();
        List<ExtendedPersonFrontEnd>  personList = new ArrayList<>();
        String phoneNumber = "222-333-4444";
        person1.setPhoneNumber(phoneNumber);
        personList.add(person1);
        personList.add(person2);

        when(visitService.getAllPerson()).thenReturn(personList);
        //LOGGER.info("perform is: {}", perform);
        // ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).getAllPerson();
    }

    @Test
    public void getAllPersonWhenListIsNullTest() throws Exception {
        List<ExtendedPersonFrontEnd>  personList = null;
        when(visitService.getAllPerson()).thenReturn(personList);

        //LOGGER.info("perform is: {}", perform);
        // ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).getAllPerson();
    }





   /*   @Test
    public void testGetMethodWithWrongPhoneNumber() throws Exception  {
            String wrongNumber = "1234567890";

            when(visitService.getPersonByPhone(wrongNumber)).thenThrow(GuestNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders
                       .get("/visitor/lookup/{phoneNumber}", wrongNumber)
                       .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andReturn();
            verify(visitService, times(1)).getPersonByPhone(wrongNumber);
        }*/

}



