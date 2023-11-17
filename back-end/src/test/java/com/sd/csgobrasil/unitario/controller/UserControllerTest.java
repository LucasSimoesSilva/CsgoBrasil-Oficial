package com.sd.csgobrasil.unitario.controller;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.NoSuchElementException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;


    @Autowired
    private JacksonTester<List<User>> userListJson;

    @Autowired
    private JacksonTester<User> userJson;

    @DisplayName("method listUsers")
    @Test
    void shoudReturnAListOfUsers() throws Exception {

        when(service.listUsers()).thenReturn(getUsers());

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotEmpty();
        assertIterableEquals(getUsers(), responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(responseObject.size() > 1);
    }

    @DisplayName("method listUsers")
    @Test
    void shoudReturnAEmptyList() throws Exception {
        List<User> users = new ArrayList<>();

        when(service.listUsers()).thenReturn(users);

        MockHttpServletResponse response = mvc.
                perform(get("/user/users")).andReturn().getResponse();

        List<User> responseObject = userListJson.parse(response.getContentAsString()).getObject();
        User userInfo = new User();
        assertIterableEquals(users, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertThat(users).isEmpty();
    }
    @DisplayName("method getUserInfo")
    @Test
    void shouldReturnAUserByEmail() throws Exception {

        User user = new User(1L, "Mauricio", "1234", "email@email.com", 11, getSkins(), "cargo");

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

    // negative

    @DisplayName("method findByUserId")
    @Test
    void getUserById() throws Exception {
        User user = new User(1L, "Mauricio", "1234", "email@email.com", 11, getSkins(), "cargo");

        when(service.findByUserId(1L)).thenReturn(user);

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", 1L)).andReturn().getResponse();
        User responseObject = userJson.parse(response.getContentAsString()).getObject();
        assertThat(responseObject).isNotNull();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
    @DisplayName("method findByUserId")
    @Test
    void getEmptyUserById() throws Exception {
        when(service.findByUserId(-1L)).thenThrow(new NoSuchElementException());

        MockHttpServletResponse response = mvc.
                perform(get("/user/{id}", -1L)).andReturn().getResponse();

        String messageInvalidId = "Invalid Id";

        assertEquals(messageInvalidId, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());

    }
    @Test
    void postCheckLogin() throws Exception{
        User user = new User(1L, "Mauricio", "1234",
                "email@email.com", 11, getSkins(), "cargo");


        when(service.findByUserId(1L)).thenReturn(user);

        MockHttpServletResponse response = mvc
                .perform(
                        post("/user/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson.write(user).getJson())
                )
                .andReturn().getResponse();

        User responseObject = userJson.parse(response.getContentAsString()).getObject();

        assertThat(responseObject).isNotNull();
        assertEquals(user, responseObject);
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }


    private static List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        skins.add(new Skin(1L, "Dragon Lore", "AWP", 100, "Nova de Guerra", ""));
        skins.add(new Skin(2L, "Dragon Red", "Pistol", 100, "Velha de Guerra", ""));
        skins.add(new Skin(3L, "Dragon Blue", "AWP", 100, "Veterana", ""));
        return skins;
    }

    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Mauricio", "1234", "email@email.com", 11, getSkins(), "cargo"));
        users.add(new User(1L, "Mauro", "3456", "email2@email.com", 10, getSkins(), "cargo2"));
        users.add(new User(1L, "Mario", "2345", "email3@email.com", 9, getSkins(), "cargo3"));
        users.add(new User(1L, "Vandaime", "6789", "vandaime@email.com", 12, getSkins(), ""));
        return users;
    }
}
