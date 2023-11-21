package com.sd.csgobrasil.unitario.mapper;

import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NumericBooleanDeserializerTest {

    @Test
    void testDeserializeTrue() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getText()).thenReturn("true");

        NumericBooleanDeserializer seuDeserializer = new NumericBooleanDeserializer();
        Integer result = seuDeserializer.deserialize(mockParser, mock(DeserializationContext.class));

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void testDeserializeFalse() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getText()).thenReturn("false");

        NumericBooleanDeserializer seuDeserializer = new NumericBooleanDeserializer();
        Integer result = seuDeserializer.deserialize(mockParser, mock(DeserializationContext.class));

        assertEquals(Integer.valueOf(0), result);
    }

    @Test
    void testDeserializeNullText() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getText()).thenReturn(null);

        NumericBooleanDeserializer seuDeserializer = new NumericBooleanDeserializer();
        assertNull(seuDeserializer.deserialize(mockParser, mock(DeserializationContext.class)));
    }

    @Test
    void testDeserializeOtherText() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getText()).thenReturn("other");

        NumericBooleanDeserializer seuDeserializer = new NumericBooleanDeserializer();
        assertNull(seuDeserializer.deserialize(mockParser, mock(DeserializationContext.class)));
    }
}
