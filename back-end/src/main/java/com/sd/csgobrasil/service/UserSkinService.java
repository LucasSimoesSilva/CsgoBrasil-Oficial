package com.sd.csgobrasil.service;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSkinService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private SkinService skinService;

    public List<SkinWithState> listSkinsWithStateFromUser(Long idUser) {
        List<SkinWithState> skinWithStates = userRepository.listSkinsWithStateFromUser(idUser);
        List<SkinWithState> skins = new ArrayList<>();
        List<Long> idsList = new ArrayList<>();

        for (SkinWithState skinWithState : skinWithStates) {
            if(!idsList.contains(skinWithState.getIdSkin())){
                idsList.add(skinWithState.getIdSkin());
                skins.add(skinWithState);
            }
        }
        return skins;
    }

    public void addSkinFromUser(Long skinsPossuidasId, Long userId) {
        userRepository.addSkinFromUser(skinsPossuidasId, userId);
    }

    public void deleteSkinFromUser(Long skinsPossuidasId, Long userId) {
        userRepository.deleteSkinFromUser(skinsPossuidasId, userId);
    }

    public List<Skin> listSkinsFromUser(Long userId) {
        List<UserSkin> userSkins = userRepository.listSkinsFromUser(userId);
        List<Skin> skins = new ArrayList<>();
        for (UserSkin userSkin : userSkins) {
            Skin skinAdd = skinService.findBySkinId(userSkin.getIdSkin());
            skins.add(skinAdd);
        }
        return skins;
    }
}
