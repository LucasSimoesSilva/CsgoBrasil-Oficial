package com.sd.csgobrasil.integracao.service;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserSkinServiceTest {
    @Autowired
    private UserSkinService service;

    @Test
    void givenSkinIdAndUserId_thenAddSkinFromUser() {
        Long skinId = 1L;
        Long userIdDelete = 1L;
        Long userIdAddSkin = 2L;

        service.deleteSkinFromUser(skinId, userIdDelete);
        service.addSkinFromUser(skinId, userIdAddSkin);

        List<Skin> skins = service.listSkinsFromUser(userIdAddSkin);

        Skin skin = getSkin();

        assertTrue(skins.contains(skin));
    }

    @Test
    void givenSkinIdAndUserId_whenSkinIdIsInvalid_thenThrowDataIntegrityViolationException() {
        Long invalidSkinId = -1L;
        Long userIdDelete = 1L;
        Long userIdAddSkin = 2L;
        String messageError = "(CAST(" + invalidSkinId + " AS BIGINT))";

        service.deleteSkinFromUser(invalidSkinId, userIdDelete);

        try {
            service.addSkinFromUser(invalidSkinId, userIdAddSkin);
        } catch (DataIntegrityViolationException ex) {
            assertTrue(ex.getMessage().contains(messageError));
        }
    }

    @Test
    void givenSkinIdAndUserId_whenUserWhoWillAcquireIdIsInvalid_thenThrowDataIntegrityViolationException() {
        Long skinId = 1L;
        Long userIdDelete = 1L;
        Long userIdAddSkin = 22L;
        String messageError = "PUBLIC.USUARIO(ID) (CAST(" + userIdAddSkin + " AS BIGINT))";

        service.deleteSkinFromUser(skinId, userIdDelete);

        try {
            service.addSkinFromUser(skinId, userIdAddSkin);
        } catch (DataIntegrityViolationException ex) {
            assertTrue(ex.getMessage().contains(messageError));
        }
    }

    @Test
    void givenSkinIdAndUserId_thenDeleteSkinFromUser() {
        Long skinsPossuidasId = 1L;
        Long userId = -1L;
        Skin skin = getSkin();

        service.deleteSkinFromUser(skinsPossuidasId, userId);

        List<Skin> skins = service.listSkinsFromUser(userId);
        assertFalse(skins.contains(skin));
    }

    @Test
    void givenSkinIdAndUserId_whenSkinIdIsInvalid_thenDoNotDeleteSkinFromUser() {
        Long skinsPossuidasId = -1L;
        Long userId = 1L;
        Skin skin = getSkin();

        service.deleteSkinFromUser(skinsPossuidasId, userId);

        List<Skin> skins = service.listSkinsFromUser(userId);
        assertTrue(skins.contains(skin));
    }

    @Test
    void givenUserId_thenReturnSkinsFromUser() {
        Long userId = 1L;
        List<Skin> skinsRight = getSkins();

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isNotEmpty();
        assertEquals(skinsRight, skinsTest);
    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptyList() {
        Long userId = -2L;

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isEmpty();
    }

    @Test
    void givenUserId_thenReturnSkinWithStateList() {
        List<SkinWithState> skinWithStateListRight = new ArrayList<>();
        skinWithStateListRight.add(new SkinWithStateImpl(1L, "Dragon Lore", "AWP", 10000,
                "Factory New", "AWP_Dragon_Lore.png", false, true, 11L));
        skinWithStateListRight.add(new SkinWithStateImpl(3L, "Cyrex", "M4A1-S", 7000,
                "Minimal Wear", "M4A1-S_Cyrex.png", false, true, 10L));

        Long userId = 1L;

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);

        for (int i = 0; i < skinWithStateListTest.size(); i++) {
            assertEquals(skinWithStateListRight.get(i).getIdSkin(), skinWithStateListTest.get(i).getIdSkin());
            assertEquals(skinWithStateListRight.get(i).getNome(), skinWithStateListTest.get(i).getNome());
            assertEquals(skinWithStateListRight.get(i).getArma(), skinWithStateListTest.get(i).getArma());
            assertEquals(skinWithStateListRight.get(i).getPreco(), skinWithStateListTest.get(i).getPreco());
            assertEquals(skinWithStateListRight.get(i).getRaridade(), skinWithStateListTest.get(i).getRaridade());
            assertEquals(skinWithStateListRight.get(i).getImagem(), skinWithStateListTest.get(i).getImagem());
            assertEquals(skinWithStateListRight.get(i).getEstadoVenda(), skinWithStateListTest.get(i).getEstadoVenda());
            assertEquals(skinWithStateListRight.get(i).getIdVenda(), skinWithStateListTest.get(i).getIdVenda());
        }
    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptySkinWithStateList() {
        Long userId = -2L;

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);
        assertTrue(skinWithStateListTest.isEmpty());

    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(getSkin());
        skins.add(new Skin(3L, "Cyrex", "M4A1-S", 7000, "Minimal Wear", "M4A1-S_Cyrex.png"));
        return skins;
    }

    private Skin getSkin() {
        return new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New",
                "AWP_Dragon_Lore.png");

    }
}
