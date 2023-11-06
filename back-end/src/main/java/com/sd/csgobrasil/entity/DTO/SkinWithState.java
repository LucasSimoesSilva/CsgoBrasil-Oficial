package com.sd.csgobrasil.entity.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanDeserializer;
import com.sd.csgobrasil.entity.DTO.jsonMapper.NumericBooleanSerializer;

public interface SkinWithState {

    Long getIdSkin();
    String getNome();
    String getArma();
    Integer getPreco();
    String getRaridade();
    String getImagem();
    boolean getEstadoVenda();
    @JsonSerialize(using = NumericBooleanSerializer.class)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    Integer getIsInMovement();
    Long getIdVenda();
}
