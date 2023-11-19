package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.repository.UserRepository;
import com.sd.csgobrasil.service.UserService;
import com.sd.csgobrasil.service.UserSkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        List<User> users = getUsers();

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

        assertThat(usersList).isEmpty();
        assertEquals(users,usersList);
    }

    @Test
    void givenUserRegister_thenTrueIfRegisterExist(){
        UserRegister userRegister = new UserRegister("Carlos","ca@gmail","1234");
        when(repository.existsUserByEmailOrNome(userRegister.getEmail(), userRegister.getNome())).thenReturn(true);

        boolean check = service.checkIfUserExist(userRegister);
        assertTrue(check);
    }

    @Test
    void givenUserRegister_thenFalseIfRegisterDoNotExist(){
        UserRegister userRegister = new UserRegister("Carlos","ca@gmail","1234");
        when(repository.existsUserByEmailOrNome(userRegister.getEmail(), userRegister.getNome())).thenReturn(false);

        boolean check = service.checkIfUserExist(userRegister);
        assertFalse(check);
    }

    @Test
    void givenLogin_whenLoginIsValid_thenReturnTrue (){
        UserLogin userLogin = new UserLogin("ca@gmail.com","1234");
        when(repository.existsUserByEmailAndSenha(userLogin.getEmail(), userLogin.getSenha())).thenReturn(true);
        boolean check = service.checkLoginUser(userLogin);
        assertTrue(check);
    }
    @Test
    void givenLogin_whenLoginIsInvalid_thenReturnFalse () {
        UserLogin userLogin = new UserLogin("ca@gmail.com", "1234");
        when(repository.existsUserByEmailAndSenha(userLogin.getEmail(), userLogin.getSenha())).thenReturn(false);
        boolean check = service.checkLoginUser(userLogin);
        assertFalse(check);
    }
    @Test
    void givenEmail_whenEmailIsValid_thenReturnTrue(){
        User userEmail = new User("ca@gmail.com","1234",9090,"cliente");
        when(repository.findUsersByEmail(userEmail.getEmail())).thenReturn(userEmail);
        User userInfo = service.getUserInfo(userEmail.getEmail());
        assertEquals(userEmail,userInfo);
    }
    @Test
    void givenEmail_whenEmailIsInvalid_thenReturnFalse(){
        when(repository.findUsersByEmail("cal@gmail.com")).thenReturn(null);
        User userInfo = service.getUserInfo("cal@gmail.com");
        assertNull(userInfo);
    }
    @Test
    void givenUser_thenAddUserToDatabaseAndReturnUserWithId(){
        User user = new User("Carlos","123","ca@gmail");

        User userRight = new User(1L,"Carlos","123","ca@gmail",1000,null, "cliente");

        when(repository.save(user)).thenReturn(userRight);

        User userTest = service.addUser(user);
        assertEquals(userRight,userTest);
    }

    @Test
    void givenValidIdAndValidUser_thenUpdateUserInDatabaseAndReturnUser(){
        User userRight = new User(1L,"Carlos","123","ca@gmail",1000,new ArrayList<>(), "cliente");

        when(repository.save(userRight)).thenReturn(userRight);

        User userTest = service.updateUser(userRight.getId(),userRight);
        assertEquals(userRight,userTest);
    }

    @Test
    void givenInvalidIdAndValidUser_thenCreateNewUserInDatabaseAndReturnUser(){
        User userRight = new User(5L,"Carlos","123","ca@gmail",1000,new ArrayList<>(), "cliente");

        when(repository.save(userRight)).thenReturn(userRight);

        User userTest = service.updateUser(-1L,userRight);
        assertEquals(userRight,userTest);
    }

    @Test
    void givenValidId_thenReturnUser(){
        User userRight = new User(1L,"Carlos","123","ca@gmail",1000,new ArrayList<>(), "cliente");

        when(repository.findById(userRight.getId())).thenReturn(Optional.of(userRight));

        User userTest = service.findByUserId(1L);
        assertEquals(userRight,userTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException(){
        doThrow(new NoSuchElementException("Invalid id")).when(repository).findById(-1L);

        try {
            service.findByUserId(-1L);
        }catch (NoSuchElementException e){
            assertEquals("Invalid id",e.getMessage());
        }
    }

    @Test
    void givenValidOrInvalidId_thenDeleteUserFromDatabase(){
        Long id = 1L;
        doNothing().when(repository).deleteById(id);
        service.deleteUser(id);
        verify(repository, times(1)).deleteById(id);
    }

    private List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;

    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User(1L,"Carlos","9090","ca@gmail",200,null,"cliente");
        User user2 = new User(2L,"Administrador","admin","admin@admin.com",100000,null,"admin");
        users.add(user2);
        users.add(user1);
        return users;
    }
}
