package com.sd.csgobrasil.integracao.service;

import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.repository.SkinRepository;
import com.sd.csgobrasil.service.SkinService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SkinServiceTest {

    @Autowired
    private SkinService service;
    @Test
    void givenRequest_thenReturnAList(){
        List<Skin> skinsTest = service.listSkins();

        assertFalse(skinsTest.isEmpty());

        for (Skin skin : getSkins()) {
            assertTrue(skinsTest.contains(skin));
        }
    }

    @Test
    void givenValidSkin_thenReturnTheSkinWithId(){
        Skin skin = new Skin("Dragon Blue","AWP",10000,"Factory New","AWP_Dragon_Blue.png");
        Skin skinRight = new Skin(34L, "Dragon Blue", "AWP", 10000, "Factory New",
                "AWP_Dragon_Blue.png");

        Skin skinTest = service.addSkin(skin);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenInvalidSkin_thenThrowConstraintViolationException(){
        Skin skin = new Skin(null,"AWP",10000,"Factory New","AWP_Dragon_Blue.png");

        try {
            service.addSkin(skin);
        }catch (ConstraintViolationException e){
            assertTrue(e.getMessage().contains("'não deve ser nulo'"));
            assertTrue(e.getMessage().contains("'não deve estar em branco'"));
        }
    }

    @Test
    void givenValidSkin_thenUpdateSkinInDatabaseAndReturnSkinUpdated(){
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New",
                "AWP_Dragon_Lore.png");
        Skin skinRight = new Skin("Dragon Green","AWP",10000,"Factory New","AWP_Dragon_Blue.png");
        Long validId = skin.getId();

        Skin skinTest = service.updateSkin(validId,skinRight);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenInvalidSkin_thenCreateSkinInDatabaseAndReturnSkinCreated(){
        Skin skinRight = new Skin("Dragon Green","AWP",10000,"Factory New","AWP_Dragon_Blue.png");
        Long invalidId = -1L;

        Skin skinTest = service.updateSkin(invalidId,skinRight);
        skinRight.setId(34L);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenValidId_thenReturnSkin(){
        Long validId = 1L;
        Skin skinRight = new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New",
                "AWP_Dragon_Lore.png");

        Skin skinTest = service.findBySkinId(validId);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException(){
        Long invalidId = -1L;
        String errorMessage = "No value present";
        try {
            service.findBySkinId(invalidId);
        }catch (NoSuchElementException e){
            assertEquals(errorMessage,e.getMessage());
        }
    }

    @Test
    void givenId_whenIdIsValid_thenDeleteSkin(){
        Long id = 2L;
        service.deleteSkin(id);

        String errorMessage = "No value present";
        try {
            service.findBySkinId(id);
        }catch (NoSuchElementException e){
            assertEquals(errorMessage,e.getMessage());
        }
    }

    @Test
    void givenId_whenIdIsInvalid_thenThrowNoSuchElementException(){
        Long idInvalid = -1L;
        String messageError = "Invalid id";

        try {
            service.deleteSkin(idInvalid);
        }catch (NoSuchElementException e){
            assertEquals(messageError,e.getMessage());
        }
    }

    @Test
    void givenMovementList_whenListIsEmpty_thenReturnEmptyListSkinWithState(){
        List<Movement> movements = new ArrayList<>();

        List<SkinMovement> skinMovementsTest = service.getSkinMovements(movements);

        assertThat(skinMovementsTest).isEmpty();
    }


    private List<SkinMovement> getSkinMovement(){
        List<SkinMovement> skins = new ArrayList<>();

        skins.add(new SkinMovement(10L,1L,false,
                "Cyrex", "M4A1-S", 7000, "Minimal Wear", "M4A1-S_Cyrex.png"));

        skins.add(new SkinMovement(11L,1L,false,
                "Dragon Lore", "AWP", 10000, "Factory New", "AWP_Dragon_Lore.png"));

        skins.add(new SkinMovement(29L,4L,false,
                "Mehndi", "P250", 2000, "Minimal Wear", "P250_Mehndi.png"));
        return skins;
    }
    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New", "AWP_Dragon_Lore.png"));
        skins.add(new Skin(11L, "Roll Cage", "FAMAS", 3000, "Field-Tested", "FAMAS_Roll_Cage.png"));
        skins.add(new Skin(19L, "Vogue", "Glock-18", 5000, "Factory New", "Glock-18_Vogue.png"));
        skins.add(new Skin(30L, "Code Red", "Desert Eagle", 3000, "Field-Tested", "Desert_Eagle_Code_Red.png"));
        skins.add(new Skin(33L, "Whiteout", "MP7", 2000, "Well-Worn", "MP7_Whiteout.png"));
        return skins;

    }
    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(10L, 1L, null, 3L, false, 7000));
        movements.add(new Movement(11L, 1L, null, 1L, false, 10000));
        movements.add(new Movement(1L, 3L, 1L, 3L, true, 7000));
        movements.add(new Movement(29L, 4L, null, 21L, false, 2000));
        return movements;
    }
}
