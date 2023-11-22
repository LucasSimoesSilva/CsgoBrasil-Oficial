package com.sd.csgobrasil.integracao.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class NumericBooleanDeserializerTest {

    @Test
    void givenTrue_thenReturnValue1() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Integer.class, new NumericBooleanDeserializer());
        mapper.registerModule(module);

        String json = "true";
        Integer value = mapper.readValue(json, Integer.class);
        assertEquals(1, value);
    }

    @Test
    void givenFalse_thenReturnValue0() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Integer.class, new NumericBooleanDeserializer());
        mapper.registerModule(module);

        String json = "false";
        Integer value = mapper.readValue(json, Integer.class);
        assertEquals(0, value);
    }

    @Test
    void givenJson_whenJsonIsNeitherFalseNorTrue_thenReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Integer.class, new NumericBooleanDeserializer());
        mapper.registerModule(module);

        String json = "null";
        Integer value = mapper.readValue(json, Integer.class);
        assertNull(value);
    }
}
