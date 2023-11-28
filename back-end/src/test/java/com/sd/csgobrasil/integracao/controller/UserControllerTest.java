package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

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

    @Test
    void givenRequestGET_thenReturnUserListAndStatusOK() throws Exception {
        List<User> users = getUsers();

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        for (int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i), responseObject.get(i));
        }

        assertIterableEquals(users, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @Test
    void givenRequestPOST_whenEmailIsValid_thenReturnCorrectUserInformationAndStatusOK() throws Exception {
        User user = new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente");

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
    void givenValidUser_whenIdIsValid_thenUpdateUserInDatabase() throws Exception {
        User userRight = new User(1L, "Lucas", "9090", "luc@gmail", 1000, null, "cliente");

        MockHttpServletResponse response = mvc
                .perform(
                        put("/user/{id}", userRight.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(userRight).getJson())
                )
                .andReturn().getResponse();

        User userTest = userJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(userRight, userTest);
    }

    @Test
    void givenValidUser_whenIdIsInvalid_thenUpdateUserInDatabase() throws Exception {
        User userRequest = new User(-1L, "Lucas", "9090", "luc@gmail", 1000, null,
                "cliente");

        User userRight = new User(5L, "Lucas", "9090", "luc@gmail", 1000, null,
                "cliente");


        MockHttpServletResponse response = mvc
                .perform(
                        put("/user/{id}", userRequest.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(userRequest).getJson())
                )
                .andReturn().getResponse();

        User userTest = userJson.parse(response.getContentAsString()).getObject();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(userRight, userTest);
    }

    @Test
    void givenValidId_thenDeleteUserInDatabaseAndReturnNoContent() throws Exception {
        Long id = 1L;

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/user/{id}", id)
                )
                .andExpect(content().string(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void givenInvalidId_thenDoNothingInDatabaseAndReturnNoContent() throws Exception {
        Long id = -1L;

        MockHttpServletResponse response = mvc
                .perform(
                        delete("/user/{id}", id)
                )
                .andExpect(content().string(""))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void givenRequestGET_whenIdIsValid_thenReturnUserWithCorrectIdAndStatusOK() throws Exception {
        User user = new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente");

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", user.getId())).andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(user, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    void givenRequestGET_whenIdIsInvalid_thenReturnErrorMessageAndStatusBadRequest() throws Exception {
        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", -1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    void givenRequestPOST_whenUserLoginIsCorrect_thenReturnTrueAndStatusOK() throws Exception {
        UserLogin userLogin = new UserLogin("ca@gmail", "9090");

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
        UserRegister userRegister = new UserRegister("Carlos",
                "ca@gmail", "9090");


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
        User userRight = new User(10L, "Vandaime", "6789", "vandaime@email.com", 0,
                new ArrayList<>(), null);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(userRight).getJson())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertEquals(userRight, responseObject);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void givenRequestPOST_whenUserIsInvalid_thenThrowConstraintViolationExceptionAndReturnStatusBadRequest() throws Exception {
        User user = new User(1L, null, "6789", "vandaime@email.com",
                1000, new ArrayList<>(), "cliente");

        String messageNull = "não deve ser nulo";
        String messageBlank = "não deve estar em branco";

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        String responseMessage = response.getContentAsString();

        assertTrue(responseMessage.contains(messageNull));
        assertTrue(responseMessage.contains(messageBlank));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }


    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente"));
        users.add(new User(2L, "Administrador", "admin", "admin@admin.com", 100000, null, "admin"));
        users.add(new User(3L, "EstoqueDinamico", "admin", "estoqued@admin.com", 100000, null, "admin"));
        users.add(new User(4L, "EstoqueEstatico", "admin", "estoques@admin.com", 100000, null, "admin"));
        return users;
    }
}
