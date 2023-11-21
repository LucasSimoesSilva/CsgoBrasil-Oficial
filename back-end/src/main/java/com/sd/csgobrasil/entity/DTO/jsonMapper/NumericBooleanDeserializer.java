package com.sd.csgobrasil.entity.DTO.jsonMapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class NumericBooleanDeserializer extends JsonDeserializer {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        if ("true".equals(p.getText())) {
            return 1;
        }
        if ("false".equals(p.getText())) {
            return 0;
        }
        return null;
    }
}
