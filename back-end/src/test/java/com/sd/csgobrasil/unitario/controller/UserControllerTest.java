package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Autowired
    private JacksonTester<List<User>> userListJson;

    @Autowired
    private JacksonTester<User> userJson;

    @Autowired
    private JacksonTester<UserLogin> userLoginJson;

    @Autowired
    private JacksonTester<Boolean> userLoginBoolJson;

    @Autowired
    private JacksonTester<Boolean> userRegisterBoolJson;

    @Autowired
    private JacksonTester<UserRegister> userRegisterJson;

    List<Skin> skinsList;
    List<User> userList;

    @BeforeAll
    void beforeAll() {
        skinsList = getSkins();
        userList = getUsers();
    }

    @Test
    void givenRequestGET_thenReturnUserListAndStatusOK() throws Exception {

        when(service.listUsers()).thenReturn(userList);

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertTrue(responseObject.size() > 1);
        assertIterableEquals(userList, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestGET_whenHaveNotUsers_thenReturnEmptyUserListAndStatusOK() throws Exception {
        List<User> users = new ArrayList<>();

        when(service.listUsers()).thenReturn(users);

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();
        assertIterableEquals(users, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertThat(users).isEmpty();
    }

    @Test
    void givenRequestPOST_whenEmailIsValid_thenReturnCorrectUserInformationAndStatusOK() throws Exception {
        User user = new User(1L, "Mauricio", "1234", "email@email.com", 11, skinsList, "cargo");

        when(service.getUserInfo("email@email.com")).thenReturn(user);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/info")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(user.getEmail())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(user, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenEmailIsInvalid_thenReturnNothingAndStatusOk() throws Exception {
        when(service.getUserInfo("email@email.com")).thenReturn(null);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/info")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("email@email.com")
                )
                .andExpect(content().string(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestGET_whenIdIsValid_thenReturnUserWithCorrectIdAndStatusOK() throws Exception {
        User user = new User(1L, "Mauricio", "1234", "email@email.com", 11, skinsList, "cargo");

        when(service.findByUserId(1L)).thenReturn(user);

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", 1L)).andReturn().getResponse();
        User responseObject = userJson.parse(response.getContentAsString()).getObject();
        assertThat(responseObject).isNotNull();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    void givenRequestGET_whenIdIsInvalid_thenReturnErrorMessageAndStatusBadRequest() throws Exception {
        when(service.findByUserId(-1L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", -1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    void givenRequestPOST_whenUserLoginIsCorrect_thenReturnTrueAndStatusOK() throws Exception {
        UserLogin userLogin = new UserLogin("email@email.com", "123");

        when(service.checkLoginUser(userLogin)).thenReturn(true);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userLoginJson.write(userLogin).getJson())
                )
                .andReturn().getResponse();

        Boolean responseObject = userLoginBoolJson.parse(response.getContentAsString()).getObject();

        assertTrue(responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserLoginIsIncorrect_thenReturnFalseAndStatusOK() throws Exception {
        UserLogin userLogin = new UserLogin("email@email.com", "123");

        when(service.checkLoginUser(userLogin)).thenReturn(false);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userLoginJson.write(userLogin).getJson())
                )
                .andReturn().getResponse();

        Boolean responseObject = userLoginBoolJson.parse(response.getContentAsString()).getObject();

        assertFalse(responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserRegisterExist_thenReturnTrueAndStatusOK() throws Exception {
        UserRegister userRegister = new UserRegister("vandaime",
                "vandaime@gmail.com", "123");

        when(service.checkIfUserExist(userRegister)).thenReturn(true);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRegisterJson.write(userRegister).getJson())
                )
                .andReturn().getResponse();

        Boolean responseObject = userRegisterBoolJson.parse(response.getContentAsString()).getObject();

        assertTrue(responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserRegisterNotExist_thenReturnFalseAndStatusOK() throws Exception {
        UserRegister userRegister = new UserRegister("vandaime",
                "vandaime@gmail.com", "123");

        when(service.checkIfUserExist(userRegister)).thenReturn(false);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRegisterJson.write(userRegister).getJson())
                )
                .andReturn().getResponse();

        Boolean responseObject = userLoginBoolJson.parse(response.getContentAsString()).getObject();

        assertFalse(responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserIsValid_thenReturnUserWithIdAndStatusCREATED() throws Exception {
        User user = new User(1L, "Vandaime", "6789", "vandaime@email.com",
                1000, skinsList, "cliente");

        when(service.addUser(any(User.class))).thenReturn(user);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertEquals(user, responseObject);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserIsInvalid_thenThrowConstraintViolationExceptionAndReturnStatusBadRequest() throws Exception {
        User user = new User(1L, null, "6789", "vandaime@email.com",
                1000, skinsList, "cliente");

        when(service.addUser(any(User.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        String responseObject = response.getContentAsString();

        assertEquals("", responseObject);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestPUTAndUser_thenReturnUserUpdated() throws Exception {
        User user = new User(1L, "Vandaime", "6789", "vandaime@email.com",
                1000, skinsList, "cliente");

        when(service.updateUser(1L, user)).thenReturn(user);

        MockHttpServletResponse response = mvc
                .perform(
                        put("/user/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(user, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void givenRequestPUTAndUser_whenUserIsInvalid_thenThrowConstraintViolationExceptionReturnStatusBadRequest() throws Exception {
        User user = new User(1L, null, "6789", "vandaime@email.com",
                1000, skinsList, "cliente");

        when(service.updateUser(1L, user)).thenThrow(new ConstraintViolationException(new HashSet<>()));

        MockHttpServletResponse response = mvc
                .perform(
                        put("/user/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        String responseObject = response.getContentAsString();

        assertEquals("", responseObject);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void givenRequestDELETE_whenIdIsValid_thenReturnStatusNoContent() throws Exception {
        doNothing().when(service).deleteUser(1L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/user/{id}", 1L)
                )
                .andReturn().getResponse();

        verify(service, times(1)).deleteUser(1L);

        String responseMessage = response.getContentAsString();

        assertEquals("", responseMessage);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void givenRequestDELETE_whenIdIsInvalid_thenThrowNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException()).when(service).deleteUser(0L);

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/user/{id}", 0L)
                )
                .andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
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
        users.add(new User(1L, "Mauricio", "1234", "email@email.com", 11, skinsList, "cargo"));
        users.add(new User(1L, "Mauro", "3456", "email2@email.com", 10, skinsList, "cargo2"));
        users.add(new User(1L, "Mario", "2345", "email3@email.com", 9, skinsList, "cargo3"));
        users.add(new User(1L, "Vandaime", "6789", "vandaime@email.com", 12, skinsList, ""));
        return users;
    }
}
