package com.sd.csgobrasil.util;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"idSkin"})
public class SkinWithStateImpl implements SkinWithState {

    private Long idSkin;
    private String nome;
    private String arma;
    private Integer preco;
    private String raridade;
    private String imagem;
    private boolean estadoVenda;
    private boolean isInMovement;
    private Long idVenda;

    public SkinWithStateImpl(Long idSkin, String nome, String arma, Integer preco, String raridade,
                             String imagem, boolean estadoVenda, boolean isInMovement, Long idVenda) {
        this.idSkin = idSkin;
        this.nome = nome;
        this.arma = arma;
        this.preco = preco;
        this.raridade = raridade;
        this.imagem = imagem;
        this.estadoVenda = estadoVenda;
        this.isInMovement = isInMovement;
        this.idVenda = idVenda;
    }

    @Override
    public Long getIdSkin() {
        return this.idSkin;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public String getArma() {
        return this.arma;
    }

    @Override
    public Integer getPreco() {
        return this.preco;
    }

    @Override
    public String getRaridade() {
        return this.raridade;
    }

    @Override
    public String getImagem() {
        return this.imagem;
    }

    @Override
    public boolean getEstadoVenda() {
        return this.estadoVenda;
    }

    @Override
    public Integer getIsInMovement() {
        if (this.isInMovement){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public Long getIdVenda() {
        return this.idVenda;
    }
}
