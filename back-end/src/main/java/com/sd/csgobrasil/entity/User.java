package com.sd.csgobrasil.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    private String cargo;
    private int pontos;
    @Column(unique = true)
    private String email;
    private String senha;

    @OneToMany
    private List<Skin> skinsUser;

    public User(Long id, String nome, String senha, String email, int pontos, List<Skin> skinsUser, String cargo) {
        this.id = id;
        this.nome = nome;
        this.pontos = pontos;
        this.email = email;
        this.skinsUser = skinsUser;
        this.senha = senha;
        this.cargo = cargo;
    }

    public User(String nome, String senha, String email, int pontos, List<Skin> skinsUser, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
        this.pontos = pontos;
        this.email = email;
        this.senha = senha;
        this.skinsUser = skinsUser;
    }

    public User(String email, String senha, int pontos, String cargo) {
        this.email = email;
        this.senha = senha;
        this.pontos = pontos;
        this.cargo = cargo;
    }

    public User() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.email);
        hash = 67 * hash + Objects.hashCode(this.senha);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.senha, other.senha);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cargo='" + cargo + '\'' +
                ", pontos=" + pontos +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", skinsPossuidas=" + skinsUser +
                '}';
    }
}
