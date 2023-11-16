package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.service.UserSkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserSkinService service;



    @Test
    public void shouldReturnAlistOfSkinFromUser(){
        Long idUser = 1L;


    }
}
