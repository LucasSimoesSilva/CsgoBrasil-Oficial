package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.Movement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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


    @Test
    public void exTestPost() throws Exception {
        Movement movement = new Movement();
        movement.setEstadoVenda(true);
        movement.setPontos(10);
        movement.setIdComprador(1L);
        movement.setIdSkin(2L);
        movement.setIdVendedor(2L);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementJson.write(
                                        movement
                                ).getJson())
                )
                .andReturn().getResponse();

        movement.setIdVenda(42L);

        assertEquals(response.getContentAsString(),movementJson.write(movement).getJson());
    }

    @Test
    public void exTestGet() throws Exception {
        Movement movement = new Movement();

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();
    }
}
