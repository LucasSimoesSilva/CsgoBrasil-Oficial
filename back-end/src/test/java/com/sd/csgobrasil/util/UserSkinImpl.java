package com.sd.csgobrasil.util;

import com.sd.csgobrasil.entity.DTO.UserSkin;

public class UserSkinImpl implements UserSkin {

    private Long idSkin;
    private Long idUser;

    public UserSkinImpl(Long idSkin, Long idUser) {
        this.idSkin = idSkin;
        this.idUser = idUser;
    }

    @Override
    public Long getIdSkin() {
        return this.idSkin;
    }

    @Override
    public Long getIdUser() {
        return this.idUser;
    }
}
