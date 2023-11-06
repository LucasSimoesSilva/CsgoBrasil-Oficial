package com.sd.csgobrasil.controllers;

import com.sd.csgobrasil.entity.DTO.UserLogin;
import com.sd.csgobrasil.entity.DTO.UserRegister;
import com.sd.csgobrasil.entity.User;
import com.sd.csgobrasil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    private UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(){
        List<User> users = service.listUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/info")
    public ResponseEntity<User> getUserInfo(@RequestBody String email){
        User userInfo = service.getUserInfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findByUserId(@PathVariable Long id){
        User user = service.findByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> checkLogin(@RequestBody UserLogin userLogin){
        boolean loginUser = service.checkLoginUser(userLogin);
        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> checkIfUserExist(@RequestBody UserRegister userRegister){
        boolean loginUser = service.checkIfUserExist(userRegister);
        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
        User addUser = service.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(addUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        User userUpdated = service.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
