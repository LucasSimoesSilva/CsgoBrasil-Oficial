package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.DTO.MovementsId;
import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import org.junit.jupiter.api.DisplayName;
import com.sd.csgobrasil.util.ReportImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureJsonTesters
class MovementControllerTest {

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
    private JacksonTester<List<SkinMovement>> skinMovementListJson;
    @Autowired
    private JacksonTester<List<ReportImpl>> reportJson;


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
    @DisplayName("method addMovement")
    void addInvalidMovement() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(
                        post("/movement")
                )
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("method listMovement")
    public void returnAllMovementsWhenDatabaseIsNotEmpty() throws Exception {
        int sizeOfListMovements = 41;
        List<Movement> movements = getMovements();

        MockHttpServletResponse response = mvc.
                perform(get("/movement/movements")).andReturn().getResponse();

        List<Movement> responseObject = movementListJson.parse(response.getContentAsString()).getObject();

        assertThat(response.getContentAsString()).isNotEmpty();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        for (Movement movement : movements) {
            assertTrue(responseObject.contains(movement));
        }
        assertEquals(sizeOfListMovements, responseObject.size());
    }

    @Test
    @DisplayName("method listMovementSkin")
    void getASkinMovementList() throws Exception {
        List<SkinMovement> skinMovements = getSkinMovementList();

        MockHttpServletResponse response = mvc.
                perform(get("/movement/skinMovements")).andReturn().getResponse();

        List<SkinMovement> responseObject = skinMovementListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        for (SkinMovement skinMovement : skinMovements) {
            assertTrue(responseObject.contains(skinMovement));
        }
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(32, responseObject.size());
    }

    @Test
    @DisplayName("method cancelMovement")
    void cancelMovementWhenIdIsValidAndWhenIdIsInvalid() throws Exception {
        Long idValidOrInvalid = 1L;

        MockHttpServletResponse response = mvc.
                perform(delete("/movement/{idVenda}",idValidOrInvalid)).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("method findByMovementId")
    void returnMovementWhenIdIsValid() throws Exception {
        Movement movement = createMovement();

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
        MockHttpServletResponse response = mvc.
                perform(get("/movement/{idVenda}",-1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }
    @Test
    @DisplayName("method makeMovement")
    void returnAMovementStatusTrue() throws Exception {
        Long idVenda = 10L;
        Long idComprador = 2L;
        MovementsId movementsId = new MovementsId(idVenda,idComprador);

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
    @DisplayName("method makeMovement")
    void returnAMovementStatusFalse() throws Exception {
        Long idVenda = -1L;
        Long idComprador = -2L;
        MovementsId movementsId = new MovementsId(idVenda,idComprador);

        MockHttpServletResponse response = mvc
                .perform(
                        put("/movement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(movementsIdJson.write(movementsId).getJson())
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }

    @DisplayName("method MakeReport")
    @Test
    void getReportList() throws Exception {
        List<Report> reportList = new ArrayList<>();
        reportList.add(new ReportImpl(2L,"EstoqueDinamico","Carlos","AWP Dragon Lore",true,10000));
        reportList.add(new ReportImpl(12L,"Administrador",null,"M4A1-S Hot Rod",false,6000));
        reportList.add(new ReportImpl(22L,"EstoqueDinamico",null,"AK-47 Vulcan",false,8000));
        reportList.add(new ReportImpl(40L,"EstoqueEstatico",null,"MP7 Impire",false,1500));

        MockHttpServletResponse response = mvc.
                perform(get("/movement/report")).andReturn().getResponse();

        List<ReportImpl> responseObject = reportJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();

        for (Report report : reportList) {
            assertTrue(responseObject.contains(report));
        }
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(1L, 3L, 1L, 3L, true, 7000));
        movements.add(new Movement(12L, 2L, null, 4L, false, 6000));
        movements.add(new Movement(22L, 3L, null, 14L, false, 8000));
        movements.add(new Movement(40L, 4L, null, 32L, false, 1500));
        return movements;
    }
    private Movement createMovement() {
        return new Movement(1L, 3L, 1L, 3L, true, 7000);
    }

    private List<SkinMovement> getSkinMovementList() {
        List<SkinMovement> skinMovements = new ArrayList<>();
        skinMovements.add(new SkinMovement(10L, 1L , false, "Cyrex",
                "M4A1-S", 7000, "Minimal Wear", "M4A1-S_Cyrex.png"));
        skinMovements.add(new SkinMovement(21L, 3L , false, "Fire Serpent",
                "AK-47", 9500, "Minimal Wear", "AK-47_Fire_Serpent.png"));
        skinMovements.add(new SkinMovement(34L, 4L , false, "The Traitor",
                "USP-S", 5000, "Field-Tested", "USP-S_The_Traitor.png"));
        return skinMovements;
    }

}
