package com.sd.csgobrasil.integracao.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class NumericBooleanSerializerTest {

    @Test
    void givenValue1_thenReturnTrue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Integer.class, new NumericBooleanSerializer());
        mapper.registerModule(module);

        Integer value = 1;
        String json = mapper.writeValueAsString(value);
        assertEquals("true", json);
    }

    @Test
    void givenValue0_thenReturnFalse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Integer.class, new NumericBooleanSerializer());
        mapper.registerModule(module);

        Integer value = 0;
        String json = mapper.writeValueAsString(value);
        assertEquals("false", json);
    }
}
