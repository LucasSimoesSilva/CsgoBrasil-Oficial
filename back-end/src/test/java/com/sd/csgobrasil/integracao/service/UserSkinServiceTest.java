package com.sd.csgobrasil.integracao.service;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class UserSkinServiceTest {
    public void fazerAlgo(){
    }
}
