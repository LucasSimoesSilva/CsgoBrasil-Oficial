package com.sd.csgobrasil.integracao.controller;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    private JacksonTester<User> userJson;

    @Autowired
    private JacksonTester<List<User>> userListJson;

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

    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Carlos", "9090", "ca@gmail", 200, null, "cliente"));
        users.add(new User(2L, "Administrador", "admin", "admin@admin.com", 100000, null, "admin"));
        users.add(new User(3L, "EstoqueDinamico", "admin", "estoqued@admin.com", 100000, null, "admin"));
        users.add(new User(4L, "EstoqueEstatico", "admin", "estoques@admin.com", 100000, null, "admin"));
        return users;
    }
}
