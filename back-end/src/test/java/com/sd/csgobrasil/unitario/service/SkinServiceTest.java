package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.repository.SkinRepository;
import com.sd.csgobrasil.service.SkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SkinServiceTest {

    @MockBean
    private SkinRepository repository;

    @Autowired
    private SkinService service;
    @Test
    void givenRequest_thenReturnAList(){

    }
}
