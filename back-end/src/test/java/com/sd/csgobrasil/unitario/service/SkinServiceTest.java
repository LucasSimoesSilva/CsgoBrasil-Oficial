package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.repository.SkinRepository;
import com.sd.csgobrasil.service.SkinService;
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
class SkinServiceTest {

    @MockBean
    private SkinRepository repository;

    @Autowired
    private SkinService service;

    List<Skin> skinList;
    Long invalidId;

    Skin incompleteSkin;

    @BeforeAll
    void beforeAll() {
        skinList = getSkins();
        invalidId = -1L;
        incompleteSkin = getIncompleteSkin();
    }

    @Test
    void givenRequest_thenReturnAList(){
        when(repository.findAll()).thenReturn(skinList);
        List<Skin> skinsTest = service.listSkins();
        assertEquals(skinList,skinsTest);
    }

    @Test
    void givenRequest_thenReturnAnEmptyList(){
        List<Skin> emptyList = new ArrayList<>();
        when(repository.findAll()).thenReturn(emptyList);
        List<Skin> skinsTest = service.listSkins();

        assertThat(skinsTest).isEmpty();
        assertEquals(emptyList,skinsTest);
    }

    @Test
    void givenValidSkin_thenReturnTheSkinWithId(){
        Skin skin = incompleteSkin;
        Skin skinRight = new Skin(1L,"Dragao","AWP",100,"Nova","");

        when(repository.save(skin)).thenReturn(skinRight);
        Skin skinTest = service.addSkin(skin);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenInvalidIdSkin_thenCreateNewSkinInDatabaseAndReturnSkin(){
        Skin skin = incompleteSkin;
        Skin skinRight = new Skin(5L,"Dragao","AWP",100,"Nova","");
        Long idInvalid = invalidId;

        when(repository.save(skin)).thenReturn(skinRight);
        Skin skinTest = service.updateSkin(idInvalid,skin);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenValidSkin_thenUpdateSkinInDatabaseAndReturnSkin(){
        Skin skin = new Skin(5L,"Dragon Blue","AWP",100,"Nova","");
        Skin skinRight = new Skin(5L,"Dragon White","AWP",1000,"Velha","");
        Long idInvalid = skin.getId();

        when(repository.save(skinRight)).thenReturn(skinRight);
        Skin skinTest = service.updateSkin(idInvalid,skinRight);

        assertNotNull(skinTest);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenValidId_thenReturnSkin(){
        Skin skinRight = new Skin(1L,"Dragao","AWP",100,"Nova","");
        when(repository.findById(skinRight.getId())).thenReturn(Optional.of(skinRight));

        Skin skinTest = service.findBySkinId(1L);
        assertEquals(skinRight,skinTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException(){
        doThrow(new NoSuchElementException("Invalid id")).when(repository).findById(invalidId);

        try {
            service.findBySkinId(invalidId);
        }catch (NoSuchElementException e){
            assertEquals("Invalid id",e.getMessage());
        }
    }

    @Test
    void givenId_whenIdIsValid_thenDeleteSkin(){
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);
        service.deleteSkin(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void givenId_whenIdIsInvalid_thenThrowNoSuchElementException(){
        Long idInvalid = invalidId;
        String messageError = "Invalid id";
        when(repository.existsById(idInvalid)).thenReturn(false);

        try {
            service.deleteSkin(idInvalid);
        }catch (NoSuchElementException e){
            verify(repository, times(0)).deleteById(idInvalid);
            assertEquals(messageError,e.getMessage());
        }
    }

    @Test
    void givenMovementList_thenReturnListSkinWithState(){
        List<Movement> movements = getMovements();
        List<SkinMovement> skinsRight = new ArrayList<>();
        skinsRight.add(new SkinMovement(1L,1L,false,
                "Dragon Lore","AWP",100,"Nova de Guerra",""));

        skinsRight.add(new SkinMovement(2L,1L,false,
                "Dragon Red", "Pistol", 200, "Velha de Guerra", ""));

        skinsRight.add(new SkinMovement(4L,4L,false,
                "Dragon Blue", "AWP", 400, "Veterana", ""));

        for (int i = 0; i < movements.size(); i++) {
            when(repository.findById(movements.get(i).getIdSkin())).thenReturn(Optional.of(skinList.get(i)));
        }

        List<SkinMovement> skinMovementsTest = service.getSkinMovements(movements);

        assertThat(skinMovementsTest).isNotEmpty();
        assertIterableEquals(skinsRight,skinMovementsTest);
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 200, "Velha de Guerra", ""));
        skins.add(new Skin(2L, "Dragon White", "M4", 300, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 400, "Veterana", ""));
        return skins;

    }

    private List<Movement> getMovements() {
        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(1L, 1L, null, 1L, false, 123));
        movements.add(new Movement(2L, 1L, null, 2L, false, 234));
        movements.add(new Movement(3L, 1L, 2L, 2L, true, 567));
        movements.add(new Movement(4L, 4L, 2L, 2L, false, 567));
        return movements;
    }

    private Skin getIncompleteSkin() {
        Skin skin = new Skin("Dragao","AWP",100,"Nova","");
        return skin;
    }
}
