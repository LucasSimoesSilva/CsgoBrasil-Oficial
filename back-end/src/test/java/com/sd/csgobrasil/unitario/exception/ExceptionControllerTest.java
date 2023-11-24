package com.sd.csgobrasil.unitario.exception;

import com.sd.csgobrasil.infra.exception.ExceptionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ExceptionControllerTest {

    @Autowired
    ExceptionController controller;

    @Test
    void given(){

    }
}
