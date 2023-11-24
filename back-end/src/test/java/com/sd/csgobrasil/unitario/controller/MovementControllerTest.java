package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.DTO.MovementsId;
import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import com.sd.csgobrasil.util.ReportImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovementControllerTest {

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
    private JacksonTester<Boolean> booleanJacksonTester;

    @Autowired
    private JacksonTester<MovementsId> movementsIdJson;

    @Autowired
    private JacksonTester<List<ReportImpl>> reportJson;

    @Autowired
    private JacksonTester<List<SkinMovement>> skinMovementListJson;


    List<Movement> movementList;
    Movement validMovement;

    @BeforeAll
    void beforeAll() {
        movementList = getMovements();
        validMovement = createMovement();
    }

    @Test
    void givenARequestGET_whenMovementRepositoryIsEmpty_thenReturnEmptyListAndStatusOK() throws Exception {
        when(service.listMovement()).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestGET_whenMovementRepositoryIsNotEmpty_thenReturnAMovementListAndStatusOK() throws Exception {
        List<Movement> movementListRight = movementList;

        when(service.listMovement()).thenReturn(movementListRight);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertTrue(responseObject.size() > 1);
        assertIterableEquals(movementListRight, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestGET_whenHaveSkinsInMovement_thenReturnASkinMovementListAndStatusOk() throws Exception {
        List<Movement> movements = movementList;
        when(service.listMovement()).thenReturn(movements);

        List<SkinMovement> skinMovements = getSkinMovementList();

        when(skinService.getSkinMovements(movements)).thenReturn(skinMovements);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/skinMovements")).andReturn().getResponse();

        List<SkinMovement> responseObject = skinMovementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertTrue(responseObject.size() > 1);
        assertIterableEquals(skinMovements, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    void givenARequestGET_whenHaveNotSkinsInMovement_thenReturnEmptyListAndStatusOk() throws Exception {
        when(service.listMovement()).thenReturn(new ArrayList<>());
        when(skinService.getSkinMovements(new ArrayList<>())).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/skinMovements")).andReturn().getResponse();

        List<SkinMovement> responseObject = skinMovementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestPOST_whenMovementIsValid_thenReturnTheMovementWithIdAndStatusCREATED() throws Exception {
        Movement movement = validMovement;

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
        assertEquals(movement, responseObject);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void givenARequestPOST_whenMovementIsInvalid_thenReturnStatusBadRequest() throws Exception {
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
    void givenARequestGET_whenMovementIdIsValid_thenReturnAMovementWithRightId() throws Exception {
        Movement movement = validMovement;
        when(service.findByMovementId(1L)).thenReturn(movement);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/{idVenda}", 1L)).andReturn().getResponse();

        Movement responseObject = movementJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(movement, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestGET_whenMovementIdIsInvalid_thenThrowNoSuchElementExceptionReturnStatusBadRequest() throws Exception {
        when(service.findByMovementId(-1L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/movement/{idVenda}", -1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenARequestPUT_whenIdsAreValid_thenReturnTrue() throws Exception {
        Long idVenda = 1L;
        Long idComprador = 2L;
        MovementsId movementsId = new MovementsId(idVenda, idComprador);

        when(service.makeMovement(idVenda, idComprador)).thenReturn(true);


        MockHttpServletResponse response = mvc
                .perform(
                        put("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementsIdJson.write(movementsId).getJson())
                )
                .andReturn().getResponse();

        Boolean responseObject = booleanJacksonTester.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertTrue(responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestPUT_whenIdsAreInvalid_thenThrowNoSuchElementExceptionAndStatusBadRequest() throws Exception {
        Long idVenda = -1L;
        Long idComprador = -2L;
        MovementsId movementsId = new MovementsId(idVenda, idComprador);

        when(service.makeMovement(idVenda, idComprador)).thenThrow(new NoSuchElementException());


        MockHttpServletResponse response = mvc
                .perform(
                        put("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementsIdJson.write(movementsId).getJson())
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenARequestGET_whenHaveMovements_thenReturnAReportListAndStatusOK() throws Exception {
        List<Report> reportList = new ArrayList<>();
        reportList.add(new ReportImpl(1L, "EstoqueDinamico", "Carlos", "Dragon Lore", true, 100));
        reportList.add(new ReportImpl(2L, "EstoqueDinamico", null, "Dragon Flame", false, 200));

        when(service.makeReport()).thenReturn(reportList);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/report")).andReturn().getResponse();

        List<ReportImpl> responseObject = reportJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertTrue(responseObject.size() > 1);
        assertIterableEquals(reportList, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestGET_whenHaveNotMovements_thenReturnAnEmptyReportListAndStatusOK() throws Exception {
        List<Report> reportList = new ArrayList<>();

        when(service.makeReport()).thenReturn(reportList);

        MockHttpServletResponse response = mvc.
                perform(get("/movement/report")).andReturn().getResponse();

        List<ReportImpl> responseObject = reportJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isEmpty();
        assertIterableEquals(reportList, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenARequestDELETE_whenIdIsValid_thenDeleteMovementFromDatabaseAndReturnStatusNoContent() throws Exception {
        doNothing().when(service).cancelMovement(1L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/movement/{idVenda}", 1L)
                )
                .andReturn().getResponse();

        verify(service, times(1)).cancelMovement(1L);

        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("", responseMessage);
    }


    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(1L, 1L, 2L, 3L, true, 123));
        movements.add(new Movement(2L, 1L, 2L, 4L, true, 234));
        return movements;
    }

    private Movement createMovement() {
        return new Movement(1L, 1L, 2L, 3L, true, 123);
    }

    private List<SkinMovement> getSkinMovementList() {
        List<SkinMovement> skinMovements = new ArrayList<>();
        skinMovements.add(new SkinMovement(1L, 1L, true, "dragon",
                "AWP", 10, "guerra", ""));
        skinMovements.add(new SkinMovement(2L, 1L, true, "eagle",
                "Pistola", 10, "guerra", ""));
        return skinMovements;
    }

}

