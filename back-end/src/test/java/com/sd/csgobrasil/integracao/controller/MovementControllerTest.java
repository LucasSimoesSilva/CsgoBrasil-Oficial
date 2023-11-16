package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.Movement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class MovementControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Movement> movementJson;

    @Autowired
    private JacksonTester<List<Movement>> movementListJson;


    @Test
    @DisplayName("method addMovement")
    public void returnMovementWhenMovementIsValid() throws Exception {
        Movement movement = new Movement(42L,2L,1L,2L,true,10);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementJson.write(
                                        movement
                                ).getJson())
                )
                .andReturn().getResponse();

        assertEquals(response.getContentAsString(),movementJson.write(movement).getJson());
        assertThat(response.getContentAsString()).isNotEmpty();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("method listMovement")
    public void returnAllMovementsWhenDatabaseIsNotEmpty() throws Exception {
        int sizeOfListMovements = 41;

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        assertThat(response.getContentAsString()).isNotEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(sizeOfListMovements, movementListJson.parse(response.getContentAsString()).getObject().size());
    }

    @Test
    @DisplayName("method cancelMovement")
    public void cancelMovementWhenIdIsValidAndWhenIdIsInvalid() throws Exception {
        Long idValidOrInvalid = 1L;

        MockHttpServletResponse response = mvc.
                perform(delete("/movement/{idVenda}",idValidOrInvalid)).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

}
