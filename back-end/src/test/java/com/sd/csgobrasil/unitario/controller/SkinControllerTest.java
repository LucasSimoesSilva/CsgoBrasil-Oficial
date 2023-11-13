package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.controllers.SkinController;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void getControllerReturnSkins() throws Exception {
        Skin skin = new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", "");

        List<Skin> skins = getSkins();

        when(service.listSkins()).thenReturn(skins);

        MockHttpServletResponse response = mvc.
                perform(get("/skin/skins")).andReturn().getResponse();

        List<Skin> responseObject = skinListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertEquals(responseObject.get(0).getNome(),skin.getNome());
        assertEquals(responseObject.get(0).getPreco(),skin.getPreco());
        assertEquals(responseObject.get(0).getRaridade(),skin.getRaridade());
        assertEquals(response.getStatus(), HttpStatus.OK.value());

    }

    private static List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L,"Dragon Lore","AWP",100,"Nova de Guerra",""));
        skins.add(new Skin(2L,"Dragon Red","Pistol",100,"Velha de Guerra",""));
        skins.add(new Skin(3L,"Dragon Blue","AWP",100,"Veterana",""));
        return skins;
    }
}
