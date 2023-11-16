package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class MovementControllerTest {

    @MockBean
    private MovementService service;

    @MockBean
    private SkinService skinService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<Movement>> movementListJson;
    @Autowired
    private JacksonTester<Movement> movementJson;
    @Autowired
    private JacksonTester<List<SkinMovement>> skinMovementListJson;


    @Test
    void getAllMovementsWhenHaveMovements() throws Exception {
        List<Movement> movementList = getMovementList();

        when(service.listMovement()).thenReturn(movementList);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertIterableEquals(movementList,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @Test
    @DisplayName("method listMovement")
    void getEmptyListWhenHaveNotMovements() throws Exception {
        when(service.listMovement()).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("method listMovementSkin")
    void getASkinMovementList() throws Exception {
        List<Movement> movements = new ArrayList<>();
        Movement movement = new Movement(1L, 1L, 2L, 3L, true, 123);
        Movement movement1 = new Movement(2L, 1L, 2L, 4L, true, 234);
        movements.add(movement);
        movements.add(movement1);
        when(service.listMovement()).thenReturn(movements);

        List<SkinMovement> skinMovements = new ArrayList<>();
        skinMovements.add(new SkinMovement(1L, 1L , true, "dragon",
                "AWP", 10, "guerra", ""));
        skinMovements.add(new SkinMovement(2L, 1L , true, "eagle",
                "Pistola", 10, "guerra", ""));
        when(skinService.getSkinMovements(movements)).thenReturn(skinMovements);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/skinMovements")).andReturn().getResponse();

        List<SkinMovement> responseObject = skinMovementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertIterableEquals(skinMovements,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }
    @Test
    @DisplayName("method listMovementSkin")
    void getAEmptySkinMovementList() throws Exception {
        when(service.listMovement()).thenReturn(new ArrayList<>());
        when(skinService.getSkinMovements(new ArrayList<>())).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/skinMovements")).andReturn().getResponse();

        List<SkinMovement> responseObject = skinMovementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("method addMovement")
    void addValidMovement() throws Exception {
        Movement movement = new Movement(1L, 1L, 2L, 3L, true, 123);

        when(service.addMovement(movement)).thenReturn(movement);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementJson.write(movement).getJson())
                )
                .andReturn().getResponse();

        Movement responseObject = movementJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(movement,responseObject);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("method addMovement")
    void addInvalidMovement() throws Exception {
        when(service.addMovement(null)).thenReturn(null);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/movement")
                )
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("method findByMovementId")
    void returnMovementWhenIdIsValid() throws Exception {
        Movement movement = new Movement(1L, 1L, 2L, 3L, true, 123);
        when(service.findByMovementId(1L)).thenReturn(movement);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/{idVenda}",1L)).andReturn().getResponse();

        Movement responseObject = movementJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(movement,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("method findByMovementId")
    void returnNullWhenIdIsInvalid() throws Exception {
        when(service.findByMovementId(-1L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/{idVenda}",-1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }



    private static List<Movement> getMovementList() {
        List<Movement> movementList = new ArrayList<>();
        movementList.add(new Movement(1L, 2L, 3L, 1L, true, 100));
        movementList.add(new Movement(2L, 3L, 4L, 10L, true, 200));
        return movementList;
    }
}
