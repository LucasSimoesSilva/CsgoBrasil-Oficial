package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.SkinService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
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
class SkinControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SkinService service;

    @Autowired
    private JacksonTester<List<Skin>> skinListJson;
    @Autowired
    private JacksonTester<Skin> skinJson;

    @Test
    void givenRequestGET_thenReturnSkinListAndStatusOK() throws Exception {
        List<Skin> skins = getSkins();

        when(service.listSkins()).thenReturn(skins);

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        List<Skin> responseObject = skinListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertTrue(responseObject.size() > 1);
        assertEquals(skins, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestGET_whenHaveNotSkins_thenReturnEmptySkinListAndStatusOK() throws Exception {
        String expectedResponse = "[]";
        when(service.listSkins()).thenReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedResponse, response.getContentAsString());
    }

    @Test
    void givenRequestGET_whenIdIsValid_thenReturnSkinWithCorrectIdAndStatusOK() throws Exception {
        Long id = 1L;

        Skin skin = new Skin(id, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.findBySkinId(id)).thenReturn(skin);

        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", id)).andReturn().getResponse();

        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();

        assertNotNull(skinResponse);
        assertEquals(skin, skinResponse);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestGET_whenIdIsInvalid_thenThrowNoSuchElementExceptionAndReturnStatusBadRequestAndAErrorMessage() throws Exception {
        when(service.findBySkinId(0L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", "0")).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestPOSTAndSkin_thenReturnSkinWithIdAndStatusCREATED() throws Exception {
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

        assertNotNull(skinResponse);
        assertEquals(skin, skinResponse);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    @Test
    void givenRequestPOSTAndSkin_whenSkinIsInvalid_thenThrowConstraintViolationExceptionAndReturnStatusBadRequest() throws Exception {
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

        assertEquals("", responseMessage);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestPUTAndSkin_thenReturnSkinUpdated() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        when(service.updateSkin(eq(1L), any(Skin.class))).thenReturn(skin);

        MockHttpServletResponse response = mvc
                .perform(
                        put("/skin/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        Skin skinResponse = skinJson.parse(response.getContentAsString()).getObject();

        assertNotNull(skinResponse);
        assertEquals(skin, skinResponse);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPUTAndSkin_whenSkinIsInvalid_thenThrowConstraintViolationExceptionReturnStatusBadRequest() throws Exception {
        Skin skin = new Skin(1L, null, "AWP", 100, "Nova de Guerra", "");

        when(service.updateSkin(eq(1L), any(Skin.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));

        MockHttpServletResponse response = mvc
                .perform(
                        put("/skin/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(skinJson.write(skin).getJson())
                )
                .andReturn().getResponse();

        String responseMessage = response.getContentAsString();

        assertEquals("", responseMessage);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestDELETEValidId_whenHaveUserWithTheSkin_thenThrowExceptionDataIntegrityViolationException() throws Exception {
        String messageErrorCheck = "REFERENCES PUBLIC.SKIN(ID)";

        doThrow(new DataIntegrityViolationException("Error in REFERENCES PUBLIC.SKIN(ID)"))
                .when(service).deleteSkin(1L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}", 1L)
                )
                .andReturn().getResponse();

        String responseMessage = response.getContentAsString();

        assertTrue(responseMessage.contains(messageErrorCheck));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestDELETE_whenIdIsValid_thenReturnStatusNoContent() throws Exception {
        doNothing().when(service).deleteSkin(1L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}", 1L)
                )
                .andReturn().getResponse();

        verify(service, times(1)).deleteSkin(1L);

        String responseMessage = response.getContentAsString();

        assertEquals("", responseMessage);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void givenRequestDELETE_whenIdIsInvalid_thenThrowNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException()).when(service).deleteSkin(0L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}", 0L)
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }
}
