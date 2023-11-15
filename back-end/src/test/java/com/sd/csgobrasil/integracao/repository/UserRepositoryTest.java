package com.sd.csgobrasil.integracao.repository;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    Long idUserTest = 2L;
    Long idUserTestInvalid = -1L;
    String emailValid = "ca@gmail";
    String emailInvalid = "Email invalid";
    String passwordValid = "9090";
    String passwordInvalid = "";
    String nameValid = "Carlos";
    String nameInvalid = "Name invalid";

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

    @Test
    void returnAnEmptyListIfTheUserDoNotExist(){
        List<SkinWithState> skinsTest = repository.listSkinsWithStateFromUser(idUserTestInvalid);
        assertThat(skinsTest).isEmpty();
    }

    @Test
    void returnTrueIfHasNameInDatabase(){
        String nome = nameValid;
        String email = emailInvalid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void returnTrueIfHasEmailInDatabase(){
        String nome = nameInvalid;
        String email = emailValid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void returnTrueIfHasEmailAndNameInDatabase(){
        String nome = nameValid;
        String email = emailValid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void returnFalseIfHasNotEmailOrNameInDatabase(){
        String nome = nameInvalid;
        String email = emailInvalid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertFalse(validation);
    }

    @Test
    void returnTrueIfHasEmailAndPasswordInDatabase(){
        String email = emailValid;
        String senha = passwordValid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertTrue(validation);
    }

    @Test
    void returnFalseIfHasNotEmailButHasPasswordInDatabase(){
        String email = emailInvalid;
        String senha = passwordValid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertFalse(validation);
    }

    @Test
    void returnFalseIfHasEmailButHasNotPasswordInDatabase(){
        String email = emailValid;
        String senha = passwordInvalid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertFalse(validation);
    }

    @Test
    void returnUserIfEmailHasInDatabase(){
        String email = emailValid;
        User userTest = repository.findUsersByEmail(email);

        List<Skin> skins = new ArrayList<>(List.of(new Skin(1L,"Dragon Lore","AWP",10000,"Factory New","AWP_Dragon_Lore.png"),
                new Skin(3L,"Cyrex","M4A1-S",7000,"Minimal Wear","M4A1-S_Cyrex.png")));
        User userRight = new User(1L, "Carlos", "9090", "ca@gmail", 200, skins,
                "cliente");

        assertAll(
                () -> assertEquals(userRight.getId(), userTest.getId()),
                () -> assertEquals(userRight.getCargo(), userTest.getCargo()),
                () -> assertEquals(userRight.getEmail(), userTest.getEmail()),
                () -> assertEquals(userRight.getNome(), userTest.getNome()),
                () -> assertEquals(userRight.getPontos(), userTest.getPontos()),
                () -> assertEquals(userRight.getSenha(), userTest.getSenha()),
                () -> assertIterableEquals(userRight.getSkinsUser(), userTest.getSkinsUser())
        );
    }

    @Test
    void returnUserIfEmailHasNotInDatabase(){
        String email = emailInvalid;
        User userTest = repository.findUsersByEmail(email);
        assertNull(userTest);
    }

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
    void returnAnEmptyListIfUserDoNotExist(){
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTestInvalid);
        assertThat(userSkins).isEmpty();
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
