package com.sd.csgobrasil.entity.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"nome","email"})
public class UserRegister {
    private String nome;
    private String email;
    private String senha;
}
