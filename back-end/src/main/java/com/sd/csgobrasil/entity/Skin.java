package com.sd.csgobrasil.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EqualsAndHashCode(of = {"id","nome"})
public class Skin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    @NotBlank
    private String arma;

    @NotNull
    private int preco;

    @NotNull
    @NotBlank
    private String raridade;

    @NotNull
    @NotBlank
    private String imagem;

    public Skin() {
    }

    public Skin(String nome, String arma, int preco, String raridade, String imagem) {
        this.nome = nome;
        this.arma = arma;
        this.preco = preco;
        this.raridade = raridade;
        this.imagem = imagem;
    }
}
