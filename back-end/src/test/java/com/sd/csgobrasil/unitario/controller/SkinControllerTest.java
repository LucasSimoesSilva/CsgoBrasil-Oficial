package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.SkinService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @DisplayName("method listSkins")
    @Test
    public void getControllerReturnSkins() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        List<Skin> skins = getSkins();

        when(service.listSkins()).thenReturn(skins);

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        List<Skin> responseObject = skinListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertEquals(skin.getNome(), responseObject.get(0).getNome());
        assertEquals(skin.getPreco(), responseObject.get(0).getPreco());
        assertEquals(skin.getRaridade(), responseObject.get(0).getRaridade());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @DisplayName("method listSkins")
    @Test
    public void getControllerReturnEmptySkinIfDoNotHaveSkin() throws Exception {
        when(service.listSkins()).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
    @DisplayName("method findBySkinId")
    @Test
    public void getControllerReturnSkinByID() throws Exception {
        Long id = 1L;

        Skin skin = new Skin(id, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.findBySkinId(id)).thenReturn(skin);
        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", id)).andReturn().getResponse();
        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();


        assertEquals(skin.getNome(), skinResponse.getNome());
        assertEquals(skin.getPreco(), skinResponse.getPreco());
        assertEquals(skin.getRaridade(), skinResponse.getRaridade());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(skinResponse);
    }
    @DisplayName("method findBySkinId")
    @Test
    public void getControllerReturnBadRequestWhenIdIsNotValid() throws Exception {
        when(service.findBySkinId(0L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", "0")).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }
    @DisplayName("method saveSkin")
    @Test
    public void postSaveSkinValid() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.addSkin(any(Skin.class))).thenReturn(skin);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/skin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(skin.getNome(), skinResponse.getNome());
        assertEquals(skin.getPreco(),skinResponse.getPreco());
        assertEquals(skin.getRaridade(), skinResponse.getRaridade());
        assertNotNull(skinResponse);

    }
    @DisplayName("method saveSkin")
    @Test
    public void postDoNotSaveInvalidSkin() throws Exception {
        Skin skin = new Skin(1L, null, "AWP", 100, "Nova de Guerra", "");

        when(service.addSkin(any(Skin.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));

        MockHttpServletResponse response = mvc
                .perform(
                        post("/skin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Blank field in the request", responseMessage);
    }

    @DisplayName("method updateSkin")
    @Test
    public void putUpdateValidObject() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.updateSkin(eq(1L),any(Skin.class))).thenReturn(skin);

        MockHttpServletResponse response = mvc
                .perform(
                        put("/skin/{id}",1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(skin.getNome(), skinResponse.getNome());
        assertEquals(skin.getPreco(),skinResponse.getPreco());
        assertEquals(skin.getRaridade(), skinResponse.getRaridade());
        assertNotNull(skinResponse);
    }
    @DisplayName("method updateSkin")
    @Test
    public void putUpdateInvalidObject() throws Exception {
        Skin skin = new Skin(1L, null, "AWP", 100, "Nova de Guerra", "");

        when(service.updateSkin(eq(1L),any(Skin.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));

        MockHttpServletResponse response = mvc
                .perform(
                        put("/skin/{id}",1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Blank field in the request", responseMessage);
    }
    @DisplayName("method deleteSkin")
    @Test
    public void deleteValidId() throws Exception {
        doNothing().when(service).deleteSkin(1L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}",1L)
                )
                .andReturn().getResponse();

        verify(service, times(1)).deleteSkin(1L);

        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("", responseMessage);
    }
    @DisplayName("method deleteSkin")
    @Test
    public void deleteInvalidId() throws Exception {
        doThrow(new NoSuchElementException()).when(service).deleteSkin(0L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}",0L)
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }




    private static List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }


}
