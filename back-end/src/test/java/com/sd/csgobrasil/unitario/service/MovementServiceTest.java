package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.repository.MovementRepository;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import com.sd.csgobrasil.service.UserService;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.ReportImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovementServiceTest {

    @MockBean
    private MovementRepository repository;


    @MockBean
    private UserService userService;


    @MockBean
    private SkinService skinService;


    @MockBean
    private UserSkinService userSkinService;

    @Autowired
    MovementService service;

    List<Movement> movementList;
    List<Report> reportList;
    Movement movementCorrect;
    Long invalidId;

    @BeforeAll
    void beforeAll() {
        movementList = getMovements();
        reportList = getReports();
        movementCorrect = getMovementRight();
        invalidId = -1L;
    }

    @Test
    void givenRequest_thenReturnAMovementList(){
        List<Movement> movementsRight = movementList;
        when(repository.findAll()).thenReturn(movementsRight);

        List<Movement> movementsTest = service.listMovement();

        assertThat(movementsTest).isNotEmpty();
        assertEquals(movementsRight, movementsTest);
    }

    @Test
    void givenRequest_thenReturnAnEmptyList(){
        List<Movement> movementsRight = new ArrayList<>();
        when(repository.findAll()).thenReturn(movementsRight);

        List<Movement> movementsTest = service.listMovement();

        assertThat(movementsTest).isEmpty();
        assertEquals(movementsRight, movementsTest);
    }

    @Test
    void givenMovement_thenAddMovementToDatabaseAndReturnMovementWithId(){
        Movement movement = new Movement(3L,3L,true,7000);

        Movement movementRight = movementCorrect;

        when(repository.save(movement)).thenReturn(movementRight);

        Movement movementTest = service.addMovement(movement);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenValidIdAndValidMovement_thenUpdateMovementInDatabaseAndReturnMovement(){
        Movement movementRight = movementCorrect;

        when(repository.save(movementRight)).thenReturn(movementRight);

        Movement movementTest = service.updateM(movementRight);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenValidId_thenReturnMovement(){
        Movement movementRight = movementCorrect;

        when(repository.findById(movementRight.getIdVenda())).thenReturn(Optional.of(movementRight));

        Movement movementTest = service.findByMovementId(1L);
        assertEquals(movementRight,movementTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException(){
        doThrow(new NoSuchElementException("Invalid id")).when(repository).findById(invalidId);

        try {
            service.findByMovementId(invalidId);
        }catch (NoSuchElementException e){
            assertEquals("Invalid id",e.getMessage());
        }
    }

    @Test
    void givenValidOrInvalidId_thenDeleteMovementFromDatabase(){
        Long id = 1L;
        doNothing().when(repository).deleteById(id);
        service.cancelMovement(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void givenRequest_thenReturnAListWithTheReports(){
        List<Report> reportsRight = reportList;
        when(repository.makeReport()).thenReturn(reportsRight);

        List<Report> reportsTest = service.makeReport();

        assertThat(reportsTest).isNotEmpty();
        assertIterableEquals(reportsRight,reportsTest);
    }

    @Test
    void givenRequest_whenDoNotHaveMovements_thenReturnAnEmptyList(){
        List<Report> reportsRight = new ArrayList<>();
        when(repository.makeReport()).thenReturn(reportsRight);

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

        when(repository.findById(idVenda)).thenReturn(Optional.of(movement));
        when(skinService.findBySkinId(movement.getIdSkin())).thenReturn(skinMovement);
        when(userService.findByUserId(idComprador)).thenReturn(comprador);
        when(userService.findByUserId(movement.getIdVendedor())).thenReturn(vendedor);

        doNothing().when(userSkinService).deleteSkinFromUser(skinMovement.getId(), vendedor.getId());
        doNothing().when(userSkinService).addSkinFromUser(skinMovement.getId(), comprador.getId());
        when(userService.updateUser(comprador.getId(), comprador)).thenReturn(new User());
        when(userService.updateUser(vendedor.getId(), vendedor)).thenReturn(new User());

        when(repository.save(movement)).thenReturn(new Movement());

        boolean check = service.makeMovement(idVenda, idComprador);
        assertTrue(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsEqualsToComprador_thenReturnFalse(){
        Long idVenda = 1L;
        Long idComprador = 1L;
        Movement movement = new Movement(1L, 2L, 1L, 3L, true, 7000);
        Skin skinMovement = new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", "");
        User comprador = new User(1L,"Carlos","9090","ca@gmail",20000,null,"cliente");
        User vendedor = new User(1L,"Carlos","9090","ca@gmail",20000,null,"cliente");

        when(repository.findById(idVenda)).thenReturn(Optional.of(movement));
        when(skinService.findBySkinId(movement.getIdSkin())).thenReturn(skinMovement);
        when(userService.findByUserId(idComprador)).thenReturn(comprador);
        when(userService.findByUserId(movement.getIdVendedor())).thenReturn(vendedor);

        boolean check = service.makeMovement(idVenda, idComprador);
        assertFalse(check);
    }

    @Test
    void givenValidIdVendaAndValidIdComprador_whenVendedorIsNotEqualsToComprador_whenCompradorHaveLessPointsToTheSkinPrice_thenReturnFalse(){
        Long idVenda = 1L;
        Long idComprador = 1L;
        Movement movement = new Movement(1L, 2L, 1L, 3L, true, 7000);
        Skin skinMovement = new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", "");
        User comprador = new User(1L,"Carlos","9090","ca@gmail",2,null,"cliente");
        User vendedor = new User(2L,"Administrador","admin","admin@admin.com",100000,null,"admin");

        when(repository.findById(idVenda)).thenReturn(Optional.of(movement));
        when(skinService.findBySkinId(movement.getIdSkin())).thenReturn(skinMovement);
        when(userService.findByUserId(idComprador)).thenReturn(comprador);
        when(userService.findByUserId(movement.getIdVendedor())).thenReturn(vendedor);

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
    }

    private Movement getMovementRight() {
        return new Movement(1L, 3L, 1L, 3L, true, 7000);
    }
}
