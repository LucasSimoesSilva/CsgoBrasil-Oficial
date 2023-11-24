package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.repository.UserRepository;
import com.sd.csgobrasil.service.SkinService;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import com.sd.csgobrasil.util.UserSkinImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserSkinServiceTest {

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private SkinService skinService;

    @Autowired
    private UserSkinService service;

    Long idSkin;
    Long validUserId;
    Long invalidUserId;

    List<Skin> skinList;

    @BeforeAll
    void beforeAll() {
        skinList = getSkins();
        invalidUserId = -2L;
        validUserId = 2L;
        idSkin = 1L;
    }

    @Test
    void givenSkinIdAndUserId_thenAddSkinFromUser() {
        Long skinUserId = idSkin;
        Long userId = validUserId;

        doNothing().when(userRepository).addSkinFromUser(skinUserId, userId);

        service.addSkinFromUser(skinUserId, userId);

        verify(userRepository, times(1)).addSkinFromUser(skinUserId, userId);
    }

    @Test
    void givenSkinIdAndUserId_thenDeleteSkinFromUser() {
        Long skinUserId = idSkin;
        Long userId = validUserId;

        doNothing().when(userRepository).deleteSkinFromUser(skinUserId, userId);

        service.deleteSkinFromUser(skinUserId, userId);

        verify(userRepository, times(1)).deleteSkinFromUser(skinUserId, userId);
    }

    @Test
    void givenUserId_thenReturnSkinsFromUser() {
        Long userId = validUserId;
        List<UserSkin> userSkins = getUserSkins();
        List<Skin> skinsRight = skinList;

        when(userRepository.listSkinsFromUser(userId)).thenReturn(userSkins);

        for (int i = 0; i < userSkins.size(); i++) {
            when(skinService.findBySkinId(userSkins.get(i).getIdSkin())).thenReturn(skinsRight.get(i));
        }

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isNotEmpty();
        assertEquals(skinsRight, skinsTest);
    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptyList() {
        Long userId = invalidUserId;
        List<UserSkin> userSkins = new ArrayList<>();
        List<Skin> skinsRight = skinList;

        when(userRepository.listSkinsFromUser(userId)).thenReturn(userSkins);

        for (int i = 0; i < userSkins.size(); i++) {
            when(skinService.findBySkinId(userSkins.get(i).getIdSkin())).thenReturn(skinsRight.get(i));
        }

        List<Skin> skinsTest = service.listSkinsFromUser(userId);

        assertThat(skinsTest).isEmpty();
    }

    @Test
    void givenUserId_thenReturnSkinWithStateList() {
        List<SkinWithState> skinWithStateListRight = new ArrayList<>();
        skinWithStateListRight.add(new SkinWithStateImpl(1L, "Dragon Lore", "AWP", 100,
                "Nova de Guerra", "", true, true, 1L));
        skinWithStateListRight.add(new SkinWithStateImpl(2L, "Dragon Red", "Pistol", 100,
                "Velha de Guerra", "", true, true, 2L));

        Long userId = validUserId;

        when(userRepository.listSkinsWithStateFromUser(userId)).thenReturn(skinWithStateListRight);

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);
        assertEquals(skinWithStateListRight, skinWithStateListTest);

    }

    @Test
    void givenUserId_whenUserIdIsInvalid_thenReturnEmptySkinWithStateList() {
        List<SkinWithState> skinWithStateListRight = new ArrayList<>();

        Long userId = invalidUserId;

        when(userRepository.listSkinsWithStateFromUser(userId)).thenReturn(skinWithStateListRight);

        List<SkinWithState> skinWithStateListTest = service.listSkinsWithStateFromUser(userId);
        assertEquals(skinWithStateListRight, skinWithStateListTest);

    }

    private List<UserSkin> getUserSkins() {
        List<UserSkin> userSkins = new ArrayList<>();
        userSkins.add(new UserSkinImpl(1L, 2L));
        userSkins.add(new UserSkinImpl(2L, 2L));
        return userSkins;
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        return skins;

    }

}
