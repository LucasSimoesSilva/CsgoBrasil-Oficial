package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.controllers.MovementController;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MovementControllerTest {

    @MockBean
    private MovementService service;

    @MockBean
    private SkinService skinService;

    @Autowired
    MovementController controller;

    @Test
    public void fazerCoisa(){

    }
}
