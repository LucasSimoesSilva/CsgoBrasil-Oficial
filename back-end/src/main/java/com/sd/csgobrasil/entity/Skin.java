package com.sd.csgobrasil.entity;


import jakarta.persistence.*;
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
    private String nome;

    @NotNull
    private String arma;

    @NotNull
    private int preco;

    @NotNull
    private String raridade;

    @NotNull
    private String imagem;

    public Skin() {
    }

}
