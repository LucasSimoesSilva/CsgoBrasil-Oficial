package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.repository.UserRepository;
import com.sd.csgobrasil.service.UserService;
import com.sd.csgobrasil.service.UserSkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository repository;


    @MockBean
    private UserSkinService userSkinService;

    @Autowired
    private UserService service;

    @Test
    void givenRequest_thenReturnAnUserList(){
        List<User> users = new ArrayList<>();
        User user1 = new User(1L,"Carlos","9090","ca@gmail",200,null,"cliente");
        User user2 = new User(2L,"Administrador","admin","admin@admin.com",100000,null,"admin");
        users.add(user2);
        users.add(user1);

        for (User user : users) {
            when(userSkinService.listSkinsFromUser(user.getId())).thenReturn(getSkins());
        }
        when(repository.findAll()).thenReturn(users);

        List<User> usersList = service.listUsers();

        assertEquals(users,usersList);
    }

    @Test
    void givenRequest_thenReturnAnEmptyUserList(){
        List<User> users = new ArrayList<>();

        when(userSkinService.listSkinsFromUser(null)).thenReturn(null);
        when(repository.findAll()).thenReturn(users);

        List<User> usersList = service.listUsers();

        assertEquals(users,usersList);
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }
}
