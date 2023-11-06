package com.sd.csgobrasil.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkinMovement {

    private Long idVenda;
    private Long idVendedor;
    private boolean estadoVenda;
    private String nome;
    private String arma;
    private int preco;
    private String raridade;
    private String imagem;
}
