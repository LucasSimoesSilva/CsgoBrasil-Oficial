package com.sd.csgobrasil.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Movement{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenda;
    @NotNull
    private Long idVendedor;
    @NotNull
    private Long idComprador;
    @NotNull
    private Long idSkin;

    // O estadoVenda quando false indica que a Skin foi anunciada, mas não comprada
    // Quando for true, os pontos serão enviados para o vendedor
    @NotNull
    private boolean estadoVenda;
    @NotNull
    private int pontos;


    public Movement( Long idVenda, Long idVendedor, Long idComprador, Long idSkin, boolean estadoVenda, int pontos) {
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.idSkin = idSkin;
        this.estadoVenda = estadoVenda;
        this.pontos = pontos;
        this.idVenda = idVenda;
    }

    public Movement(Long idVendedor, Long idSkin, boolean estadoVenda, int pontos) {
        this.idVendedor = idVendedor;
        this.idSkin = idSkin;
        this.estadoVenda = estadoVenda;
        this.pontos = pontos;
    }

    public Movement() {
    }

    @Override
    public String toString() {
        return "Movement{" +
                "idVenda=" + idVenda +
                ", idVendedor=" + idVendedor +
                ", idComprador=" + idComprador +
                ", idSkin=" + idSkin +
                ", estadoVenda=" + estadoVenda +
                ", pontos=" + pontos +
                '}';
    }
}
