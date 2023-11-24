package com.sd.csgobrasil.integracao.repository;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.repository.UserRepository;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    Long idUserTest;
    Long idUserTestInvalid;
    String emailValid;
    String emailInvalid;
    String passwordValid;
    String passwordInvalid;
    String nameValid;
    String nameInvalid;

    @BeforeAll
    void beforeAll() {
        idUserTest = 1L;
        idUserTestInvalid = -1L;
        emailValid = "ca@gmail";
        emailInvalid = "Email invalid";
        passwordValid = "9090";
        passwordInvalid = "";
        nameValid = "Carlos";
        nameInvalid = "Name invalid";
    }

    @Test
    void givenUserId_whenIdIsValid_thenReturnAListWithTheUserSkinsWithState() {
        List<SkinWithStateImpl> skinsRight = getSkinWithStates();
        List<SkinWithState> skinsTest = repository.listSkinsWithStateFromUser(idUserTest);

        assertNotNull(skinsTest);
        assertEquals(2, skinsTest.size());
        for (int i = 0; i < skinsTest.size(); i++) {
            assertEquals(skinsRight.get(i).getIdSkin(), skinsTest.get(i).getIdSkin());
            assertEquals(skinsRight.get(i).getPreco(), skinsTest.get(i).getPreco());
            assertEquals(skinsRight.get(i).getIdVenda(), skinsTest.get(i).getIdVenda());
            assertEquals(skinsRight.get(i).getImagem(), skinsTest.get(i).getImagem());
            assertEquals(skinsRight.get(i).getArma(), skinsTest.get(i).getArma());
            assertEquals(skinsRight.get(i).getRaridade(), skinsTest.get(i).getRaridade());
            assertEquals(skinsRight.get(i).getEstadoVenda(), skinsTest.get(i).getEstadoVenda());
            assertEquals(skinsRight.get(i).getNome(), skinsTest.get(i).getNome());
        }
    }

    @Test
    void givenUserId_whenIdIsInvalid_thenReturnAnEmptyListIfTheUserDoNotExist() {
        List<SkinWithState> skinsTest = repository.listSkinsWithStateFromUser(idUserTestInvalid);
        assertThat(skinsTest).isEmpty();
    }

    @Test
    void givenUserNameAndEmail_whenEmailIsInvalid_thenReturnTrue() {
        String nome = nameValid;
        String email = emailInvalid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void givenUserNameAndEmail_whenNameIsInvalid_thenReturnTrue() {
        String nome = nameInvalid;
        String email = emailValid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void givenUserNameAndEmail_whenBothAreValid_thenReturnTrue() {
        String nome = nameValid;
        String email = emailValid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertTrue(validation);
    }

    @Test
    void givenUserNameAndEmail_whenBothAreInvalid_thenReturnFalse() {
        String nome = nameInvalid;
        String email = emailInvalid;
        boolean validation = repository.existsUserByEmailOrNome(email, nome);
        assertFalse(validation);
    }

    @Test
    void givenUserEmailAndSenha_whenBothAreValid_thenReturnTrue() {
        String email = emailValid;
        String senha = passwordValid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertTrue(validation);
    }

    @Test
    void givenUserEmailAndSenha_whenEmailIsInvalid_thenReturnFalse() {
        String email = emailInvalid;
        String senha = passwordValid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertFalse(validation);
    }

    @Test
    void givenUserEmailAndSenha_whenSenhaIsInvalid_thenReturnFalse() {
        String email = emailValid;
        String senha = passwordInvalid;
        boolean validation = repository.existsUserByEmailAndSenha(email, senha);
        assertFalse(validation);
    }

    @Test
    void givenUserEmail_whenEmailIsValid_thenReturnUser() {
        String email = emailValid;
        User userTest = repository.findUsersByEmail(email);
        List<Skin> skins = getSkinList();

        User userRight = new User(1L, "Carlos", "9090", "ca@gmail", 200, skins,
                "cliente");
        assertEquals(userRight, userTest);
    }

    @Test
    void givenUserEmail_whenEmailIsInvalid_thenReturnNull() {
        String email = emailInvalid;
        User userTest = repository.findUsersByEmail(email);
        assertNull(userTest);
    }

    @Test
    void givenUserId_whenIdIsValid_thenReturnAListWithSkinsFromUser() {
        List<Long> idSkinList = new ArrayList<>(List.of(1L, 3L));

        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        assertNotNull(userSkins);
        assertEquals(2L, userSkins.size());
        for (int i = 0; i < userSkins.size(); i++) {
            assertEquals(idSkinList.get(i), userSkins.get(i).getIdSkin());
            assertEquals(idUserTest, userSkins.get(i).getIdUser());
        }
    }

    @Test
    void givenUserId_whenIdIsInvalid_thenReturnAnEmptyList() {
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTestInvalid);
        assertThat(userSkins).isEmpty();
    }

    @Test
    void givenUserIdAndSkinId_thenCheckIfSkinWasDeletedFromUser() {
        Long idSkinTest = 1L;

        repository.deleteSkinFromUser(idSkinTest, idUserTest);
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        for (UserSkin userSkin : userSkins) {
            assertNotEquals(idSkinTest, (long) userSkin.getIdSkin());
        }
    }

    @Test
    void givenUserIdAndSkinId_thenCheckIfSkinWasAddedFromUser() {
        Long idSkinTest = 4L;
        Long idUserTestAdmin = 2L;

        repository.deleteSkinFromUser(idSkinTest, idUserTestAdmin);
        repository.addSkinFromUser(idSkinTest, idUserTest);
        List<UserSkin> userSkins = repository.listSkinsFromUser(idUserTest);
        boolean verify = userSkins.stream().anyMatch(userSkin -> {
            return userSkin.getIdSkin().equals(idSkinTest);
        });
        assertTrue(verify);
    }

    private List<SkinWithStateImpl> getSkinWithStates() {
        List<SkinWithStateImpl> listSkins = new ArrayList<>();
        SkinWithStateImpl skin1 = new SkinWithStateImpl(1L, "Dragon Lore", "AWP", 10000,
                "Factory New", "AWP_Dragon_Lore.png", false, true, 11L);
        SkinWithStateImpl skin2 = new SkinWithStateImpl(3L, "Cyrex", "M4A1-S", 7000, "Minimal Wear",
                "M4A1-S_Cyrex.png", false, true, 10L);
        listSkins.add(skin1);
        listSkins.add(skin2);
        return listSkins;
    }

    private List<Skin> getSkinList() {
        List<Skin> skins = new ArrayList<>(List.of(new Skin(1L, "Dragon Lore", "AWP"
                        , 10000, "Factory New", "AWP_Dragon_Lore.png"),
                new Skin(3L, "Cyrex", "M4A1-S", 7000, "Minimal Wear", "M4A1-S_Cyrex.png")));
        return skins;
    }
}
