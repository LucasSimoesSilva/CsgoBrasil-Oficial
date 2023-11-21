package com.sd.csgobrasil.integracao.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class NumericBooleanDeserializerTest {

    NumericBooleanDeserializer deserializer;

    @Test
    void testIntegration() throws Exception {
        deserializer = new NumericBooleanDeserializer();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Integer.class, deserializer);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        String jsonString = "{\"yourField\":\"true\"}";

        Integer seuObjeto = objectMapper.readValue(jsonString, Integer.class);

        assertEquals(Integer.valueOf(1), seuObjeto);
    }
}
