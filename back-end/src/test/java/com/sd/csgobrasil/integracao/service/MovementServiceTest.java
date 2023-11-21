package com.sd.csgobrasil.integracao.service;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.util.ReportImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
public class MovementServiceTest {

    /*@Autowired
    MovementService service;

    @Test
    void givenRequest_thenReturnAMovementList(){
        List<Movement> movementsRight = getMovements();


        List<Movement> movementsTest = service.listMovement();

        assertThat(movementsTest).isNotEmpty();
        assertEquals(movementsRight, movementsTest);
    }

    @Test
    void givenRequest_thenReturnAnEmptyList(){
        List<Movement> movementsRight = new ArrayList<>();


        List<Movement> movementsTest = service.listMovement();

        assertThat(movementsTest).isEmpty();
        assertEquals(movementsRight, movementsTest);
    }

    @Test
    void givenMovement_thenAddMovementToDatabaseAndReturnMovementWithId(){
        Movement movement = new Movement(3L,3L,false,7000);

        Movement movementRight = new Movement(1L, 3L, 1L, 3L, false, 7000);



        Movement movementTest = service.addMovement(movement);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenValidIdAndValidMovement_thenUpdateMovementInDatabaseAndReturnMovement(){
        Movement movementRight = new Movement(1L, 3L, 1L, 3L, true, 7000);



        Movement movementTest = service.updateM(movementRight);
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
        try {
            service.findByMovementId(-1L);
        }catch (NoSuchElementException e){
            assertEquals("Invalid id",e.getMessage());
        }
    }

    @Test
    void givenValidOrInvalidId_thenDeleteMovementFromDatabase(){
        Long id = 1L;
        service.cancelMovement(id);
    }

    @Test
    void givenRequest_thenReturnAListWithTheReports(){
        List<Report> reportsRight = getReports();


        List<Report> reportsTest = service.makeReport();

        assertThat(reportsTest).isNotEmpty();
        assertIterableEquals(reportsRight,reportsTest);
    }

    @Test
    void givenRequest_whenDoNotHaveMovements_thenReturnAnEmptyList(){
        List<Report> reportsRight = new ArrayList<>();


        List<Report> reportsTest = service.makeReport();

        assertThat(reportsTest).isEmpty();
        assertIterableEquals(reportsRight,reportsTest);
    }


    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsNotEqualsToComprador_whenCompradorHaveMoreOrEqualsPointsToTheSkinPrice_thenReturnTrue(){
        Long idVenda = 1L;
        Long idComprador = 1L;
        Movement movement = new Movement(1L, 2L, 1L, 3L, true, 7000);
        Skin skinMovement = new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", "");
        User comprador = new User(1L,"Carlos","9090","ca@gmail",20000,null,"cliente");
        User vendedor = new User(2L,"Administrador","admin","admin@admin.com",100000,null,"admin");





        boolean check = service.makeMovement(idVenda, idComprador);
        assertTrue(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsEqualsToComprador_thenReturnTrue(){
        Long idVenda = 1L;
        Long idComprador = 1L;
        Movement movement = new Movement(1L, 2L, 1L, 3L, true, 7000);
        Skin skinMovement = new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", "");
        User comprador = new User(1L,"Carlos","9090","ca@gmail",20000,null,"cliente");
        User vendedor = new User(1L,"Carlos","9090","ca@gmail",20000,null,"cliente");



        boolean check = service.makeMovement(idVenda, idComprador);
        assertFalse(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsNotEqualsToComprador_whenCompradorHaveLessPointsToTheSkinPrice_thenReturnTrue(){
        Long idVenda = 1L;
        Long idComprador = 1L;
        Movement movement = new Movement(1L, 2L, 1L, 3L, true, 7000);
        Skin skinMovement = new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", "");
        User comprador = new User(1L,"Carlos","9090","ca@gmail",2,null,"cliente");
        User vendedor = new User(2L,"Administrador","admin","admin@admin.com",100000,null,"admin");



        boolean check = service.makeMovement(idVenda, idComprador);
        assertFalse(check);
    }



    private List<Report> getReports() {
        List<Report> reportList = new ArrayList<>();
        reportList.add(new ReportImpl(2L,"EstoqueDinamico","Carlos","AWP Dragon Lore",true,10000));
        reportList.add(new ReportImpl(12L,"Administrador",null,"M4A1-S Hot Rod",false,6000));
        reportList.add(new ReportImpl(22L,"EstoqueDinamico",null,"AK-47 Vulcan",false,8000));
        reportList.add(new ReportImpl(40L,"EstoqueEstatico",null,"MP7 Impire",false,1500));
        return reportList;
    }

    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(1L, 3L, 1L, 3L, true, 7000));
        movements.add(new Movement(12L, 2L, null, 4L, false, 6000));
        movements.add(new Movement(22L, 3L, null, 14L, false, 8000));
        movements.add(new Movement(40L, 4L, null, 32L, false, 1500));
        return movements;
    }*/

}
