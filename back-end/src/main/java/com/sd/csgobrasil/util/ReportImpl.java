package com.sd.csgobrasil.util;

import com.sd.csgobrasil.entity.DTO.Report;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"idVenda"})
public class ReportImpl implements Report {

    Long idVenda;
    String nomeVendedor;
    String nomeComprador;
    String nomeSkin;
    boolean estadoVenda;
    int pontos;

    public ReportImpl(Long idVenda, String nomeVendedor, String nomeComprador, String nomeSkin,
                      boolean estadoVenda, int pontos) {
        this.idVenda = idVenda;
        this.nomeVendedor = nomeVendedor;
        this.nomeComprador = nomeComprador;
        this.nomeSkin = nomeSkin;
        this.estadoVenda = estadoVenda;
        this.pontos = pontos;
    }

    @Override
    public Long getIdVenda() {
        return this.idVenda;
    }

    @Override
    public String getNomeVendedor() {
        return this.nomeVendedor;
    }

    @Override
    public String getNomeComprador() {
        return this.nomeComprador;
    }

    @Override
    public String getNomeSkin() {
        return this.nomeSkin;
    }

    @Override
    public boolean getEstadoVenda() {
        return this.estadoVenda;
    }

    @Override
    public int getPontos() {
        return this.pontos;
    }
}