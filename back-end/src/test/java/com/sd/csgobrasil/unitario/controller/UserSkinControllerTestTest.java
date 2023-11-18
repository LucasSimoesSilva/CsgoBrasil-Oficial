package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.UserSkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class UserSkinControllerTestTest {

    @Autowired
    UserSkinService service;



    @Autowired
    private MockMvc mvc;

    @Test
    public void fazerCoisa() {

    }


}
