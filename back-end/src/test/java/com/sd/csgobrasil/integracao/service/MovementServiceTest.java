package com.sd.csgobrasil.integracao.service;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.util.ReportImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MovementServiceTest {

    @Autowired
    MovementService service;

    @Test
    void givenRequest_thenReturnAMovementList(){
        List<Movement> movementsRight = getMovements();

        List<Movement> movementsTest = service.listMovement();

        assertThat(movementsTest).isNotEmpty();
        for (Movement movement : movementsRight) {
            assertTrue(movementsTest.contains(movement));
        }
    }

    @Test
    void givenMovement_thenAddMovementToDatabaseAndReturnMovementWithId(){
        Movement movement = new Movement(3L,3L,false,8000);

        Movement movementRight = new Movement(42L, 3L, 1L, 3L, false, 7000);

        Movement movementTest = service.addMovement(movement);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenMovement_whenMovementIsInvalid_thenThrowConstraintViolationException(){
        Movement movementInvalid = new Movement(42L, null, 1L, 3L,
                false, 7000);

        try {
            service.addMovement(movementInvalid);
        }catch (ConstraintViolationException e){
            assertTrue(e.getMessage().contains("'não deve ser nulo'"));
        }
    }

    @Test
    void givenValidIdAndValidMovement_thenUpdateMovementInDatabaseAndReturnMovement(){
        Movement movementRight = new Movement(1L, 3L, 1L, 3L, false, 100);

        Movement movementTest = service.updateM(movementRight);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenInvalidMovementId_thenCreateMovementInDatabaseAndReturnMovementCreated(){
        Movement movementRight = new Movement(-1L, 3L, 1L, 3L, false, 100);

        Movement movementTest = service.updateM(movementRight);
        movementRight.setIdVenda(42L);

        assertNotNull(movementTest);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenValidId_thenReturnMovement(){
        Movement movementRight = new Movement(1L, 3L, 1L, 3L, true, 7000);

        Movement movementTest = service.findByMovementId(1L);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException(){
        String errorMessage = "No value present";
        try {
            service.findByMovementId(-1L);
        }catch (NoSuchElementException e){
            assertTrue(e.getMessage().contains(errorMessage));
        }
    }

    @Test
    void givenValidId_thenDeleteMovementFromDatabase(){
        Long id = 1L;
        service.cancelMovement(id);

        String errorMessage = "No value present";
        try {
            service.findByMovementId(id);
        }catch (NoSuchElementException e){
            assertEquals(errorMessage,e.getMessage());
        }
    }

    @Test
    void givenInvalidId_whenTryToDeleteMovement_thenThrowNoSuchElementException(){
        Long id = -1L;
        String errorMessage = "No value present";
        try {
            service.cancelMovement(id);
        }catch (NoSuchElementException e){
            assertEquals(errorMessage,e.getMessage());
        }
    }

    @Test
    void givenRequest_thenReturnAListWithTheReports(){
        List<Report> reportsRight = getReports();

        List<Report> reportsTest = service.makeReport();

        assertThat(reportsTest).isNotEmpty();

        for (int i = 0; i < reportsRight.size(); i++) {
            assertEquals(reportsRight.get(i).getPontos(), reportsTest.get(i).getPontos());
            assertEquals(reportsRight.get(i).getNomeComprador(), reportsTest.get(i).getNomeComprador());
            assertEquals(reportsRight.get(i).getNomeSkin(), reportsTest.get(i).getNomeSkin());
            assertEquals(reportsRight.get(i).getIdVenda(), reportsTest.get(i).getIdVenda());
            assertEquals(reportsRight.get(i).getNomeVendedor(), reportsTest.get(i).getNomeVendedor());
            assertEquals(reportsRight.get(i).getEstadoVenda(), reportsTest.get(i).getEstadoVenda());
        }
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsNotEqualsToComprador_whenCompradorHaveMoreOrEqualsPointsToTheSkinPrice_thenReturnTrue(){
        Long idVenda = 41L;
        Long idComprador = 2L;

        boolean check = service.makeMovement(idVenda, idComprador);
        assertTrue(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsEqualsToComprador_thenReturnFalse(){
        Long idVenda = 41L;
        Long idComprador = 4L;

        boolean check = service.makeMovement(idVenda, idComprador);
        assertFalse(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsNotEqualsToComprador_whenCompradorHaveLessPointsToTheSkinPrice_thenReturnFalse(){
        Long idVenda = 37L;
        Long idComprador = 1L;

        boolean check = service.makeMovement(idVenda, idComprador);
        assertFalse(check);
    }

    private List<Report> getReports() {
        List<Report> reportList = new ArrayList<>();
        reportList.add(new ReportImpl(1L,"EstoqueDinamico","Carlos",
                "M4A1-S Cyrex",true,7000));
        reportList.add(new ReportImpl(2L,"EstoqueDinamico","Carlos",
                "AWP Dragon Lore",true,10000));
        reportList.add(new ReportImpl(3L,"EstoqueDinamico","Administrador",
                "M4A1-S Hot Rod",true,6000));
        reportList.add(new ReportImpl(4L,"EstoqueDinamico","Administrador",
                "SCAR-20 Bloodsport",true,1000));
        return reportList;
    }

    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(1L, 3L, 1L, 3L, true, 7000));
        movements.add(new Movement(12L, 2L, null, 4L, false, 6000));
        movements.add(new Movement(22L, 3L, null, 14L, false, 8000));
        movements.add(new Movement(40L, 4L, null, 32L, false, 1500));
        return movements;
    }

}
