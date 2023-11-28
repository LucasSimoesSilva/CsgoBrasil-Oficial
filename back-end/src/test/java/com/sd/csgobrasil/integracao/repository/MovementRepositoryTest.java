package com.sd.csgobrasil.integracao.repository;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.repository.MovementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class MovementRepositoryTest {

    @Autowired
    MovementRepository repository;

    @Test
    void givenRequest_whenEstadoVendaIsTrue_thenReturnVendedorAndComprador() {
        List<String> nomeCompradorList = new ArrayList<>(List.of("Carlos", "Administrador"));
        List<String> nomeVendedorList = new ArrayList<>(List.of("EstoqueDinamico"));

        List<Report> reports = repository.makeReport();

        for (Report report : reports) {
            if (report.getEstadoVenda()) {
                assertTrue(nomeCompradorList.contains(report.getNomeComprador()));
                assertTrue(nomeVendedorList.contains(report.getNomeVendedor()));
            }
        }
    }

    @Test
    void givenRequest_whenEstadoVendaIsFalse_thenReturnNullToCompradorAndNormalToVendedor() {
        List<String> nomeVendedorList = new ArrayList<>(List.of("EstoqueDinamico", "EstoqueEstatico"
                , "Administrador", "Carlos"));

        List<Report> reports = repository.makeReport();

        for (Report report : reports) {
            if (!report.getEstadoVenda()) {
                assertNull(report.getNomeComprador());
                assertTrue(nomeVendedorList.contains(report.getNomeVendedor()));
            }
        }
    }


}
