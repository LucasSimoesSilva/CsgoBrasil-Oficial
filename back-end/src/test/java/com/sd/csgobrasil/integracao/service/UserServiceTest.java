package com.sd.csgobrasil.integracao.service;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    void givenRequest_thenReturnAnUserList() {
        List<User> users = getUsers();

        List<User> usersList = service.listUsers();

        assertEquals(users, usersList);
    }

    @Test
    void givenUserRegister_thenTrueIfRegisterExist() {
        UserRegister userRegister = new UserRegister("Carlos", "ca@gmail", "9090");

        boolean check = service.checkIfUserExist(userRegister);
        assertTrue(check);
    }

    @Test
    void givenUserRegister_thenFalseIfRegisterDoNotExist() {
        UserRegister userRegister = new UserRegister("Lucas", "lucas@gmail", "1234");

        boolean check = service.checkIfUserExist(userRegister);
        assertFalse(check);
    }

    @Test
    void givenLogin_whenLoginIsValid_thenReturnTrue() {
        UserLogin userLogin = new UserLogin("ca@gmail", "9090");
        boolean check = service.checkLoginUser(userLogin);
        assertTrue(check);
    }

    @Test
    void givenLogin_whenPasswordIsInvalid_thenReturnFalse() {
        UserLogin userLogin = new UserLogin("ca@gmail", "1234");
        boolean check = service.checkLoginUser(userLogin);
        assertFalse(check);
    }

    @Test
    void givenLogin_whenEmailIsInvalid_thenReturnFalse() {
        UserLogin userLogin = new UserLogin("ca@gmail.gmail", "9090");
        boolean check = service.checkLoginUser(userLogin);
        assertFalse(check);
    }

    @Test
    void givenEmail_whenEmailIsValid_thenReturnTrue() {
        String nomeUser = "Carlos";
        User userRight = new User("ca@gmail", "9090", 200, "cliente");
        User userTest = service.getUserInfo(userRight.getEmail());
        userRight.setNome(nomeUser);
        assertEquals(userRight, userTest);
    }

    @Test
    void givenEmail_whenEmailIsInvalid_thenReturnFalse() {
        User userInfo = service.getUserInfo("ca@gmail.com");
        assertNull(userInfo);
    }

    @Test
    void givenUser_thenAddUserToDatabaseAndReturnUserWithId() {
        User user = new User("Lucas", "123", "lucas@gmail");

        User userRight = new User(5L, "Lucas", "123", "lucas@gmail", 1000, null,
                "cliente");

        User userTest = service.addUser(user);
        assertEquals(userRight, userTest);
    }

    @Test
    void givenUser_whenUserIsInvalid_thenThrowConstraintViolationException() {
        User user = new User(null, "123", "lucas@gmail");
        try {
            service.addUser(user);
        } catch (
                ConstraintViolationException e) {
            assertTrue(e.getMessage().contains("não deve ser nulo"));
            assertTrue(e.getMessage().contains("não deve estar em branco"));
        }
    }

    @Test
    void givenValidIdAndValidUser_thenUpdateUserInDatabaseAndReturnUser() {
        User userRight = new User(1L, "Carlos", "123", "ca@gmail", 1000, new ArrayList<>(), "cliente");

        User userTest = service.updateUser(userRight.getId(), userRight);
        assertEquals(userRight, userTest);
    }

    @Test
    void givenInvalidIdAndValidUser_thenCreateNewUserInDatabaseAndReturnUser() {
        User user = new User("Lucas", "123", "lucas@gmail", 1000, new ArrayList<>(), "cliente");
        User userRight = new User(5L,"Lucas", "123", "lucas@gmail", 1000, new ArrayList<>(), "cliente");

        User userTest = service.updateUser(-1L, user);
        assertEquals(userRight, userTest);
    }

    @Test
    void givenValidId_thenReturnUser() {
        User userRight = new User(1L, "Carlos", "9090", "ca@gmail", 1000, new ArrayList<>(), "cliente");

        User userTest = service.findByUserId(1L);
        assertEquals(userRight, userTest);
    }

    @Test
    void givenInvalidId_thenThrowNoSuchElementException() {
        String messageErrorId = "No value present";

        try {
            service.findByUserId(-1L);
        } catch (NoSuchElementException e) {
            assertEquals(messageErrorId, e.getMessage());
        }
    }

    @Test
    void givenValidOrInvalidId_thenDeleteUserFromDatabase() {
        String messageErrorId = "No value present";
        Long id = 1L;
        service.deleteUser(id);
        try {
            service.findByUserId(-1L);
        } catch (NoSuchElementException e) {
            assertEquals(messageErrorId, e.getMessage());
        }
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente");
        User user2 = new User(2L, "Administrador", "admin", "admin@admin.com", 100000, null, "admin");
        User user3 = new User(2L, "EstoqueDinamico", "admin", "estoqued@admin.com", 100000, null, "admin");
        User user4 = new User(2L, "EstoqueEstatico", "admin", "estoques@admin.com", 100000, null, "admin");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        return users;
    }
}
