package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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
    void getEmptyListWhenHaveNotMovements() throws Exception {
        when(service.listMovement()).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private static List<Movement> getMovementList() {
        List<Movement> movementList = new ArrayList<>();
        movementList.add(new Movement(1L, 2L, 3L, 1L, true, 100));
        movementList.add(new Movement(2L, 3L, 4L, 10L, true, 200));
        return movementList;
    }
}
