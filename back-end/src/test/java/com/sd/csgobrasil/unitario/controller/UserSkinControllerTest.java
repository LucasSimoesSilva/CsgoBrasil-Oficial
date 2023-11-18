package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserSkinControllerTest {

    @MockBean
    private UserSkinService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<SkinWithStateImpl>> skinsWithStateListJson;

    @Autowired
    private JacksonTester<List<Skin>> skinsListJson;

    @Test
    void whenValidUserId_thenReturnSkinListWithStates() throws Exception {
        List<SkinWithState> listSkins = getSkinWithStates();

        when(service.listSkinsWithStateFromUser(1L)).thenReturn(listSkins);

        MockHttpServletResponse response = mvc.
                perform(get("/userskin/skinsState/{idUser}", 1L)).andReturn().getResponse();

        List<SkinWithStateImpl> responseObject = skinsWithStateListJson.parse(response.getContentAsString()).getObject();

        assertIterableEquals(listSkins,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @Test
    void whenInvalidUserId_thenReturnEmptyListWithState() throws Exception {
        when(service.listSkinsWithStateFromUser(-1L)).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/userskin/skinsState/{idUser}", -1L)).andReturn().getResponse();

        List<SkinWithStateImpl> responseObject = skinsWithStateListJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.isEmpty());
    }

    @Test
    void whenValidUserId_thenReturnSkinList() throws Exception {
        List<Skin> skinList = getSkins();

        when(service.listSkinsFromUser(1L)).thenReturn(skinList);

        MockHttpServletResponse response = mvc.
                perform(get("/userskin/{idUser}", 1L)).andReturn().getResponse();

        List<Skin> responseObject = skinsListJson.parse(response.getContentAsString()).getObject();

        assertIterableEquals(skinList,responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @Test
    void whenInvalidUserId_thenReturnEmptyList() throws Exception {
        when(service.listSkinsFromUser(-1L)).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/userskin/{idUser}", -1L)).andReturn().getResponse();

        List<Skin> responseObject = skinsListJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.isEmpty());
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }

    private List<SkinWithState> getSkinWithStates() {
        List<SkinWithState> listSkins = new ArrayList<>();
        SkinWithStateImpl skin1 = new SkinWithStateImpl(1L, "Dragon Lore", "AWP", 10000,
                "Factory New", "AWP_Dragon_Lore.png", false, true, 11L);
        SkinWithStateImpl skin2 = new SkinWithStateImpl(3L, "Cyrex", "M4A1-S", 7000, "Minimal Wear",
                "M4A1-S_Cyrex.png", false, true, 10L);
        listSkins.add(skin1);
        listSkins.add(skin2);
        return listSkins;
    }


}
