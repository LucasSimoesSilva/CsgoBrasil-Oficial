package com.sd.csgobrasil.integracao.repository;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.repository.MovementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class MovementRepositoryTest {

    @Autowired
    MovementRepository repository;

    @Test
    public void chamarMovement(){

    }


}
