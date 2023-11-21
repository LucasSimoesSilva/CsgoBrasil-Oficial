package com.sd.csgobrasil.unitario.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
class NumericBooleanSerializerTest {

    @Test
    void testSerializeTrue() throws IOException {
        NumericBooleanSerializer suaClasse = new NumericBooleanSerializer();
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        suaClasse.serialize(1, mockGenerator, mockProvider);

        verify(mockGenerator).writeBoolean(true);
    }

    @Test
    void testSerializeFalse() throws IOException {
        NumericBooleanSerializer suaClasse = new NumericBooleanSerializer();
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        suaClasse.serialize(0, mockGenerator, mockProvider);

        verify(mockGenerator).writeBoolean(false);
    }

    @Test
    void testSerializeOtherValue() throws IOException {
        NumericBooleanSerializer suaClasse = new NumericBooleanSerializer();
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        suaClasse.serialize(42, mockGenerator, mockProvider);

        verify(mockGenerator, times(0)).writeBoolean(false);
    }
}
