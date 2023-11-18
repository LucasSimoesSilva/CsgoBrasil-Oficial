package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.controllers.UserSkinController;
import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class UserSkinControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<SkinWithStateImpl>> skinsWithStateListJson;

    @Autowired
    private JacksonTester<List<Skin>> skinsListJson;

    @Autowired
    UserSkinController controller;


    @Test
    void whenValidUserId_thenReturnSkinListWithStates() throws Exception {
        List<SkinWithStateImpl> listSkins = getSkinWithStates();


        ResponseEntity<List<SkinWithState>> response = controller.listSkinsWithState(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotEmpty();
        assertEquals(2,response.getBody().size());
        for (int i = 0; i < response.getBody().size(); i++) {
            assertEquals(listSkins.get(i).getIdSkin(),response.getBody().get(i).getIdSkin());
            assertEquals(listSkins.get(i).getNome(),response.getBody().get(i).getNome());
            assertEquals(listSkins.get(i).getArma(),response.getBody().get(i).getArma());
            assertEquals(listSkins.get(i).getPreco(),response.getBody().get(i).getPreco());
            assertEquals(listSkins.get(i).getRaridade(),response.getBody().get(i).getRaridade());
            assertEquals(listSkins.get(i).getImagem(),response.getBody().get(i).getImagem());
            assertEquals(listSkins.get(i).getEstadoVenda(),response.getBody().get(i).getEstadoVenda());
            assertEquals(listSkins.get(i).getIdVenda(),response.getBody().get(i).getIdVenda());
        }
    }

    @Test
    void whenInvalidUserId_thenReturnEmptyListWithState() throws Exception {
        MockHttpServletResponse response = mvc.
                perform(get("/userskin/skinsState/{idUser}", -1L)).andReturn().getResponse();

        List<SkinWithStateImpl> responseObject = skinsWithStateListJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.isEmpty());
    }

    @Test
    void whenValidUserId_thenReturnSkinList() throws Exception {
        List<Skin> skinList = getSkins();

        MockHttpServletResponse response = mvc.
                perform(get("/userskin/{idUser}", 1L)).andReturn().getResponse();

        List<Skin> responseObject = skinsListJson.parse(response.getContentAsString()).getObject();

        assertIterableEquals(skinList,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(2, responseObject.size());
    }

    @Test
    void whenInvalidUserId_thenReturnEmptyList() throws Exception {
        MockHttpServletResponse response = mvc.
                perform(get("/userskin/{idUser}", -1L)).andReturn().getResponse();

        List<Skin> responseObject = skinsListJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.isEmpty());
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New", "AWP_Dragon_Lore.png"));
        skins.add(new Skin(3L, "Cyrex", "M4A1-S", 7000, "Minimal Wear", "M4A1-S_Cyrex.png"));
        return skins;
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
}
