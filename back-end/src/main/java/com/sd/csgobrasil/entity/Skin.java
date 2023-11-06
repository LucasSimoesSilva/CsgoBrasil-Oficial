package com.sd.csgobrasil.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Skin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String arma;
    private int preco;
    private String raridade;
    private String imagem;

    public Skin() {
    }

}
