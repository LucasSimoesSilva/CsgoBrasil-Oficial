package com.sd.csgobrasil.entity.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email","senha"})
public class UserLogin {
    private String email;
    private String senha;
}
