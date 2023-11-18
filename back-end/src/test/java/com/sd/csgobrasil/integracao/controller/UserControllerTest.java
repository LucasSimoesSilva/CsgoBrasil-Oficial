package com.sd.csgobrasil.integracao.controller;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureTestDatabase
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class UserControllerTest {

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

    @DisplayName("method listUsers")
    @Test
    void shoudReturnAListOfUsers() throws Exception {
        List<User> users = getUsers();

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        for (int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i).getNome(),responseObject.get(i).getNome());
            assertEquals(users.get(i).getId(),responseObject.get(i).getId());
            assertEquals(users.get(i).getPontos(),responseObject.get(i).getPontos());
            assertEquals(users.get(i).getSenha(),responseObject.get(i).getSenha());
            assertEquals(users.get(i).getEmail(),responseObject.get(i).getEmail());
            assertEquals(users.get(i).getCargo(),responseObject.get(i).getCargo());
        }

        assertIterableEquals(users, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @DisplayName("method getUserInfo")
    @Test
    void shouldReturnAUserByEmail() throws Exception {
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

    @DisplayName("method getUserInfo")
    @Test
    void shouldReturnBlankResponse() throws Exception {
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

    @DisplayName("method findByUserId")
    @Test
    void getUserById() throws Exception {
        User user = new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente");

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", 1L)).andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(user, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
    @DisplayName("method findByUserId")
    @Test
    void getEmptyUserById() throws Exception {
        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", -1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());

    }

    @DisplayName("method checkUser")
    @Test
    void  shoudReturnATrueLoginUser() throws Exception{
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
    @DisplayName("method checkUser")
    @Test
    void  shoudReturnAFalseLoginUser() throws Exception{
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
    @DisplayName("method checkIfUserExist")
    @Test
    void  shoudReturnATrueUserRegister() throws Exception{
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
    @DisplayName("method checkIfUserExist")
    @Test
    void  shoudReturnAFalseUserRegister() throws Exception{
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

    @DisplayName("method saveUser")
    @Test
    void  shouldReturANewUser() throws Exception{
        User user = new User(100L,"Vandaime","6789","vandaime@email.com",0,new ArrayList<>(),  null);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertEquals(user.getNome(), responseObject.getNome());
        assertEquals(1000, responseObject.getPontos());
        assertEquals(user.getEmail(), responseObject.getEmail());
        assertEquals(user.getSenha(), responseObject.getSenha());
        assertEquals("cliente", responseObject.getCargo());
        assertTrue(responseObject.getSkinsUser().isEmpty());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }


    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente"));
        users.add(new User(2L, "Administrador", "admin", "admin@admin.com", 100000, null, "admin"));
        users.add(new User(3L, "EstoqueDinamico", "admin", "estoqued@admin.com", 100000, null, "admin"));
        users.add(new User(4L, "EstoqueEstatico", "admin", "estoques@admin.com", 100000, null, "admin"));
        return users;
    }
}
