package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.Skin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class SkinControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<Skin>> skinListJson;
    @Autowired
    private JacksonTester<Skin> skinJson;

    @DisplayName("method listSkins")
    @Test
    public void getControllerReturnSkins() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 10000, "Factory New", "AWP_Dragon_Lore.png");

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        List<Skin> responseObject = skinListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertEquals(skin,responseObject.get(0));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @DisplayName("method findBySkinId")
    @Test
    public void getControllerReturnSkinByID() throws Exception {
        Long id = 1L;

        Skin skin = new Skin(id, "Dragon Lore", "AWP", 10000, "Factory New", "AWP_Dragon_Lore.png");

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

        MockHttpServletResponse response = mvc.
                perform(get("/skin/{id}", "0")).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }
    @DisplayName("method saveSkin")
    @Test
    public void postSaveSkinValid() throws Exception {
        Skin skin = new Skin(33L, "Dragon Green", "AWP", 1000, "Nova de Guerra", "");


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
        Skin skin = new Skin(0L, null, "AWP", 100, "Nova de Guerra", "");


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
        Skin skin = new Skin(33L, "Dragon Blue", "AWP", 100, "Nova de Guerra", "");


        MockHttpServletResponse response = mvc
                .perform(
                        put("/skin/{id}",skin.getId())
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
        Skin skin = new Skin(0L, null, "AWP", 100, "Nova de Guerra", "");


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
    public void givenValidId_whenDoNotHaveUserWithTheSkin_thenDelete() throws Exception {

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}",2L)
                )
                .andReturn().getResponse();


        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("", responseMessage);
    }

    @DisplayName("method deleteSkin")
    @Test
    public void givenValidId_whenHaveUserWithTheSkin_thenThrowExceptionSQLIntegrityConstraintViolationException() throws Exception {

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}",1L)
                )
                .andReturn().getResponse();


        String responseMessage = response.getContentAsString();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Can not delete a skin that have an owner", responseMessage);
    }

    @DisplayName("method deleteSkin")
    @Test
    public void deleteInvalidId() throws Exception {

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/skin/{id}",0L)
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }
}
