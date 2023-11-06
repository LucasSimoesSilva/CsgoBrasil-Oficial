package com.sd.csgobrasil.entity.DTO.jsonMapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class NumericBooleanSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o.equals(1)) {
            jsonGenerator.writeBoolean(true);
        }else if(o.equals(0)){
            jsonGenerator.writeBoolean(false);
        }
    }
}
