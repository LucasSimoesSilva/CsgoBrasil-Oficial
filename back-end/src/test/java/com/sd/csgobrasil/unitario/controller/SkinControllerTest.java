package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.SkinService;
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
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SkinControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SkinService service;

    @Autowired
    private JacksonTester<List<Skin>> skinListJson;
    @Autowired
    private JacksonTester<Skin> skinJson;

    @Test
    public void getControllerReturnSkins() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        List<Skin> skins = getSkins();

        when(service.listSkins()).thenReturn(skins);

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        List<Skin> responseObject = skinListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertEquals(responseObject.get(0).getNome(), skin.getNome());
        assertEquals(responseObject.get(0).getPreco(), skin.getPreco());
        assertEquals(responseObject.get(0).getRaridade(), skin.getRaridade());
        assertEquals(response.getStatus(), HttpStatus.OK.value());

    }

    @Test
    public void getControllerReturnSkinByID() throws Exception {
        Long id = 1L;

        Skin skin = new Skin(id, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.findBySkinId(id)).thenReturn(skin);
        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", id)).andReturn().getResponse();
        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();


        assertEquals(skinResponse.getNome(), skin.getNome());
        assertEquals(skinResponse.getPreco(), skin.getPreco());
        assertEquals(skinResponse.getRaridade(), skin.getRaridade());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertNotNull(skinResponse);
    }

    @Test
    public void getControllerReturnBadRequestWhenIdIsNotValid() throws Exception {
        when(service.findBySkinId(0L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", "0")).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    private static List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }


}
