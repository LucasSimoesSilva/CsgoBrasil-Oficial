package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.repository.UserRepository;
import com.sd.csgobrasil.service.SkinService;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import com.sd.csgobrasil.util.UserSkinImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class UserSkinServiceTest {

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private SkinService skinService;

    @Autowired
    private UserSkinService service;

    @Test
    void givenSkinIdAndUserId_thenAddSkinFromUser(){
        Long skinsPossuidasId = 1L;
        Long userId = 2L;

        doNothing().when(userRepository).addSkinFromUser(skinsPossuidasId, userId);

        service.addSkinFromUser(skinsPossuidasId,userId);

        verify(userRepository, times(1)).addSkinFromUser(skinsPossuidasId,userId);
    }

    @Test
    void givenSkinIdAndUserId_thenDeleteSkinFromUser(){
        Long skinsPossuidasId = 1L;
        Long userId = 2L;

        doNothing().when(userRepository).deleteSkinFromUser(skinsPossuidasId, userId);

        service.deleteSkinFromUser(skinsPossuidasId,userId);

        verify(userRepository, times(1)).deleteSkinFromUser(skinsPossuidasId,userId);
    }

    @Test
    void givenUserId_thenReturnSkinsFromUser(){
        Long userId = 2L;
        List<UserSkin> userSkins = getUserSkins();
        List<Skin> skinsRight = getSkins();

        when(userRepository.listSkinsFromUser(userId)).thenReturn(userSkins);

        for (int i = 0; i < userSkins.size(); i++) {
            when(skinService.findBySkinId(userSkins.get(i).getIdSkin())).thenReturn(skinsRight.get(i));
        }

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isNotEmpty();
        assertEquals(skinsRight,skinsTest);
    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptyList(){
        Long userId = -2L;
        List<UserSkin> userSkins = new ArrayList<>();
        List<Skin> skinsRight = getSkins();

        when(userRepository.listSkinsFromUser(userId)).thenReturn(userSkins);

        for (int i = 0; i < userSkins.size(); i++) {
            when(skinService.findBySkinId(userSkins.get(i).getIdSkin())).thenReturn(skinsRight.get(i));
        }

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isEmpty();
    }

    @Test
    void givenUserId_thenReturnSkinWithStateList(){
        List<SkinWithState> skinWithStateListRight = new ArrayList<>();
        skinWithStateListRight.add(new SkinWithStateImpl(1L, "Dragon Lore", "AWP", 100,
                "Nova de Guerra", "",true,true,1L));
        skinWithStateListRight.add(new SkinWithStateImpl(2L, "Dragon Red", "Pistol", 100,
                "Velha de Guerra", "",true,true,2L));

        Long userId = 2L;

        when(userRepository.listSkinsWithStateFromUser(userId)).thenReturn(skinWithStateListRight);

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);
        assertEquals(skinWithStateListRight,skinWithStateListTest);

    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptySkinWithStateList(){
        List<SkinWithState> skinWithStateListRight = new ArrayList<>();

        Long userId = -2L;

        when(userRepository.listSkinsWithStateFromUser(userId)).thenReturn(skinWithStateListRight);

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);
        assertEquals(skinWithStateListRight,skinWithStateListTest);

    }

    private List<UserSkin> getUserSkins() {
        List<UserSkin> userSkins = new ArrayList<>();
        userSkins.add(new UserSkinImpl(1L,2L));
        userSkins.add(new UserSkinImpl(2L,2L));
        return userSkins;
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        return skins;

    }

}
