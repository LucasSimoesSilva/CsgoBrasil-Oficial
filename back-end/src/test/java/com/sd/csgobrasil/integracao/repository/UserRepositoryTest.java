package com.sd.csgobrasil.integracao.repository;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    Long idUserTest = 2L;

    @Test
    void returnAListWithTheUserSkinsWithState(){
        List<Boolean> estadoVendaList = getEstadoVendaList();
        List<String> nomeSkinList = getNomeSkinsList();

        List<SkinWithState> skinsTest = repository.listSkinsWithStateFromUser(idUserTest);

        assertNotNull(skinsTest);
        assertEquals(7,skinsTest.size());

        for (int i = 0; i < skinsTest.size(); i++) {
            assertEquals(nomeSkinList.get(i),skinsTest.get(i).getNome());
            assertEquals(estadoVendaList.get(i),skinsTest.get(i).getEstadoVenda());
        }
    }

    /*@Test
    void returnTrueIfHasNameInDatabase(){
        String nome = "Carlos";
        String email = "lalalala@gmail.com";
        boolean b = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(b);
    }

    @Test
    void returnTrueIfHasEamilInDatabase(){
        String nome = "Lalalalalala";
        String email = "ca@gmail";
        boolean b = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(b);
    }*/

    @Test
    void returnAListWithSkinsFromUser(){
        List<Long> idSkinList = new ArrayList<>(List.of(4L,5L,6L,7L,8L,9L,10L));

        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        assertNotNull(userSkins);
        assertEquals(7L,userSkins.size());
        for (int i = 0; i < userSkins.size(); i++) {
            assertEquals(idSkinList.get(i), userSkins.get(i).getIdSkin());
            assertEquals(idUserTest, userSkins.get(i).getIdUser());
        }
    }

    @Test
    void checkIfSkinWasDeletedFromUser(){
        Long idSkinTest = 4L;

        repository.deleteSkinFromUser(idSkinTest,idUserTest);
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        for (UserSkin userSkin : userSkins) {
            assertNotEquals(idSkinTest, (long) userSkin.getIdSkin());
        }
    }

    @Test
    void checkIfSkinWasAddedFromUser(){
        Long idSkinTest = 1L;

        repository.deleteSkinFromUser(idSkinTest,1L);
        repository.addSkinFromUser(idSkinTest,idUserTest);
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        boolean verify = userSkins.stream().anyMatch(userSkin -> {
            return userSkin.getIdSkin().equals(idSkinTest);
        });
        assertTrue(verify);
    }

    private static List<String> getNomeSkinsList() {
        List<String> nomeSkinList = new ArrayList<>();
        nomeSkinList.add("Hot Rod");
        nomeSkinList.add("Bloodsport");
        nomeSkinList.add("Splash Jam");
        nomeSkinList.add("Integrale");
        nomeSkinList.add("Hazard Pay");
        nomeSkinList.add("Chatterbox");
        nomeSkinList.add("Sugar Rush");
        nomeSkinList.add("Commemoration");
        return nomeSkinList;
    }

    private static List<Boolean> getEstadoVendaList() {
        List<Boolean> estadoVendaList = new ArrayList<>();
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        estadoVendaList.add(false);
        return estadoVendaList;
    }
}
